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

import java.util.Collections;
import java.util.List;

import org.neo4j.driver.internal.InternalType;

/**
 * Static helper methods for constructing various types.
 *
 * @see Type
 */
public final class Types
{
    private Types()
    {
        throw new UnsupportedOperationException( "Must not instantiate" );
    }

    // *** ANY ***

    /**
     * @return the "ANY" type
     */
    public static Type anyType()
    {
        return _anyType();
    }

    private static InternalType _anyType() { return ANY_TYPE; }

    private static final MaterialType ANY_TYPE = new MaterialType()
    {
        @Override
        public String constructorName()
        {
            return "ANY";
        }

        @Override
        public InternalType parent()
        {
            return this;
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_ANY_TYPE;
        }

        @Override
        protected boolean containsOther( InternalType otherType )
        {
            return !otherType.isNullable();
        }
    };

    private static final NullableType NULLABLE_ANY_TYPE = new NullableType( ANY_TYPE );


    // *** LIST ***

    /**
     * @param elementType the type of list elements
     * @return a "LIST OF" type
     */
    public static Type listType( final Type elementType ) {
        return _listType( internal( elementType ) );
    }

    private static InternalType _listType( final InternalType elementType) {
        return new ListType( elementType );
    }

    // *** MAP ***

    /**
     * @return the "MAP" type
     */
    public static Type mapType()
    {
        return _mapType();
    }

    private static InternalType _mapType() { return MAP_TYPE; }

    private static final MaterialType MAP_TYPE = new MaterialType()
    {
        @Override
        public String constructorName()
        {
            return "MAP";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_MAP_TYPE;
        }

        @Override
        protected boolean containsOther( InternalType otherType )
        {
            return nodeType().equals( otherType ) || relationshipType().equals( otherType );
        }
    };

    private static final NullableType NULLABLE_MAP_TYPE = new NullableType( MAP_TYPE );


    // *** NODE ***

    /**
     * @return the "NODE" type
     */
    public static Type nodeType()
    {
        return NODE_TYPE;
    }

    private static final MaterialType NODE_TYPE = new MapLeafType()
    {
        @Override
        public String constructorName()
        {
            return "NODE";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_NODE_TYPE;
        }
    };

    private static final NullableType NULLABLE_NODE_TYPE = new NullableType( NODE_TYPE );


    // *** RELATIONSHIP ***

    /**
     * @return the "RELATIONSHIP" type
     */
    public static Type relationshipType()
    {
        return RELATIONSHIP_TYPE;
    }

    private static final MaterialType RELATIONSHIP_TYPE = new MapLeafType()
    {
        @Override
        public String constructorName()
        {
            return "RELATIONSHIP";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_RELATIONSHIP_TYPE;
        }
    };

    private static final NullableType NULLABLE_RELATIONSHIP_TYPE = new NullableType( RELATIONSHIP_TYPE );


    // *** PATH ***

    /**
     * @return the "PATH" type
     */
    public static Type pathType() { return PATH_TYPE; }

    private static final MaterialType PATH_TYPE = new LeafType()
    {
        @Override
        public String constructorName()
        {
            return "PATH";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_PATH_TYPE;
        }
    };

    private static final NullableType NULLABLE_PATH_TYPE = new NullableType( PATH_TYPE );


    // *** NUMBER ***

    /**
     * @return the "NUMBER" type
     */
    public static Type numberType()
    {
        return _numberType();
    }

    private static InternalType _numberType() { return NUMBER_TYPE; }

    private static final MaterialType NUMBER_TYPE = new MaterialType()
    {
        @Override
        public String constructorName()
        {
            return "NUMBER";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_NUMBER_TYPE;
        }

        @Override
        protected boolean containsOther( InternalType otherType )
        {
            return integerType().equals( otherType )|| floatType().equals( otherType );
        }
    };

    private static final NullableType NULLABLE_NUMBER_TYPE = new NullableType( NUMBER_TYPE );


