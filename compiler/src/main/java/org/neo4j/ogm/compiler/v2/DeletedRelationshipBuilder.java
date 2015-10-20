/*
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 *
 */

package org.neo4j.ogm.compiler.v2;

import org.neo4j.ogm.api.compiler.DeletedRelationshipEmitter;

import java.util.Map;
import java.util.Set;

/**
 * @author Mark Angrish
 * @author Luanne Misquitta
 */
class DeletedRelationshipBuilder implements DeletedRelationshipEmitter {

    private final String type;
    private final String src;
    private final String tgt;
    private final String rid;
    private final Long relId;

    public DeletedRelationshipBuilder(String type, String src, String tgt, String rid, Long relId) {
        this.type = type;
        this.src = src;
        this.tgt = tgt;
        this.rid = rid;
        this.relId = relId;
    }

    public boolean emit(StringBuilder queryBuilder, Map<String, Object> parameters, Set<String> varStack) {

        if (!varStack.isEmpty()) {
            queryBuilder.append(" WITH ").append(NodeBuilder.toCsv(varStack));
        }

        queryBuilder.append(" MATCH (");
        queryBuilder.append(src);
        queryBuilder.append(")-[");
        queryBuilder.append(rid);
        queryBuilder.append(":`");
        queryBuilder.append(type);
        queryBuilder.append("`]->(");
        queryBuilder.append(tgt);
        queryBuilder.append(")");

        boolean where = false;

        if (!varStack.contains(src)) {
            queryBuilder.append(" WHERE id(");
            queryBuilder.append(src);
            queryBuilder.append(")={");
            queryBuilder.append(src); // existing nodes have an id. we pass it in as $id
            queryBuilder.append("}");
            parameters.put(src, Long.parseLong(src.substring(1)));
            varStack.add(src);
            where = true;
        }

        if (!varStack.contains(tgt)) {
            if (where) {
                queryBuilder.append(" AND id(");
            } else {
                queryBuilder.append(" WHERE id(");
            }
            queryBuilder.append(tgt);
            queryBuilder.append(")={");
            queryBuilder.append(tgt); // existing nodes have an id. we pass it in as $id
            queryBuilder.append("}");
            varStack.add(tgt);
            parameters.put(tgt, Long.parseLong(tgt.substring(1)));
            where = true;
        }

        if (!varStack.contains(rid) && relId!=null) {
            if (where) {
                queryBuilder.append(" AND id(");
            } else {
                queryBuilder.append(" WHERE id(");
            }
            queryBuilder.append(rid);
            queryBuilder.append(")={");
            queryBuilder.append(rid); // existing relationships have an id. we pass it in as $id
            queryBuilder.append("}");
            parameters.put(rid, relId);
            varStack.add(rid);
        }

        queryBuilder.append(" DELETE ");
        queryBuilder.append(rid);

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeletedRelationshipBuilder that = (DeletedRelationshipBuilder) o;

        if (!rid.equals(that.rid)) return false;
        if (!src.equals(that.src)) return false;
        if (!tgt.equals(that.tgt)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + src.hashCode();
        result = 31 * result + tgt.hashCode();
        result = 31 * result + rid.hashCode();
        return result;
    }

    @Override
    public int compareTo(DeletedRelationshipEmitter o) {
        return hashCode()-o.hashCode();
    }
}