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

package org.jetbrains.kotlin.resolve.scopes

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.KtType
import org.jetbrains.kotlin.utils.Printer

public abstract class KtScopeImpl : KtScope {
    override fun getClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null

    override fun getProperties(name: Name, location: LookupLocation): Collection<VariableDescriptor> = emptyList()

    override fun getLocalVariable(name: Name): VariableDescriptor? = null

    override fun getPackage(name: Name): PackageViewDescriptor? = null

    override fun getFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()

    override fun getSyntheticExtensionProperties(receiverTypes: Collection<KtType>, name: Name, location: LookupLocation): Collection<PropertyDescriptor> = emptyList()
    override fun getSyntheticExtensionFunctions(receiverTypes: Collection<KtType>, name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()

    override fun getSyntheticExtensionProperties(receiverTypes: Collection<KtType>): Collection<PropertyDescriptor> = emptyList()
    override fun getSyntheticExtensionFunctions(receiverTypes: Collection<KtType>): Collection<FunctionDescriptor> = emptyList()

    override fun getDeclarationsByLabel(labelName: Name): Collection<DeclarationDescriptor> = emptyList()

    override fun getDescriptors(kindFilter: DescriptorKindFilter,
                                nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> = emptyList()

    override fun getImplicitReceiversHierarchy(): List<ReceiverParameterDescriptor> = emptyList()

    override fun getOwnDeclaredDescriptors(): Collection<DeclarationDescriptor> = emptyList()

    // This method should not be implemented here by default: every scope class has its unique structure pattern
    abstract override fun printScopeStructure(p: Printer)
}