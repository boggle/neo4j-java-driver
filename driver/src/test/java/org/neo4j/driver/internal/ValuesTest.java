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
package org.neo4j.driver.internal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Value;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.internal.value.BooleanValue;
import org.neo4j.driver.internal.value.FloatValue;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.MapValue;
import org.neo4j.driver.internal.value.TextValue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.neo4j.driver.Values.value;
import static org.neo4j.driver.Values.valueToBoolean;
import static org.neo4j.driver.Values.valueToDouble;
import static org.neo4j.driver.Values.valueToFloat;
import static org.neo4j.driver.Values.valueToInt;
import static org.neo4j.driver.Values.valueToList;
import static org.neo4j.driver.Values.valueToLong;
import static org.neo4j.driver.Values.valueToString;
import static org.neo4j.driver.Values.values;

public class ValuesTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldConvertPrimitiveArrays() throws Throwable
    {
        assertThat( value( new int[]{1, 2, 3} ),
                equalTo( (Value) new ListValue( values( 1, 2, 3 ) ) ) );

        assertThat( value( new short[]{1, 2, 3} ),
                equalTo( (Value) new ListValue( values( 1, 2, 3 ) ) ) );

        assertThat( value( new char[]{'a', 'b', 'c'} ),
                equalTo( (Value) new TextValue( "abc" ) ) );

        assertThat( value( new long[]{1, 2, 3} ),
                equalTo( (Value) new ListValue( values( 1, 2, 3 ) ) ) );

        assertThat( value( new float[]{1.1f, 2.2f, 3.3f} ),
                equalTo( (Value) new ListValue( values( 1.1f, 2.2f, 3.3f ) ) ) );

        assertThat( value( new double[]{1.1, 2.2, 3.3} ),
                equalTo( (Value) new ListValue( values( 1.1, 2.2, 3.3 ) ) ) );

        assertThat( value( new boolean[]{true, false, true} ),
                equalTo( (Value) new ListValue( values( true, false, true ) ) ) );

        assertThat( value( new String[]{"a", "b", "c"} ),
                equalTo( (Value) new ListValue( values( "a", "b", "c" ) ) ) );
    }

    @Test
    public void shouldComplainAboutStrangeTypes() throws Throwable
    {
        // Expect
        exception.expect( ClientException.class );
        exception.expectMessage( "Unable to convert java.lang.Object to Neo4j Value." );

        // When
        value( new Object() );
    }

    @Test
    public void equalityRules() throws Throwable
    {
        assertEquals( value( 1 ), value( 1 ) );
        assertEquals( value( Long.MAX_VALUE ), value( Long.MAX_VALUE ) );
        assertEquals( value( Long.MIN_VALUE ), value( Long.MIN_VALUE ) );
        assertNotEquals( value( 1 ), value( 2 ) );

        assertEquals( value( 1.1337 ), value( 1.1337 ) );
        assertEquals( value( Double.MAX_VALUE ), value( Double.MAX_VALUE ) );
        assertEquals( value( Double.MIN_VALUE ), value( Double.MIN_VALUE ) );

        assertEquals( value( true ), value( true ) );
        assertEquals( value( false ), value( false ) );
        assertNotEquals( value( true ), value( false ) );

        assertEquals( value( "Hello" ), value( "Hello" ) );
        assertEquals( value( "This åäö string ?? contains strange Ü" ),
                value( "This åäö string ?? contains strange Ü" ) );
        assertEquals( value( "" ), value( "" ) );
        assertNotEquals( value( "Hello" ), value( "hello" ) );
        assertNotEquals( value( "This åäö string ?? contains strange " ),
                value( "This åäö string ?? contains strange Ü" ) );
    }

    @Test
    public void shouldMapDriverComplexTypesToListOfJavaPrimitiveTypes() throws Throwable
    {
        // Given
        Map<String,Value> map = new HashMap<>();
        map.put( "Cat", new ListValue( values( "meow", "miaow" ) ) );
        map.put( "Dog", new ListValue( values( "wow" ) ) );
        map.put( "Wrong", new ListValue( values( -1 ) ) );
        MapValue values = new MapValue( map );

        // When
        List<List<String>> list = values.javaList( valueToList( valueToString() ) );

        // Then
        assertEquals( 3, list.size() );
        int i = 0;
        for ( Value value : values )
        {
            assertEquals( value.get( 0 ).javaString(), list.get( i ).get( 0 ) );
            i++;
        }
    }

    @Test
    public void shouldMapDriverSimpleTypesToListOfJavaPrimitiveTypes() throws Throwable
    {
        assertEquals( "string", new TextValue( "string" ).javaList( valueToString() ).get( 0 ) );

        assertFalse( new BooleanValue( false ).javaList( valueToBoolean() ).get( 0 ) );

        assertThat( -1, equalTo( (int) (new IntegerValue( -1 ).javaList( valueToInt() ).get( 0 )) ) );
        assertThat( -1L, equalTo( (long) (new IntegerValue( -1 ).javaList( valueToLong() ).get( 0 )) ) );

        assertThat( -1.1F, equalTo( (float) (new FloatValue( -1.1 ).javaList( valueToFloat() ).get( 0 )) ) );
        assertThat( -1.1, equalTo( (double) (new FloatValue( -1.1 ).javaList( valueToDouble() ).get( 0 )) ) );
    }
}
