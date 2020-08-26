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

package edu.rpi.tw.jena.query.fulltext;

import static org.junit.Assert.assertTrue;

import java.util.Arrays ;
import java.util.HashSet ;
import java.util.Map ;
import java.util.Set ;

import org.apache.jena.atlas.lib.StrUtils ;
import org.junit.Test ;

/*
 * This abstract class defines a collection of test methods for testing
 * test searches.  Its subclasses create a dataset using the index to 
 * to be tested and then call the test methods in this class to run
 * the actual tests.
 */
public abstract class AbstractTestDatasetWithTextIndex extends AbstractTestDatasetWithTextIndexBase {
    
    @Test
    public void testOneSimpleResult() {
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testOneSimpleResult>",
                "  rdfs:label 'bar testOneSimpleResult barfoo foo'",
                "."
                );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search ( 'testOneSimpleResult' 10 ) . ?s rdfs:label ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList("http://example.org/data/resource/testOneSimpleResult")) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    static final String R_S1 = RESOURCE_BASE + "s1" ;
    static final String R_S2 = RESOURCE_BASE + "s2" ;
    static final String PF_DATA = StrUtils.strjoinNL(
                                               TURTLE_PROLOG,
                                               "<" + R_S1 + "> rdfs:label 'text' .",
                                               "<" + R_S2 + "> rdfs:label 'fuzz' ."
                                               );
    
    @Test
    public void propertyFunctionText_1() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "     ?label text:search  'text' . ?s rdfs:label ?label.",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_1_dft() {
        // As before but using default field.
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "   ?o text:search 'text' . ?s ?p ?o. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }


    @Test
    public void propertyFunctionText_1_score() {
        // As before but capturing the score in a variable.
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s ?score",
                "WHERE {",
                "     (?o ?score) text:search 'text'. ?s ?p ?o. ",
                "}"
                );

        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearchWithScores(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_2() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search 'text' .",
                "    ?s rdfs:label ?label .",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_2_dft() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search 'text' .",
                "    ?s rdfs:label ?label .",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_2_score() {
        // As before but capturing the score in a variable.
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s ?score",
                "WHERE {",
                "    (?label ?score) text:search 'text' .",
                "    ?s rdfs:label ?label .",
                "}"
                );

        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearchWithScores(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_3() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search ('text') .",
                "    ?s rdfs:label ?label .",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }
    
    @Test
    public void propertyFunctionText_3_dft() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search 'text'.",
                "    ?s rdfs:label ?label .",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }
    
    @Test
    public void propertyFunctionText_3_score() {
        // As before but capturing the score in a variable.
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s ?score",
                "WHERE {",
                "    (?label ?score) text:search 'text' .",
                "    ?s rdfs:label ?label .",
                "}"
                );

        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearchWithScores(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_4() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?s rdfs:label 'text' .",
                "    ?label text:search 'fuzz' .",
                "    ?s rdfs:label ?label.",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_5() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    BIND('text' AS ?t)", 
                "    ?label text:search ?t . ?s rdfs:label ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_6() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    BIND(rdfs:label AS ?P)", 
                "    ?label text:search 'text' . ?s ?P ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }


    @Test
    public void propertyFunctionText_7() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    BIND(1 AS ?C)", 
                "    ?o text:search ( 'text' ?C) . ?s ?p ?o. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void propertyFunctionText_8() {
        final String turtle = PF_DATA ;
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?s rdfs:label 'text' .",
                "    ?label text:search 'text' . ?s ?p ?label. ",
                "    ?s rdfs:label 'text' .",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList( R_S1 ) ) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testMultipleResults() {
        String label = "testMultipleResults";
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + label +"1>",
                "  rdfs:label '" + label + "1'",
                ".",
                "<" + RESOURCE_BASE + label + "2>",
                "  rdfs:label '" + label + "2'",
                "."
                );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search (  '" + label + "?' 10 ) . ?s rdfs:label ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList(
                "http://example.org/data/resource/" + label + "1",
                "http://example.org/data/resource/" + label + "2"
            ));
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testMultipleResults_dft() {
        String label = "testMultipleResults";
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + label +"1>",
                "  rdfs:label '" + label + "1'",
                ".",
                "<" + RESOURCE_BASE + label + "2>",
                "  rdfs:label '" + label + "2'",
                "."
                );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?o text:search ('" + label + "?' 10 ) . ?s ?p ?o. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList(
                "http://example.org/data/resource/" + label + "1",
                "http://example.org/data/resource/" + label + "2"
            ));
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testMultipleResults_score() {
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "brownfox>",
                "  rdfs:label 'The quick brown fox jumped over the lazy dog.'",
                ".",
                "<" + RESOURCE_BASE + "redfox>",
                "  rdfs:label 'red fox'",
                "."
                );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s ?score",
                "WHERE {",
                "    (?label ?score) text:search ('brown fox' 10 ) . ?s rdfs:label ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>();
        expectedURIs.addAll( Arrays.asList(
                RESOURCE_BASE + "brownfox",
                RESOURCE_BASE + "redfox"
            ));
        Map<String,Float> scores = doTestSearchWithScores(turtle, queryString, expectedURIs);
        // the entry for ("The quick brown fox...") should have a higher score since it's a better match
        assertTrue(scores.get(RESOURCE_BASE + "brownfox") > scores.get(RESOURCE_BASE + "redfox"));
    }


    @Test
    public void testSearchDefaultField() {
        String label = "testSearchDefaultField";
        String label2 = "testSearchDefaultFieldOther";
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + label +"1>",
                "  rdfs:label '" + label + "1' ; ",
                "  rdfs:comment '" + label2 + "1' ;",
                ".",
                "<" + RESOURCE_BASE + label + "2>",
                "  rdfs:label '" + label2 + "2' ; ",
                "  rdfs:comment '" + label + "2' ; ",
                "."
                );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search ( '" + label + "?' 10 ) . ?s rdfs:label ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList(
                "http://example.org/data/resource/" + label + "1"
            ));
        doTestSearch("default field:", turtle, queryString, expectedURIs);
    }


    @Test
    public void testSearchLimitsResults() {
        String label = "testSearchLimitsResults";
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + label + "1>",
                "  rdfs:label '" + label + " 1' ;",
                ".",
                "<" + RESOURCE_BASE + label + "2>",
                "  rdfs:label '" + label + " 2' ;",
                ".",
                "<" + RESOURCE_BASE + label + "3>",
                "  rdfs:label '" + label + " 3' ;",
                ".",
                "<" + RESOURCE_BASE + label + "4>",
                "  rdfs:label '" + label + " 4' ;",
                "."
                );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search ( '" + label + "' 3 ) . ?s rdfs:label ?label. ",
                "}"
                );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList(
                        RESOURCE_BASE + label + "1",
                        RESOURCE_BASE + label + "2",
                        RESOURCE_BASE + label + "3",
                        RESOURCE_BASE + label + "4"
            ));
        doTestSearch("default field:", turtle, queryString, expectedURIs, 3 );
    }
}