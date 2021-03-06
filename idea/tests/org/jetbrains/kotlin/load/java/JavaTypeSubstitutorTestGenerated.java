/*
 * Copyright 2010-2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.load.java;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/typeSubstitution")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class JavaTypeSubstitutorTestGenerated extends AbstractJavaTypeSubstitutorTest {
    public void testAllFilesPresentInTypeSubstitution() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/typeSubstitution"), Pattern.compile("^(.+)\\.java$"), true);
    }

    @TestMetadata("arrayType.java")
    public void testArrayType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/arrayType.java");
        doTest(fileName);
    }

    @TestMetadata("classType.java")
    public void testClassType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/classType.java");
        doTest(fileName);
    }

    @TestMetadata("classWithWildcard.java")
    public void testClassWithWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/classWithWildcard.java");
        doTest(fileName);
    }

    @TestMetadata("genericArray.java")
    public void testGenericArray() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/genericArray.java");
        doTest(fileName);
    }

    @TestMetadata("innerParameter.java")
    public void testInnerParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/innerParameter.java");
        doTest(fileName);
    }

    @TestMetadata("intersectionType.java")
    public void testIntersectionType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/intersectionType.java");
        doTest(fileName);
    }

    @TestMetadata("intersectionTypeAsTypeParameter.java")
    public void testIntersectionTypeAsTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/intersectionTypeAsTypeParameter.java");
        doTest(fileName);
    }

    @TestMetadata("intersectionTypeInEnum.java")
    public void testIntersectionTypeInEnum() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/intersectionTypeInEnum.java");
        doTest(fileName);
    }

    @TestMetadata("intersectionTypeInInterfaceDeclaration.java")
    public void testIntersectionTypeInInterfaceDeclaration() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/intersectionTypeInInterfaceDeclaration.java");
        doTest(fileName);
    }

    @TestMetadata("intersectionTypeInTypeVariableClass.java")
    public void testIntersectionTypeInTypeVariableClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/intersectionTypeInTypeVariableClass.java");
        doTest(fileName);
    }

    @TestMetadata("mapEntry.java")
    public void testMapEntry() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/mapEntry.java");
        doTest(fileName);
    }

    @TestMetadata("primitiveType.java")
    public void testPrimitiveType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/primitiveType.java");
        doTest(fileName);
    }

    @TestMetadata("rawArrayType.java")
    public void testRawArrayType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawArrayType.java");
        doTest(fileName);
    }

    @TestMetadata("rawArrayTypeParameterWithBound.java")
    public void testRawArrayTypeParameterWithBound() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawArrayTypeParameterWithBound.java");
        doTest(fileName);
    }

    @TestMetadata("rawEnum.java")
    public void testRawEnum() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawEnum.java");
        doTest(fileName);
    }

    @TestMetadata("rawExtendsWildcard.java")
    public void testRawExtendsWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawExtendsWildcard.java");
        doTest(fileName);
    }

    @TestMetadata("rawIntersectionType.java")
    public void testRawIntersectionType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawIntersectionType.java");
        doTest(fileName);
    }

    @TestMetadata("rawSuperWildcard.java")
    public void testRawSuperWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawSuperWildcard.java");
        doTest(fileName);
    }

    @TestMetadata("rawType.java")
    public void testRawType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawType.java");
        doTest(fileName);
    }

    @TestMetadata("rawTypeInDeclaration.java")
    public void testRawTypeInDeclaration() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawTypeInDeclaration.java");
        doTest(fileName);
    }

    @TestMetadata("rawTypeWithBound.java")
    public void testRawTypeWithBound() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawTypeWithBound.java");
        doTest(fileName);
    }

    @TestMetadata("rawTypeWithSelfReferenceBound.java")
    public void testRawTypeWithSelfReferenceBound() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawTypeWithSelfReferenceBound.java");
        doTest(fileName);
    }

    @TestMetadata("rawWildcardInTypeVariableClass.java")
    public void testRawWildcardInTypeVariableClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawWildcardInTypeVariableClass.java");
        doTest(fileName);
    }

    @TestMetadata("rawWildcardWithBound.java")
    public void testRawWildcardWithBound() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/rawWildcardWithBound.java");
        doTest(fileName);
    }

    @TestMetadata("selfReference.java")
    public void testSelfReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/selfReference.java");
        doTest(fileName);
    }

    @TestMetadata("twoParameters.java")
    public void testTwoParameters() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/twoParameters.java");
        doTest(fileName);
    }

    @TestMetadata("twoParametersInSubClass.java")
    public void testTwoParametersInSubClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/twoParametersInSubClass.java");
        doTest(fileName);
    }

    @TestMetadata("typeVariableClass.java")
    public void testTypeVariableClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/typeVariableClass.java");
        doTest(fileName);
    }

    @TestMetadata("typeVariableRaw.java")
    public void testTypeVariableRaw() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/typeVariableRaw.java");
        doTest(fileName);
    }

    @TestMetadata("unboundedWildcard.java")
    public void testUnboundedWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/unboundedWildcard.java");
        doTest(fileName);
    }

    @TestMetadata("unboundedWildcardToTypeParameter.java")
    public void testUnboundedWildcardToTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/unboundedWildcardToTypeParameter.java");
        doTest(fileName);
    }

    @TestMetadata("varargArray.java")
    public void testVarargArray() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargArray.java");
        doTest(fileName);
    }

    @TestMetadata("varargArrayTypeParameter.java")
    public void testVarargArrayTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargArrayTypeParameter.java");
        doTest(fileName);
    }

    @TestMetadata("varargClass.java")
    public void testVarargClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargClass.java");
        doTest(fileName);
    }

    @TestMetadata("varargClassWithWildcard.java")
    public void testVarargClassWithWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargClassWithWildcard.java");
        doTest(fileName);
    }

    @TestMetadata("varargRawType.java")
    public void testVarargRawType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargRawType.java");
        doTest(fileName);
    }

    @TestMetadata("varargRawTypeWithBound.java")
    public void testVarargRawTypeWithBound() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargRawTypeWithBound.java");
        doTest(fileName);
    }

    @TestMetadata("varargToClass.java")
    public void testVarargToClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargToClass.java");
        doTest(fileName);
    }

    @TestMetadata("varargToClassWithWildcard.java")
    public void testVarargToClassWithWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/varargToClassWithWildcard.java");
        doTest(fileName);
    }

    @TestMetadata("wildcardExtends.java")
    public void testWildcardExtends() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/wildcardExtends.java");
        doTest(fileName);
    }

    @TestMetadata("wildcardExtendsObject.java")
    public void testWildcardExtendsObject() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/wildcardExtendsObject.java");
        doTest(fileName);
    }

    @TestMetadata("wildcardExtendsTypeParameter.java")
    public void testWildcardExtendsTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/wildcardExtendsTypeParameter.java");
        doTest(fileName);
    }

    @TestMetadata("wildcardSuper.java")
    public void testWildcardSuper() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/wildcardSuper.java");
        doTest(fileName);
    }

    @TestMetadata("wildcardToWildcard.java")
    public void testWildcardToWildcard() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/typeSubstitution/wildcardToWildcard.java");
        doTest(fileName);
    }
}
