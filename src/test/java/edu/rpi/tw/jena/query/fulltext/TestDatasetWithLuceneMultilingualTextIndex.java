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

import org.apache.jena.assembler.Assembler;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.tw.jena.query.fulltext.assembler.TextAssembler;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestDatasetWithLuceneMultilingualTextIndex extends AbstractTestDatasetWithTextIndexBase {
    
    private static final String SPEC_BASE = "http://example.org/spec#";
    private static final String SPEC_ROOT_LOCAL = "lucene_text_dataset";
    private static final String SPEC_ROOT_URI = SPEC_BASE + SPEC_ROOT_LOCAL;
    private static final String SPEC;

    static final String DIR = "testing/TextQuery" ;

    static {
        SPEC = StrUtils.strjoinNL(
                    "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ",
                    "prefix skos: <http://www.w3.org/2004/02/skos/core#> ",
                    "prefix ja:   <http://jena.hpl.hp.com/2005/11/Assembler#> ",
                    "prefix tdb:  <http://jena.hpl.hp.com/2008/tdb#>",
                    "prefix text: <http://jena.apache.org/fulltext#>",
                    "prefix :     <" + SPEC_BASE + ">",
                    "",
                    "[] ja:loadClass    \"edu.rpi.tw.jena.query.fulltext.TextQuery\" .",
                    "text:TextDataset      rdfs:subClassOf   ja:RDFDataset .",
                    "text:TextIndexLucene  rdfs:subClassOf   text:TextIndex .",

                    ":" + SPEC_ROOT_LOCAL,
                    "    a              text:TextDataset ;",
                    "    text:dataset   :dataset ;",
                    "    text:index     :indexLucene ;",
                    "    .",
                    "",
                    ":dataset",
                    "    a                     tdb:DatasetTDB ;",
                    "    tdb:location          \"--mem--\" ;",
//                    "    tdb:unionDefaultGraph true ;",
                    ".",
                    "",
                    ":indexLucene",
                    "    a text:TextIndexLucene ;",
                    "    text:directory \"mem\" ;",
                    "    text:storeValues true ;",
                    "    text:multilingualSupport true ;",
                    "    text:entityMap <#entMap> ;",
                    "    .",
                    "<#entMap> a text:EntityMap ;",
                    "    text:entityField      'uri' ;",
                    "    text:defaultField     'text' ;",
                    "    .",
                    ""
                    );
    }
    
    @Before
    public void before() {
        Reader reader = new StringReader(SPEC);
        Model specModel = ModelFactory.createDefaultModel();
        specModel.read(reader, "", "TURTLE");
        TextAssembler.init();
        Resource root = specModel.getResource(SPEC_ROOT_URI);
        dataset = (Dataset) Assembler.general.open(root);
    }
    
    @After
    public void after() {
        dataset.close();
    }
    
    @Test
    public void testNoResultsOnFirstCreateIndex(){
        String turtle = "";
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search ('book'@en  10 ). ?s rdfs:label ?label .",
                "}"
                );
        doTestSearch(turtle, queryString, new HashSet<String>());
    }

    @Test
    public void testRetrievingEnglishLocalizedResource(){
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testEnglishLocalizedResource>",
                "  rdfs:label 'He offered me a gift'@en",
                ".",
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testGermanLocalizedResource>",
                "  rdfs:label 'Er schluckte gift'@de",
                "."
        );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search 'gift'@en . ?s rdfs:label ?label.",
//                "    ?s rdfs:label 'He offered me a gift'@en . ",
                "}"
        );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList(RESOURCE_BASE+"testEnglishLocalizedResource")) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testRetrievingGermanLocalizedResource(){
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testEnglishLocalizedResource>",
                "  rdfs:label 'He offered me a gift'@en",
                ".",
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testGermanLocalizedResource>",
                "  rdfs:label 'Er schluckte gift'@de",
                "."
        );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search ('gift'@de 10 ) . ?s rdfs:label ?label. ",
                "}"
        );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList("http://example.org/data/resource/testGermanLocalizedResource")) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testEnglishStemming(){
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testEnglishStemming>",
                "  rdfs:label 'I met some engineers'@en",
                "."
        );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search (  'engineering'@en 10 ) . ?s rdfs:label ?label. ",
                "}"
        );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList("http://example.org/data/resource/testEnglishStemming")) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testRetrievingUnlocalizedResource(){
        final String turtle = StrUtils.strjoinNL(
                TURTLE_PROLOG,
                "<" + RESOURCE_BASE + "testLocalizedResource>",
                "  rdfs:label 'A localized text'@en",
                ".",
                "<" + RESOURCE_BASE + "testUnlocalizedResource>",
                "  rdfs:label 'An unlocalized text'",
                "."
        );
        String queryString = StrUtils.strjoinNL(
                QUERY_PROLOG,
                "SELECT ?s",
                "WHERE {",
                "    ?label text:search (  'text' 10 ) . ?s rdfs:label ?label. ",
                "}"
        );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll( Arrays.asList(
        		RESOURCE_BASE + "testUnlocalizedResource", 
        		RESOURCE_BASE + "testLocalizedResource")) ;
        doTestSearch(turtle, queryString, expectedURIs);
    }

    @Test
    public void testRetrievingSKOSConcepts() {
        String queryString = StrUtils.strjoinNL(
                "PREFIX text: <http://jena.apache.org/fulltext#>",
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
                "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>",
                "SELECT ?s",
                "WHERE {",
                "    { ?label text:search (  'frites'@fr ). ?s skos:prefLabel ?label.  }",
                "    UNION ",
                "    { ?label text:search (  'Kartoffelpüree'@de ). ?s skos:prefLabel ?label.  }" ,
                "}"
        );
        Set<String> expectedURIs = new HashSet<>() ;
        expectedURIs.addAll(Arrays.asList("http://example.com/dishes#fries",
                                          "http://example.com/dishes#mashed")) ;

        dataset.begin(ReadWrite.WRITE);
        Model model = dataset.getDefaultModel();
        RDFDataMgr.read(model, DIR + "/data.skos", Lang.RDFXML);
        dataset.commit();
        doTestQuery(dataset, "", queryString, expectedURIs, expectedURIs.size());
    }
}