    // *** BOOLEAN ***

    /**
     * @return the "BOOLEAN" type
     */
    public static Type booleanType()
    {
        return BOOLEAN_TYPE;
    }

    private static final MaterialType BOOLEAN_TYPE = new LeafType()
    {
        @Override
        public String constructorName()
        {
            return "BOOLEAN";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_BOOLEAN_TYPE;
        }
    };

    private static final NullableType NULLABLE_BOOLEAN_TYPE = new NullableType( BOOLEAN_TYPE );


    // *** STRING ***

    /**
     * @return the "STRING" type
     */
    public static Type stringType()
    {
        return STRING_TYPE;
    }

    private static final MaterialType STRING_TYPE = new LeafType()
    {
        @Override
        public String constructorName()
        {
            return "STRING";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_STRING_TYPE;
        }
    };

    private static final NullableType NULLABLE_STRING_TYPE = new NullableType( STRING_TYPE );


    // *** INTEGER ***

    /**
     * @return the "INTEGER" type
     */
    public static Type integerType()
    {
        return INTEGER_TYPE;
    }

    private static final MaterialType INTEGER_TYPE = new NumberLeafType()
    {
        @Override
        public String constructorName()
        {
            return "INTEGER";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_INTEGER_TYPE;
        }
    };

    private static final NullableType NULLABLE_INTEGER_TYPE = new NullableType( INTEGER_TYPE );


    // *** FLOAT ***

    /**
     * @return the "FLOAT" type
     */
    public static Type floatType()
    {
        return FLOAT_TYPE;
    }

    private static final MaterialType FLOAT_TYPE = new NumberLeafType()
    {
        @Override
        public String constructorName()
        {
            return "FLOAT";
        }

        @Override
        public InternalType nullableType()
        {
            return NULLABLE_FLOAT_TYPE;
        }
    };

    private static final NullableType NULLABLE_FLOAT_TYPE = new NullableType( FLOAT_TYPE );


    // *** VOID ***

    /**
     * @return the "VOID" type
     */
    public static Type voidType()
    {
        return VOID_TYPE;
    }

    private static final Type VOID_TYPE = new VoidType();


    // *** IMPLEMENTATION CLASSES ***

    private static abstract class MapLeafType extends LeafType
    {
        @Override
        public InternalType parent()
        {
            return _mapType();
        }
    }

    private static abstract class NumberLeafType extends LeafType
    {
        @Override
        public InternalType parent()
        {
            return _numberType();
        }
    }

    private static final class ListType extends MaterialType
    {
        private final InternalType elementType;

        public ListType( InternalType elementType )
        {
            this.elementType = elementType;
        }

        @Override
        protected boolean containsOther( InternalType otherType )
        {
            if ( otherType instanceof ListType )
            {
                ListType otherListType = (ListType) otherType;
                return elementType.contains( otherListType.elementType );
            }
            else
            {
                return false;
            }
        }

        @Override
        public String name()
        {
            return constructorName() + " " + elementType.name();
        }

        @Override
        public String constructorName()
        {
            return "LIST OF";
        }

        public List<Type> parameters()
        {
            return Collections.<Type>singletonList( elementType );
        }

        @Override
        public InternalType nullableType()
        {
            String nullableConstructor = "LIST? OF";
            return new NullableType(
                nullableConstructor + " " + elementType.name(),
                nullableConstructor,
                this
            );
        }

        @Override
        public InternalType join( InternalType otherType )
        {
            if ( otherType.isNullable() )
            {
                return otherType.join( this );
            }
            else if ( otherType instanceof ListType )
            {
                ListType otherListType = (ListType) otherType;
                return _listType( elementType.join( otherListType.elementType ) );
            }
            else
            {
                return super.join( otherType );
            }
        }
    }

    private static class VoidType extends SimpleType
    {
        @Override
        protected boolean containsOther( InternalType otherType )
        {
            return false;
        }

