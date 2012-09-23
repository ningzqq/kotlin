/*
 * Copyright 2010-2012 JetBrains s.r.o.
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


package org.jetbrains.jet.di;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.BuiltinsScopeExtensionMode;
import org.jetbrains.jet.lang.descriptors.ModuleDescriptor;
import org.jetbrains.jet.lang.resolve.BindingTrace;
import org.jetbrains.jet.lang.resolve.java.*;

import javax.annotation.PreDestroy;

/* This file is generated by org.jetbrains.jet.di.AllInjectorsGenerator. DO NOT EDIT! */
public class InjectorForJavaDescriptorResolver {

    private final Project project;
    private final BindingTrace bindingTrace;
    private final ModuleDescriptor moduleDescriptor;
    private final BuiltinsScopeExtensionMode builtinsScopeExtensionMode;
    private JavaBridgeConfiguration javaBridgeConfiguration;
    private JavaSemanticServices javaSemanticServices;
    private JavaDescriptorResolver javaDescriptorResolver;
    private PsiClassFinderImpl psiClassFinder;
    private JavaTypeTransformer javaTypeTransformer;
    private JavaDescriptorSignatureResolver javaDescriptorSignatureResolver;

    public InjectorForJavaDescriptorResolver(
        @NotNull Project project,
        @NotNull BindingTrace bindingTrace,
        @NotNull ModuleDescriptor moduleDescriptor,
        @NotNull BuiltinsScopeExtensionMode builtinsScopeExtensionMode
    ) {
        this.project = project;
        this.bindingTrace = bindingTrace;
        this.moduleDescriptor = moduleDescriptor;
        this.builtinsScopeExtensionMode = builtinsScopeExtensionMode;
        this.javaBridgeConfiguration = new JavaBridgeConfiguration();
        this.javaSemanticServices = new JavaSemanticServices();
        this.javaDescriptorResolver = new JavaDescriptorResolver();
        this.psiClassFinder = new PsiClassFinderImpl();
        this.javaTypeTransformer = new JavaTypeTransformer();
        this.javaDescriptorSignatureResolver = new JavaDescriptorSignatureResolver();

        javaBridgeConfiguration.setBuiltinsScopeExtensionMode(builtinsScopeExtensionMode);
        javaBridgeConfiguration.setJavaSemanticServices(javaSemanticServices);
        javaBridgeConfiguration.setProject(project);

        this.javaSemanticServices.setDescriptorResolver(javaDescriptorResolver);
        this.javaSemanticServices.setPsiClassFinder(psiClassFinder);
        this.javaSemanticServices.setTrace(bindingTrace);
        this.javaSemanticServices.setTypeTransformer(javaTypeTransformer);

        this.javaDescriptorResolver.setJavaDescriptorSignatureResolver(javaDescriptorSignatureResolver);
        this.javaDescriptorResolver.setProject(project);
        this.javaDescriptorResolver.setPsiClassFinder(psiClassFinder);
        this.javaDescriptorResolver.setSemanticServices(javaSemanticServices);
        this.javaDescriptorResolver.setTrace(bindingTrace);

        this.psiClassFinder.setProject(project);

        javaTypeTransformer.setJavaSemanticServices(javaSemanticServices);
        javaTypeTransformer.setResolver(javaDescriptorResolver);

        javaDescriptorSignatureResolver.setJavaDescriptorResolver(javaDescriptorResolver);
        javaDescriptorSignatureResolver.setJavaSemanticServices(javaSemanticServices);

        javaBridgeConfiguration.init();

        psiClassFinder.initialize();

    }

    @PreDestroy
    public void destroy() {
    }

    public Project getProject() {
        return this.project;
    }

    public BindingTrace getBindingTrace() {
        return this.bindingTrace;
    }

    public ModuleDescriptor getModuleDescriptor() {
        return this.moduleDescriptor;
    }

    public JavaSemanticServices getJavaSemanticServices() {
        return this.javaSemanticServices;
    }

    public JavaDescriptorResolver getJavaDescriptorResolver() {
        return this.javaDescriptorResolver;
    }

    public PsiClassFinderImpl getPsiClassFinder() {
        return this.psiClassFinder;
    }

}
