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
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.neo4j.driver.Values.parameters;

@RunWith( DocTestRunner.class )
public class ValueDocIT
{
    private final Value exampleValue = Values.value(
            parameters( "users", asList( parameters( "name", "Anders" ), parameters( "name", "John" ) ) ));

    public void classDocTreeExample( DocSnippet snippet )
    {
        // given
        snippet.set( "value", exampleValue );

        // when
        snippet.run();

        // then
        assertThat( snippet.get("username"), equalTo( (Object)"John" ));
    }

    public void classDocIterationExample( DocSnippet snippet )
    {
        // given
        snippet.addImport( LinkedList.class );
        snippet.addImport( List.class );
        snippet.set( "value", exampleValue );

        // when
        snippet.run();

        // then
        assertThat( snippet.get("names"), equalTo( (Object)asList("Anders","John") ));
    }
}
