/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.driver;

import javadoctest.DocSnippet;
import javadoctest.DocTestRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;

import org.neo4j.driver.util.TestSession;

import static org.junit.Assert.assertEquals;

@RunWith( DocTestRunner.class )
public class TransactionDocIT
{
    @Rule
    public TestSession session = new TestSession();

    /** @see Transaction */
    public void classDoc( DocSnippet snippet )
    {
        // Given
        snippet.set( "session", session );

        // When I run the snippet
        snippet.run();

        // Then a node should've been created
        assertEquals( 1, session.run( "MATCH (n) RETURN count(n)" ).single().get( "count(n)" ).javaInteger() );
    }

    /** @see Transaction#failure()  */
    public void failure( DocSnippet snippet )
    {
        // Given
        snippet.set( "session", session );

        // When I run the snippet
        snippet.run();

        // Then a node should've been created
        assertEquals( 0, session.run( "MATCH (n) RETURN count(n)" ).single().get( "count(n)" ).javaInteger() );
    }
}
