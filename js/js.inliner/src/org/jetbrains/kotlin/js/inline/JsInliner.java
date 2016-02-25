/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.js.inline;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.backend.js.ast.metadata.MetadataProperties;
import com.intellij.psi.PsiElement;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.descriptors.CallableDescriptor;
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor;
import org.jetbrains.kotlin.diagnostics.DiagnosticSink;
import org.jetbrains.kotlin.diagnostics.Errors;
import org.jetbrains.kotlin.js.inline.clean.RemoveUnusedFunctionDefinitionsKt;
import org.jetbrains.kotlin.js.inline.clean.RemoveUnusedLocalFunctionDeclarationsKt;
import org.jetbrains.kotlin.js.inline.context.FunctionContext;
import org.jetbrains.kotlin.js.inline.context.InliningContext;
import org.jetbrains.kotlin.js.inline.context.NamingContext;
import org.jetbrains.kotlin.js.inline.util.*;
import org.jetbrains.kotlin.js.translate.context.TranslationContext;
import org.jetbrains.kotlin.resolve.inline.InlineStrategy;

import java.util.*;

import static org.jetbrains.kotlin.js.inline.FunctionInlineMutator.canBeExpression;
import static org.jetbrains.kotlin.js.inline.FunctionInlineMutator.getInlineableCallReplacement;
import static org.jetbrains.kotlin.js.translate.utils.JsAstUtils.flattenStatement;

public class JsInliner extends JsVisitorWithContextImpl {

    private final IdentityHashMap<JsName, JsFunction> functions;
    private final Stack<JsInliningContext> inliningContexts = new Stack<JsInliningContext>();
    private final Set<JsFunction> processedFunctions = CollectionUtilsKt.IdentitySet();
    private final Set<JsFunction> inProcessFunctions = CollectionUtilsKt.IdentitySet();
    private final FunctionReader functionReader;
    private final DiagnosticSink trace;

    // these are needed for error reporting, when inliner detects cycle
    private final Stack<JsFunction> namedFunctionsStack = new Stack<JsFunction>();
    private final LinkedList<JsCallInfo> inlineCallInfos = new LinkedList<JsCallInfo>();
    private final Function1<JsNode, Boolean> canBeExtractedByInliner = new Function1<JsNode, Boolean>() {
        @Override
        public Boolean invoke(JsNode node) {
            if (!(node instanceof JsInvocation)) return false;

            JsInvocation call = (JsInvocation) node;

            if (hasToBeInlined(call)) {
                JsFunction function = getFunctionContext().getFunctionDefinition(call);
                return !canBeExpression(function);
            }

            return false;
        }
    };
    private final Map<DeclarationDescriptor, InliningContext.BlockInfo> blocks =
            new HashMap<DeclarationDescriptor, InliningContext.BlockInfo>();
    private final Deque<Holder<JsVars>> varsStack = new ArrayDeque<Holder<JsVars>>();
    private final Deque<Holder<JsVars.JsVar>> varStack = new ArrayDeque<Holder<JsVars.JsVar>>();
    private final Deque<Integer> cutIndexStack = new ArrayDeque<Integer>();

    public static JsProgram process(@NotNull TranslationContext context) {
        JsProgram program = context.program();
        IdentityHashMap<JsName, JsFunction> functions = CollectUtilsKt.collectNamedFunctions(program);
        JsInliner inliner = new JsInliner(functions, new FunctionReader(context), context.bindingTrace());
        inliner.accept(program);
        RemoveUnusedFunctionDefinitionsKt.removeUnusedFunctionDefinitions(program, functions);
        return program;
    }

    private JsInliner(
            @NotNull IdentityHashMap<JsName, JsFunction> functions,
            @NotNull FunctionReader functionReader,
            @NotNull DiagnosticSink trace
    ) {
        this.functions = functions;
        this.functionReader = functionReader;
        this.trace = trace;
    }

    @Override
    public boolean visit(@NotNull JsVars x, @NotNull JsContext ctx) {
        varsStack.push(new Holder<JsVars>(x));
        cutIndexStack.push(0);
        return super.visit(x, ctx);
    }

    @Override
    public void endVisit(@NotNull JsVars x, @NotNull JsContext ctx) {
        super.endVisit(x, ctx);
        varsStack.pop();
        int cutIndex = cutIndexStack.pop();
        if (cutIndex > 0) {
            x.getVars().subList(0, cutIndex).clear();
        }
    }

