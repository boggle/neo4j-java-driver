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
package org.neo4j.driver.v1.internal;

import org.neo4j.driver.v1.CypherType;

/**
 * Additional utility methods for {@link CypherType} that currently are not exposed on the
 * external API
 */
public interface InternalType extends CypherType
{
    /**
     * @return true if this type does not include null
     */
    boolean isMaterial();

    /**
     * @return material type of this type (i.e. the same type but excluding null) or null if this is the VOID type
     */
    InternalType materialType();

    /**
     * Compute the least common super type of this type and another type
     *
     * @param otherType the other type
     * @return the least common super type of this and otherType
     */
    InternalType join( InternalType otherType );

    // Overridden to use InternalType as much as possible without having to cast

    @Override
    InternalType nullableType();
}
