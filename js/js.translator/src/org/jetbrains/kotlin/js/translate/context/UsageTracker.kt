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

package org.jetbrains.kotlin.js.translate.context

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.resolve.DescriptorUtils.isAncestor
import com.google.dart.compiler.backend.js.ast.JsName
import org.jetbrains.kotlin.js.translate.utils.ManglingUtils.getSuggestedName
import com.google.dart.compiler.backend.js.ast.JsFunctionScope
import org.jetbrains.kotlin.resolve.DescriptorUtils

private val CAPTURED_RECEIVER_NAME_PREFIX : String = "this$"

class UsageTracker(
        private val parent: UsageTracker?,
        val containingDescriptor: MemberDescriptor,
        private val scope: JsFunctionScope
) {

    private val captured = linkedMapOf<DeclarationDescriptor, JsName>()

    // For readonly access from external places.
    val capturedDescriptorToJsName: Map<DeclarationDescriptor, JsName>
        get() = captured

    fun used(descriptor: DeclarationDescriptor) {
        if (isCaptured(descriptor)) return

        // local named function
        if (descriptor is FunctionDescriptor && descriptor.visibility == Visibilities.LOCAL) {
            assert(!descriptor.getName().isSpecial) { "Function with special name can not be captured, descriptor: $descriptor" }
            captureIfNeed(descriptor)
        }
        // local variable
        else if (descriptor is VariableDescriptor && descriptor !is PropertyDescriptor) {
            captureIfNeed(descriptor)
        }
        // this or receiver
        else if (descriptor is ReceiverParameterDescriptor) {
            captureIfNeed(descriptor)
        }
        else if (descriptor is TypeParameterDescriptor && descriptor.isReified) {
            captureIfNeed(descriptor)
        }
    }

    private fun captureIfNeed(descriptor: DeclarationDescriptor?) {
        if (descriptor == null || isCaptured(descriptor) || isAncestor(containingDescriptor, descriptor, /* strict = */ true)) return

        parent?.captureIfNeed(descriptor)

        captured[descriptor] = descriptor.getJsNameForCapturedDescriptor()
    }

    private fun DeclarationDescriptor.getJsNameForCapturedDescriptor(): JsName {
        val suggestedName = when (this) {
            is ReceiverParameterDescriptor -> this.getNameForCapturedReceiver()
            is TypeParameterDescriptor -> Namer.isInstanceSuggestedName(this)
            else -> getSuggestedName(this)
        }

        return scope.declareFreshName(suggestedName)
    }
}

fun UsageTracker.getNameForCapturedDescriptor(descriptor: DeclarationDescriptor): JsName? = capturedDescriptorToJsName.get(descriptor)

fun UsageTracker.hasCapturedExceptContaining(): Boolean {
    val hasNotCaptured =
            capturedDescriptorToJsName.isEmpty() ||
            (capturedDescriptorToJsName.size == 1 && capturedDescriptorToJsName.containsKey(containingDescriptor))

    return !hasNotCaptured
}

fun UsageTracker.isCaptured(descriptor: DeclarationDescriptor): Boolean = capturedDescriptorToJsName.containsKey(descriptor)

// NOTE: don't use from other places to avoid name clashes! So, it is not in Namer.
private fun ReceiverParameterDescriptor.getNameForCapturedReceiver(): String {

    fun DeclarationDescriptor.getNameForCapturedDescriptor(namePostfix: String = ""): String {
        val name = this.name
        val nameAsString = if (name.isSpecial) "" else name.asString()

        return CAPTURED_RECEIVER_NAME_PREFIX + nameAsString + namePostfix
    }

    val containingDeclaration = this.containingDeclaration

    assert(containingDeclaration is MemberDescriptor) {
        "Unsupported descriptor type: ${containingDeclaration.javaClass}, " +
        "receiverDescriptor = $this, " +"containingDeclaration = $containingDeclaration"
    }

    if (DescriptorUtils.isCompanionObject(containingDeclaration)) {
        return containingDeclaration.containingDeclaration!!.getNameForCapturedDescriptor(namePostfix = "$")
    }

    return containingDeclaration.getNameForCapturedDescriptor()
}
