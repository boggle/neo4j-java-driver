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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Result;
import org.neo4j.driver.v1.ResultSummary;
import org.neo4j.driver.v1.Value;

public class SimpleResult implements Result
{
    private final List<String> keys;
    private final Iterator<Record> iter;
    private final ResultSummary summary;

    private boolean open = true;
    private Record current = null;
    private int position = -1;

    public SimpleResult( List<String> keys, List<Record> body, ResultSummary summary )
    {
        this.keys = keys;
        this.iter = body.iterator();
        this.summary = summary;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public ResultSummary summarize()
    {
        last();
        return summary;
    }

    @Override
    public boolean isOpen()
    {
        return open;
    }

    @Override
    public void close()
    {
        if ( open )
        {
            open = false;
        }
        else
        {
            throw new IllegalStateException( "Already closed" );
        }
    }

    @Override
    public int position()
    {
        assertOpen();

        return position;
    }

    @Override
    public boolean atEnd()
    {
        assertOpen();

        return !iter.hasNext();
    }

    @Override
    public boolean next()
    {
        assertOpen();

        if ( iter.hasNext() )
        {
            current = iter.next();
            position += 1;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int skip( int elements )
    {
        if ( elements < 0 )
        {
            throw new IllegalArgumentException( "Cannot skip negative number of elements" );
        }
        else
        {
            int skipped = 0;
            while ( skipped < elements && next() )
            {
                skipped += 1;
            }
            return skipped;
        }
    }

    @Override
    public boolean first()
    {
        int pos = position();
        return pos == -1 ? next() : pos == 0;
    }

    @Override
    public boolean single()
    {
        return first() && atEnd();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean last()
    {
        while ( next() ) ;
        return ( position() >= 0 );
    }

    @Override
    public List<Record> asList()
    {
        if ( first() )
        {
            List<Record> result = new ArrayList<>();
            do
            {
                result.add( record() );
            } while ( next() );
            return result;
        }
        else
        {
            return Collections.emptyList();
        }
    }

    public Value value( int index )
    {
        return current == null ? null : current.value( index );
    }

    @Override
    public Record record()
    {
        assertOpen();
        if ( current == null )
        {
            current = new EmptyRecord( keys );
        }
        return current;
    }

    public Value value( String key )
    {
        assertOpen();
        return current == null ? null : current.value( key );
    }

    @Override
    public int numberOfFields()
    {
        return keys.size();
    }

    public Iterable<String> keys()
    {
        return keys;
    }

    private void assertOpen()
    {
        if ( !open )
        {
            throw new IllegalStateException( "Cursor already closed" );
        }
    }
}