        @Override
        public String constructorName()
        {
            return "VOID";
        }

        @Override
        public InternalType parent()
        {
            return _anyType().nullableType();
        }

        @Override
        public boolean isNullable()
        {
            return true;
        }

        @Override
        public InternalType materialType()
        {
            return null;
        }

        @Override
        public InternalType nullableType()
        {
            return this;
        }

        @Override
        public InternalType join( InternalType otherType )
        {
            return otherType.nullableType();
        }
    }

    private static final class NullableType extends SimpleType
    {
        private final String nullableName;
        private final String nullableConstructor;
        private final MaterialType materialType;

        protected NullableType( MaterialType materialType )
        {
            this( materialType.name() + "?", materialType.constructorName() + "?", materialType );
        }

        protected NullableType( String nullableName, String nullableConstructor, MaterialType materialType )
        {
            this.nullableName = nullableName;
            this.nullableConstructor = nullableConstructor;
            this.materialType = materialType;
        }

        @Override
        public String name()
        {
            return nullableName;
        }

        @Override
        public String constructorName()
        {
            return nullableConstructor;
        }

        public List<Type> parameters()
        {
            return materialType.parameters();
        }

        @Override
        public InternalType parent()
        {
            return materialType.parent().nullableType();
        }

        @Override
        public boolean isNullable()
        {
            return true;
        }

        @Override
        public InternalType materialType()
        {
            return materialType;
        }

        @Override
        public InternalType nullableType()
        {
            return this;
        }

        @Override
        protected boolean containsOther( InternalType otherType )
        {
            return    otherType == VOID_TYPE
                   || materialType.contains( otherType.materialType() );
        }

        @Override
        public InternalType join( InternalType otherType )
        {
            return materialType.join( otherType ).nullableType();
        }
    }

    private static abstract class LeafType extends MaterialType
    {
        @Override
        protected final boolean containsOther( InternalType otherType )
        {
            return false;
        }
    }

    private static abstract class MaterialType extends SimpleType
    {
        @Override
        public final boolean isNullable()
        {
            return false;
        }

        @Override
        public final InternalType materialType()
        {
            return this;
        }
    }

    private static abstract class SimpleType implements InternalType
    {
        @Override
        public final String toString()
        {
            return "Type{" + name() + "}";
        }

        @Override
        public final int hashCode()
        {
            return name().hashCode();
        }

        @Override
        public final boolean equals( Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            else if ( obj instanceof SimpleType )
            {
                SimpleType otherType = (SimpleType) obj;
                return containsOther( otherType ) && otherType.containsOther( this );
            }
            else
            {
                return false;
            }
        }

        @Override
        public String name()
        {
            return constructorName();
        }

        public abstract String constructorName();

        public List<Type> parameters()
        {
            return Collections.emptyList();
        }

        /**
         * @return smallest super type of this with the same nullability and ignoring any type parameters
         */
        public InternalType parent()
        {
            return _anyType();
        }

        public boolean isMaterial() {
            return !isNullable();
        }

        @Override
        public final boolean contains( Type otherType )
        {
            if ( otherType == null )
            {
                throw new NullPointerException( "Expected type but got: null" );
            }
            else
            {
                return (this == otherType) || containsOther( internal( otherType ) );
            }
        }

        protected abstract boolean containsOther( InternalType otherType );

        @Override
        public InternalType join( InternalType otherType )
        {
            assert isMaterial();

            if ( voidType().equals( otherType ) )
            {
                return nullableType();
            }

            Type otherMaterialType = otherType.materialType();

            if ( contains( otherMaterialType ) )
            {
                return otherType.isNullable() ? nullableType() : this;
            }

            if ( otherMaterialType.contains( this ) )
            {
                return otherType;
            }

            return parent().join( ((SimpleType) otherType).parent() );
        }
    }

    private static InternalType internal( Type otherType )
    {
        return (InternalType) otherType;
    }
}