    @Override
    public boolean visit(@NotNull JsVars.JsVar x, @NotNull JsContext ctx) {
        varStack.push(new Holder<JsVars.JsVar>(x));
        return super.visit(x, ctx);
    }

    @Override
    public void endVisit(@NotNull JsVars.JsVar x, @NotNull JsContext ctx) {
        varStack.pop();
        super.endVisit(x, ctx);
    }

    @Override
    public boolean visit(@NotNull JsFunction function, @NotNull JsContext context) {
        inliningContexts.push(new JsInliningContext(function));
        assert !inProcessFunctions.contains(function): "Inliner has revisited function";
        inProcessFunctions.add(function);

        if (functions.containsValue(function)) {
            namedFunctionsStack.push(function);
        }

        return super.visit(function, context);
    }

    @Override
    public void endVisit(@NotNull JsFunction function, @NotNull JsContext context) {
        super.endVisit(function, context);
        NamingUtilsKt.refreshLabelNames(getInliningContext().newNamingContext(), function);

        RemoveUnusedLocalFunctionDeclarationsKt.removeUnusedLocalFunctionDeclarations(function);
        processedFunctions.add(function);

        assert inProcessFunctions.contains(function);
        inProcessFunctions.remove(function);

        inliningContexts.pop();

        if (!namedFunctionsStack.empty() && namedFunctionsStack.peek() == function) {
            namedFunctionsStack.pop();
        }
    }

    @Override
    public boolean visit(@NotNull JsInvocation call, @NotNull JsContext context) {
        if (!hasToBeInlined(call)) return true;

        JsFunction containingFunction = getCurrentNamedFunction();

        if (containingFunction != null) {
            inlineCallInfos.add(new JsCallInfo(call, containingFunction));
        }

        JsFunction definition = getFunctionContext().getFunctionDefinition(call);

        if (inProcessFunctions.contains(definition))  {
            reportInlineCycle(call, definition);
        }
        else if (!processedFunctions.contains(definition)) {
            accept(definition);
        }

        return true;
    }

    @Override
    public void endVisit(@NotNull JsInvocation x, @NotNull JsContext ctx) {
        if (hasToBeInlined(x)) {
            inline(x, ctx);
        }

        JsCallInfo lastCallInfo = null;

        if (!inlineCallInfos.isEmpty()) {
            lastCallInfo = inlineCallInfos.getLast();
        }

        if (lastCallInfo != null && lastCallInfo.call == x) {
            inlineCallInfos.removeLast();
        }
    }

    @Override
    protected void doAcceptStatementList(List<JsStatement> statements) {
        // at top level of js ast, contexts stack can be empty,
        // but there is no inline calls anyway
        if(!inliningContexts.isEmpty()) {
            JsScope scope = getFunctionContext().getScope();
            int i = 0;

            while (i < statements.size()) {
                List<JsStatement> additionalStatements =
                        ExpressionDecomposer.preserveEvaluationOrder(scope, statements.get(i), canBeExtractedByInliner);
                statements.addAll(i, additionalStatements);
                i += additionalStatements.size() + 1;
            }
        }

        super.doAcceptStatementList(statements);
    }

