/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
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
package org.neo4j.kernel.impl.coreapi;

import org.neo4j.exceptions.CypherExecutionException;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.NotInTransactionException;
import org.neo4j.kernel.impl.core.NodeEntity;
import org.neo4j.kernel.impl.core.RelationshipEntity;

import static java.lang.String.format;
import static org.neo4j.values.storable.Values.utf8Value;

/**
 * Default implementation of {@link org.neo4j.graphdb.Transaction}
 */
abstract class EntityValidationTransactionImpl implements InternalTransaction
{

    @Override
    public <E extends Entity> E validateSameDB( E entity )
    {
        InternalTransaction internalTransaction;

        if (entity instanceof NodeEntity) {
            internalTransaction = ((NodeEntity) entity).getTransaction();
        } else if (entity instanceof RelationshipEntity) {
            internalTransaction = ((RelationshipEntity) entity).getTransaction();
        } else {
            internalTransaction = null;
        }

        if (internalTransaction != null) {
            if ( !internalTransaction.isOpen() ) {
                throw new NotInTransactionException("must be open");
            } else if ( internalTransaction.databaseId() != this.databaseId() )
            {
                throw new CypherExecutionException( "Node passed in is from another database. Not allowed to pass results from another database." );
            }
        }

        return entity;
    }
}
