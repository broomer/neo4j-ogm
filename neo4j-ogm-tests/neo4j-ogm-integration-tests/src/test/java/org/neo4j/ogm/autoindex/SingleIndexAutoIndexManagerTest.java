/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
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
package org.neo4j.ogm.autoindex;

import static org.assertj.core.api.Assertions.*;

import org.junit.After;
import org.junit.Test;
import org.neo4j.ogm.domain.autoindex.SingleIndexEntity;

/**
 * @author Frantisek Hartman
 * @author Michael J. Simons
 * @author Gerrit Meier
 */
public class SingleIndexAutoIndexManagerTest extends BaseAutoIndexManagerTestClass {

    private static final String INDEX = "INDEX ON :`Entity`(`login`)";
    private static final String CONSTRAINT = "CONSTRAINT ON (entity:Entity) ASSERT entity.login IS UNIQUE";

    public SingleIndexAutoIndexManagerTest() {
        super(new String[] { INDEX }, SingleIndexEntity.class);
    }

    @Override
    protected void additionalTearDown() {
        executeDrop(CONSTRAINT);
        executeDrop(INDEX);
    }

    @Test
    public void testAutoIndexManagerUpdateConstraintChangedToIndex() {
        executeCreate(CONSTRAINT);

        runAutoIndex("update");

        executeForIndexes(indexes -> {
            assertThat(indexes).hasSize(1);
        });
        executeForConstraints(constraints -> {
            assertThat(constraints).isEmpty();
        });
    }
}
