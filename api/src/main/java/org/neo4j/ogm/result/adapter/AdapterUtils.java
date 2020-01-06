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
package org.neo4j.ogm.result.adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Luanne Misquitta
 * @author Andreas Berger
 * @author Michael J. Simons
 */
final class AdapterUtils {

    private static final Pattern PATTERN_COMPREHENSION_PATTERN = Pattern.compile("^\\[.+|.+\\]$");

    static Iterable<Object> convertToIterable(Object array) {
        List<Object> list = new ArrayList<>();
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            list.add(Array.get(array, i));
        }
        return list;
    }

    /**
     * Helper method that checks wether the given result key ("column name") describes a node generated by
     * pattern comprehension.
     *
     * @param resultKey
     * @return True, if the result key describes a node generated by pattern comprehension
     */
    static boolean describesGeneratedNode(String resultKey) {
        return PATTERN_COMPREHENSION_PATTERN.matcher(resultKey).matches();
    }

    private AdapterUtils() {
    }
}
