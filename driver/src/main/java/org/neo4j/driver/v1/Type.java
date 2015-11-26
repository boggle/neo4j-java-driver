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

import java.util.List;

/**
 * Type of a value in the public type system used by Cypher and Neo4j
 *
 * @see Value
 * @see Values
 * @see Types
 */
public interface Type
{
    /**
     * @return the literal type name
     */
    String name();

    /**
     * @return all type parameters (currently only used by LIST)
     */
    List<Type> parameters();

    /**
     * @return true if this type includes null
     */
    boolean isNullable();

    /**
     * @return nullable type of this type, i.e. the same type but including null
     */
    Type nullableType();

    /**
     * Test if this type is a super type of another type
     *
     * @param otherType the contained type
     * @return true if this type is a super type of otherType
     */
    boolean contains( Type otherType );
}