    private void inline(@NotNull JsInvocation call, @NotNull JsContext context) {
        JsInliningContext inliningContext = getInliningContext();
        FunctionContext functionContext = getFunctionContext();
        functionContext.declareFunctionConstructorCalls(call.getArguments());
        InlineableResult inlineableResult = getInlineableCallReplacement(call, inliningContext);

        JsStatement inlineableBody = inlineableResult.getInlineableBody();
        JsExpression resultExpression = inlineableResult.getResultExpression();
        JsContext<JsStatement> statementContext = inliningContext.getStatementContext();
        // body of inline function can contain call to lambdas that need to be inlined
        JsStatement inlineableBodyWithLambdasInlined = accept(inlineableBody);
        assert inlineableBody == inlineableBodyWithLambdasInlined;

        // When we deal with `var v1 = e1, v2 = e2` and e2 emits inlineable body, split it into `var v1 = e1` and `var v2 = e2`
        // to place inlineable body in between.
        JsVars outerVars = varsStack.peek().value;
        if (outerVars != null) {
            JsVars.JsVar outerVar = varStack.peek().value;
            int index = outerVars.getVars().indexOf(outerVar);
            int lastCutIndex = cutIndexStack.pop();
            if (index > lastCutIndex) {
                JsVars leftVars = new JsVars(new ArrayList<JsVars.JsVar>(outerVars.getVars().subList(lastCutIndex, index)),
                                             outerVars.isMultiline());
                cutIndexStack.push(index);
                statementContext.addPrevious(leftVars);
            } else {
                cutIndexStack.push(lastCutIndex);
            }
        }
        statementContext.addPrevious(flattenStatement(inlineableBody));

        /**
         * Assumes, that resultExpression == null, when result is not needed.
         * @see FunctionInlineMutator.isResultNeeded()
         */
        if (resultExpression == null) {
            statementContext.removeMe();
            return;
        }

        resultExpression = accept(resultExpression);
        JsStatement currentStatement = statementContext.getCurrentNode();

        if (currentStatement instanceof JsExpressionStatement &&
            ((JsExpressionStatement) currentStatement).getExpression() == call &&
            (resultExpression == null || !SideEffectUtilsKt.canHaveSideEffect(resultExpression))
        ) {
            statementContext.removeMe();
        }
        else {
            context.replaceMe(resultExpression);
        }
    }

    @NotNull
    private JsInliningContext getInliningContext() {
        return inliningContexts.peek();
    }

    @NotNull FunctionContext getFunctionContext() {
        return getInliningContext().getFunctionContext();
    }

    @Nullable
    private JsFunction getCurrentNamedFunction() {
        if (namedFunctionsStack.empty()) return null;
        return namedFunctionsStack.peek();
    }

    private void reportInlineCycle(@NotNull JsInvocation call, @NotNull JsFunction calledFunction) {
        MetadataProperties.setInlineStrategy(call, InlineStrategy.NOT_INLINE);
        Iterator<JsCallInfo> it = inlineCallInfos.descendingIterator();

        while (it.hasNext()) {
            JsCallInfo callInfo = it.next();
            PsiElement psiElement = MetadataProperties.getPsiElement(callInfo.call);

            CallableDescriptor descriptor = MetadataProperties.getDescriptor(callInfo.call);
            if (psiElement != null && descriptor != null) {
                trace.report(Errors.INLINE_CALL_CYCLE.on(psiElement, descriptor));
            }

            if (callInfo.containingFunction == calledFunction) {
                break;
            }
        }
    }

    public boolean hasToBeInlined(@NotNull JsInvocation call) {
        InlineStrategy strategy = MetadataProperties.getInlineStrategy(call);
        if (strategy == null || !strategy.isInline()) return false;

        return getFunctionContext().hasFunctionDefinition(call);
    }

    @Override
    public boolean visit(@NotNull JsBlock block, @NotNull JsContext ctx) {
        varsStack.push(new Holder<JsVars>(null));
        return super.visit(block, ctx);
    }

    @Override
    public void endVisit(@NotNull JsBlock x, @NotNull JsContext ctx) {
        varsStack.pop();
        super.endVisit(x, ctx);
    }

    private class JsInliningContext implements InliningContext {
        private final FunctionContext functionContext;

        JsInliningContext(JsFunction function) {
            functionContext = new FunctionContext(function, this, functionReader) {
                @Nullable
                @Override
                protected JsFunction lookUpStaticFunction(@Nullable JsName functionName) {
                    return functions.get(functionName);
                }
            };
        }

        @NotNull
        @Override
        public NamingContext newNamingContext() {
            JsScope scope = getFunctionContext().getScope();
            return new NamingContext(scope, getStatementContext());
        }

        @NotNull
        @Override
        public JsContext<JsStatement> getStatementContext() {
            return getLastStatementLevelContext();
        }

        @NotNull
        @Override
        public FunctionContext getFunctionContext() {
            return functionContext;
        }

        @NotNull
        @Override
        public Map<DeclarationDescriptor, InliningContext.BlockInfo> getBlocks() {
            return blocks;
        }
    }

    private static class JsCallInfo {
        @NotNull
        public final JsInvocation call;

        @NotNull
        public final JsFunction containingFunction;

        private JsCallInfo(@NotNull JsInvocation call, @NotNull JsFunction function) {
            this.call = call;
            containingFunction = function;
        }
    }

    static class Holder<T> {
        final T value;

        public Holder(T value) {
            this.value = value;
        }
    }
}
