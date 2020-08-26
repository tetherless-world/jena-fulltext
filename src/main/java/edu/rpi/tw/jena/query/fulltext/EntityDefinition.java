/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.rpi.tw.jena.query.fulltext ;

import java.util.Collection ;
import java.util.Collections ;
import java.util.HashMap ;
import java.util.Map ;

import org.apache.jena.ext.com.google.common.collect.ArrayListMultimap;
import org.apache.jena.ext.com.google.common.collect.ListMultimap;
import org.apache.jena.graph.Node ;
import org.apache.jena.rdf.model.Resource ;
import org.apache.lucene.analysis.Analyzer ;

/**
 * Definition of a "document"
 */
public class EntityDefinition {
    // private final Collection<String> fields =
    // Collections.unmodifiableCollection(fieldToPredicate.keySet()) ;
    private final String                     entityField ;
    private final String                     primaryField ;
    private String                           graphField = null ;
    private String                           langField ="lang";
    private String                           uidField ;

    private boolean                          cacheQueries;

    /**
     * @param entityField
     *            The entity being indexed (e.g. it's URI).
     * @param primaryField
     *            The primary/default field to search
     */
    public EntityDefinition(String entityField, String primaryField) {
        this.entityField = entityField ;
        this.primaryField = primaryField ;
    }

    /**
     * @param entityField
     *            The entity being indexed (e.g. it's URI).
     * @param primaryField
     *            The primary/default field to search
     * @param graphField
     *            The field that stores graph URI, or null
     */
    public EntityDefinition(String entityField, String primaryField, String graphField) {
        this(entityField, primaryField) ;
        setGraphField(graphField);
    }


    public String getEntityField() {
        return entityField ;
    }


    public String getPrimaryField() {
        return primaryField ;
    }

    public String getGraphField() {
        return graphField ;
    }

    public void setGraphField(String graphField) {
        this.graphField = graphField;
    }

    public String getLangField() {
        return langField;
    }

    public void setLangField(String langField) {
        this.langField = langField;
    }

    public String getUidField() {
        return uidField;
    }

    public void setUidField(String uidField) {
        this.uidField = uidField;
    }

    private static <T> T getOne(Collection<T> collection) {
        if ( collection.size() != 1 )
            return null ;
        return collection.iterator().next() ;
    }

    public boolean areQueriesCached() {
        return cacheQueries;
    }
    
    public void setCacheQueries(boolean cacheQueries) {
        this.cacheQueries = cacheQueries;
    }
    
    @Override
    public String toString() {
        return entityField ;
        
    }
}
