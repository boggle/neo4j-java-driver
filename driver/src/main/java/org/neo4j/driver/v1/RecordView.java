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

/**
 * A record view provides access to an ordered, finite collection of zero or more fields.
 *
 * A field is a pair of a string key and a value.
 *
 * A record view must not contain multiple fields with the same key.
 */
public interface RecordView
{
    /**
     * @return an ordered sequence of all field's keys
     */
    Iterable<String> keys();

    /**
     * Retrieve the value of the field that has the given key
     *
     * @param key the field's key
     * @return the field's value or null if this record view does not contain a field with the given key
     */
    Value value( String key );

    /**
     * @return the number of fields accessible from this record view
     */
    int numberOfFields();

    /**
     * Retrieve the value of the field at the given index
     *
     * @param index the index of the field
     * @throws IndexOutOfBoundsException if an invalid index was given
     * @return the field's value
     */
    Value value( int index );

    /**
     * @return an immutable copy of the currently viewed record
     */
    Record record();
}
