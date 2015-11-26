/**
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.driver.v1;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import org.neo4j.driver.internal.InternalType;

import static java.util.Collections.singletonList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.neo4j.driver.v1.Types.anyType;
import static org.neo4j.driver.v1.Types.booleanType;
import static org.neo4j.driver.v1.Types.floatType;
import static org.neo4j.driver.v1.Types.integerType;
import static org.neo4j.driver.v1.Types.listType;
import static org.neo4j.driver.v1.Types.mapType;
import static org.neo4j.driver.v1.Types.nodeType;
import static org.neo4j.driver.v1.Types.numberType;
import static org.neo4j.driver.v1.Types.pathType;
import static org.neo4j.driver.v1.Types.relationshipType;
import static org.neo4j.driver.v1.Types.stringType;
import static org.neo4j.driver.v1.Types.voidType;

public class TypesTest
{
    // Whenever adding types, be sure to update these lists of types below

    private List<InternalType> allTestedTypes()
    {
        List<InternalType> types = new ArrayList<>();
        addMaterialPlainTypes( types );
        addNullablePlainTypes( types );
        addMaterialListTypes( types );
        addNullableListTypes( types );
        return types;
    }

    private List<InternalType> testedMaterialTypes()
    {
        List<InternalType> types = new ArrayList<>();
        addMaterialPlainTypes( types );
        addMaterialListTypes( types );
        return types;
    }

    private List<InternalType> testedNullableTypes()
    {
        List<InternalType> types = new ArrayList<>();
        addNullablePlainTypes( types );
        addNullableListTypes( types );
        return types;
    }

    private void addMaterialPlainTypes( List<InternalType> types )
    {
        types.add( internal( anyType() ) );
        types.add( internal( booleanType() ) );
        types.add( internal( stringType() ) );
        types.add( internal( numberType() ) );
        types.add( internal( integerType() ) );
        types.add( internal( floatType() ) );
        types.add( internal( mapType() ) );
        types.add( internal( nodeType() ) );
        types.add( internal( relationshipType() ) );
    }

    private void addNullablePlainTypes( List<InternalType> types )
    {
        for ( InternalType t : materialPlainTypes() )
        {
            types.add( internal( t.nullableType() ) );
        }
        types.add( internal( voidType() ) );
    }

    private List<InternalType> materialPlainTypes()
    {
        List<InternalType> types = new ArrayList<>();
        addMaterialPlainTypes( types );
        return types;
    }

    private void addMaterialListTypes( List<InternalType> types )
    {
        for ( InternalType t : allPlainTypes() )
        {
            types.add( internal( listType( t ) ) );
            types.add( internal( listType( listType( t ) ) ) );
            types.add( internal( listType( listType( t ).nullableType() ) ) );
        }
    }

    private void addNullableListTypes( List<InternalType> types )
    {
        for ( InternalType t : allPlainTypes() )
        {
            types.add( internal( listType( t ).nullableType() ) );
            types.add( internal( listType( listType( t ) ).nullableType() ) );
            types.add( internal( listType( listType( t ).nullableType() ).nullableType() ) );
        }
    }

    private List<InternalType> allPlainTypes()
    {
        List<InternalType> types = new ArrayList<>();
        for ( InternalType t : materialPlainTypes() )
        {
            types.add( t );
            types.add( t.materialType() );
        }
        types.add( internal( voidType() ) );
        return types;
    }

    @Test
    public void shouldNameMaterialTypes()
    {
        assertThat( booleanType().name(), equalTo( "BOOLEAN" ) );
        assertThat( integerType().name(), equalTo( "INTEGER" ) );
        assertThat( floatType().name(), equalTo( "FLOAT" ) );
        assertThat( stringType().name(), equalTo( "STRING" ) );
        assertThat( numberType().name(), equalTo( "NUMBER" ) );
        assertThat( anyType().name(), equalTo( "ANY" ) );
        assertThat( nodeType().name(), equalTo( "NODE" ) );
        assertThat( relationshipType().name(), equalTo( "RELATIONSHIP" ) );
        assertThat( pathType().name(), equalTo( "PATH" ) );
        assertThat( mapType().name(), equalTo( "MAP" ) );
        assertThat( listType( booleanType() ).name(), equalTo( "LIST OF BOOLEAN" ) );
    }

    @Test
    public void shouldNameNullableTypes()
    {
        assertThat( booleanType().nullableType().name(), equalTo( "BOOLEAN?" ) );
        assertThat( integerType().nullableType().name(), equalTo( "INTEGER?" ) );
        assertThat( floatType().nullableType().name(), equalTo( "FLOAT?" ) );
        assertThat( stringType().nullableType().name(), equalTo( "STRING?" ) );
        assertThat( numberType().nullableType().name(), equalTo( "NUMBER?" ) );
        assertThat( anyType().nullableType().name(), equalTo( "ANY?" ) );
        assertThat( nodeType().nullableType().name(), equalTo( "NODE?" ) );
        assertThat( relationshipType().nullableType().name(), equalTo( "RELATIONSHIP?" ) );
        assertThat( pathType().nullableType().name(), equalTo( "PATH?" ) );
        assertThat( mapType().nullableType().name(), equalTo( "MAP?" ) );
        assertThat( listType( booleanType() ).nullableType().name(), equalTo( "LIST? OF BOOLEAN" ) );
        assertThat( listType( booleanType().nullableType() ).nullableType().name(), equalTo( "LIST? OF BOOLEAN?" ) );
        assertThat( listType( booleanType().nullableType() ).name(), equalTo( "LIST OF BOOLEAN?" ) );
        assertThat( listType( listType( booleanType().nullableType() ).nullableType() ).name(), equalTo( "LIST OF LIST? OF BOOLEAN?" ) );
    }

    @Test
    public void testAnyContainsAllMaterialTypes()
    {
        for ( Type t : testedMaterialTypes() )
        {
            if ( !t.equals( voidType() ) )
            {
                assertThat( anyType(), containsType( t ) );
            }
        }
    }

    @Test
    public void testMapContainsMapTypesOnly()
    {
        assertThat( mapType(), containsType( mapType() ) );
        assertThat( mapType(), containsType( nodeType() ) );
        assertThat( mapType(), containsType( relationshipType() ) );

        for ( Type t : testedMaterialTypes() )
        {
            if ( !(t.equals( nodeType() ) || t.equals( relationshipType() ) || t.equals( mapType() )) )
            {
                assertThat( mapType(), not( containsType( t ) ) );
            }
        }
    }

    @Test
    public void testNumberContainsNumberTypesOnly()
    {
        assertThat( numberType(), containsType( integerType() ) );
        assertThat( numberType(), containsType( floatType() ) );

        for ( Type t : testedMaterialTypes() )
        {
            if ( ! ( t.equals( integerType() ) || t.equals( floatType() ) || t.equals( numberType() ) ) )
            {
                assertThat( numberType(), not( containsType( t ) ) );
            }
        }
    }

    @Test
    public void testContainsIsReflexive()
    {
        for ( Type type : allTestedTypes() )
        {
            assertThat( type, containsType( type ) );
        }
    }

    @Test
    public void shouldComputeMaterialAndNullableTypesCorrectly()
    {
        for ( Type type : testedMaterialTypes() )
        {
            assertFalse( internal( type.nullableType() ).isMaterial() );
            assertTrue( type.nullableType().isNullable() );

            InternalType materialType = internal( internal( type ).materialType() );
            if ( materialType != null ) // VOID
            {
                assertFalse( materialType.isNullable() );
                assertTrue( materialType.isMaterial() );
                assertEquals( materialType.nullableType(), type.nullableType() );
            }

            assertEquals( internal( type.nullableType() ).materialType(), materialType );
        }
    }

    @Test
    public void testNullableAnyContainsEveryOtherType()
    {
        for ( Type t : allTestedTypes() )
        {
            assertThat( anyType().nullableType(), containsType( t ) );
        }
    }


    @Test
    public void testAnyDoesNotContainNullableTypes()
    {
        for ( Type t : testedNullableTypes() )
        {
            if ( ! t.equals( voidType() ) )
            {
                assertThat( anyType(), not( containsType( t ) ) );
            }
        }
    }

    @Test
    public void testAllNonVoidTypesHaveAMaterialType()
    {
        for ( Type t : allTestedTypes() )
        {
            assertTrue( implies( internal( t ).materialType() == null, voidType().equals( t ) ) );
        }
    }

    @Test
    public void testNullableTypesContainVoid()
    {
        for ( Type t : testedNullableTypes() )
        {
            assertThat( t, containsType( voidType() ) );
        }
    }

    @Test
    public void testMaterialTypesDoNotContainVoid()
    {
        for ( Type t : testedMaterialTypes() )
        {
            assertThat( t, not( containsType( voidType() ) ) );
        }
    }

    @Test
    public void shouldComputeTypeEquality()
    {
        // reflexive
        for ( Type t : allTestedTypes() )
        {
            assertThat( t, equalTo( t ) );
        }

        // symmetric
        for ( Type t1 : allTestedTypes() )
        {
            for ( Type t2 : allTestedTypes() )
            {
                if ( t1.equals( t2 ) )
                {
                    assertTrue( t2.equals( t1 ) );
                }
                else
                {
                    assertThat( t2, not( equalTo( t1) ) );
                }
            }
        }

        // transitive (if t1 == t2 && t2 == t3 => t1 == t3)
        for ( Type t1 : allTestedTypes() )
        {
            for ( Type t2 : allTestedTypes() )
            {
                for ( Type t3 : allTestedTypes() )
                {
                    boolean p1 = t1.equals( t2 );
                    boolean p2 = t2.equals( t3 );
                    boolean q = t1.equals( t3 );
//                    System.out.printf("%s, %s, %s (%s, %s, %s)%n", t1.name(), t2.name(), t3.name(), p1, p2, q);
                    assertTrue( !(p1 && p2) || q );
                }
            }
        }
    }

    @Test
    public void shouldHandleListTypeParameters()
    {
        assertThat( listType( numberType() ), containsType( listType( integerType() ) ) );
        assertThat( listType( listType( numberType() ) ), containsType( listType( listType( integerType() ) ) ) );
        assertThat( listType( listType( numberType() ).nullableType() ), containsType( listType( voidType() ) ) );
        assertThat( listType( numberType().nullableType() ), containsType( listType( integerType().nullableType() ) ) );
        assertThat( listType( numberType() ), not( containsType( listType( integerType().nullableType() ) ) ) );
        assertThat( listType( numberType() ), not( containsType( listType( mapType() ) ) ) );

        // List<T1 extends Type> vs List<T2 extends Type>
        assertThat( listType( integerType() ).parameters(), equalTo( singletonList( integerType() ) ) );
        assertThat( listType( integerType().nullableType() ).parameters(), equalTo( singletonList( integerType().nullableType() ) ) );
        assertThat( listType( listType( nodeType() ) ).parameters(), equalTo( singletonList( listType( nodeType() ) ) ) );
    }

    @Test
    public void testListTypesHaveSingleParameter()
    {
        for ( Type t : allTestedTypes() )
        {
            if ( isListType( t ) )
            {
                assertThat( t.parameters().size(), equalTo( 1 ) );
            }
        }
    }

    @Test
    public void testNonListTypesDontHaveParameters()
    {
        for ( Type t : allTestedTypes() )
        {
            if ( !isListType( t ) )
            {
                assertTrue( t.parameters().isEmpty() );
            }
        }
    }

    @Test
    public void shouldComputeTypeIntersection()
    {
        // reflexive
        for ( InternalType t : allTestedTypes() )
        {
            assertThat( t.join( t ), equalTo( t ) );
        }

        // nullable
        for ( InternalType t : allTestedTypes() )
        {
            assertThat( t.join( t.nullableType() ), equalTo( t.nullableType() ) );
        }

        // nullability of material types
        for ( InternalType t : testedMaterialTypes() )
        {
            assertThat( t.join( t.nullableType() ), equalTo( t.nullableType() ) );
            assertThat( internal( t.nullableType() ).join( t ), equalTo( t.nullableType() ) );
        }

        // void
        for ( InternalType t : allTestedTypes() )
        {
            assertThat( t.join( internal( voidType() ) ), equalTo( t.nullableType() ) );
        }

        // symmetric
        for ( InternalType t1 : allTestedTypes() )
        {
            for ( InternalType t2 : allTestedTypes() )
            {
                assertThat( t1.join( t2 ), equalTo( t2.join( t1 ) ) );
            }
        }

        // associative
        for ( InternalType t1 : allTestedTypes() )
        {
            for ( InternalType t2 : allTestedTypes() )
            {
                for ( InternalType t3 : allTestedTypes() )
                {
                    InternalType jFwd1 = t1.join( t2 );
                    InternalType jFwd2 = jFwd1.join( t3 );
                    InternalType jBwd1 = t2.join( t3 );
                    InternalType jBwd2 = t1.join( jBwd1 );
//                    System.out.printf("%s, %s, %s (%s -> %s : %s -> %s )%n", t1.name(), t2.name(), t3.name(), jFwd1, jFwd2, jBwd1, jBwd2);
                    assertThat( jFwd2, equalTo( jBwd2 ) );
                }
            }
        }

        // monotonic, i.e. moving upwards in the lattice
        for ( InternalType t1 : allTestedTypes() )
        {
            for ( InternalType t2 : allTestedTypes() )
            {
                Type joinedType = t1.join( t2 );
                assertThat( joinedType, containsType( t1 ) );
                assertThat( joinedType, containsType( t2 ) );
            }
        }

        // join with any: ANY? is the top-most type
        for ( InternalType t : allTestedTypes() )
        {
            assertThat( internal( anyType().nullableType() ).join( t ), equalTo( anyType().nullableType() ) );
        }

        // join with any: ANY is the top-most material type
        for ( InternalType t : testedMaterialTypes() )
        {
            assertThat( internal( anyType() ).join( t ), equalTo( anyType() ) );
        }

        // numbers
        assertThat( internal( integerType() ).join( internal( floatType() ) ), equalTo( numberType() ) );
        assertThat( internal( numberType() ).join( internal( booleanType() ) ), equalTo( anyType() ) );

        // maps
        assertThat( internal( nodeType() ).join( internal( mapType() ) ), equalTo( mapType() ) );
        assertThat( internal( relationshipType() ).join( internal( mapType() ) ), equalTo( mapType() ) );

        // handling lists
        for ( InternalType t1 : allTestedTypes() )
        {
            for ( InternalType t2 : allTestedTypes() )
            {
                assertThat(
                    internal( listType( t1 ) ).join( internal( listType( t2 ) ) ),
                    equalTo( listType( t1.join( t2 ) ) )
                );
            }
        }
    }

    private Matcher<Type> containsType( final Type subType )
    {
        return new BaseMatcher<Type>() {

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "subtype of " );
                description.appendValue( subType );
            }

            @Override
            public boolean matches( Object item )
            {
                if ( item instanceof Type )
                {
                    Type superType = (Type) item;
                    return superType.contains( subType );
                }
                else
                {
                    return false;
                }
            }
        };
    }

    private static InternalType internal( Type otherType )
    {
        return (InternalType) otherType;
    }

    private static boolean implies( boolean condition, boolean consequence )
    {
        return !condition || consequence;
    }

    private static boolean isListType( Type t )
    {
        return !voidType().equals( t ) && listType( anyType().nullableType() ).nullableType().contains( t );
    }
}
