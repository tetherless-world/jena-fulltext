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

import org.apache.jena.query.Dataset ;
import org.apache.jena.tdb.TDBFactory ;
import org.apache.jena.vocabulary.RDFS ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.ByteBuffersDirectory ;
import org.junit.After ;
import org.junit.Before ;

import edu.rpi.tw.jena.query.fulltext.EntityDefinition;
import edu.rpi.tw.jena.query.fulltext.TextDatasetFactory;
import edu.rpi.tw.jena.query.fulltext.TextIndex;
import edu.rpi.tw.jena.query.fulltext.TextIndexConfig;
import edu.rpi.tw.jena.query.fulltext.TextIndexLucene;


/**
 * This abstract class defines a setup configuration for a dataset with a graph-enabled Lucene index.
 */
public class AbstractTestDatasetWithLuceneGraphTextIndex extends AbstractTestDatasetWithGraphTextIndex {

    @Before
    public void init() {
        Dataset ds1 = TDBFactory.createDataset() ;
        Directory dir = new ByteBuffersDirectory() ;
        EntityDefinition eDef = new EntityDefinition("iri", "text");
        eDef.setGraphField("graph");
        TextIndex tidx = new TextIndexLucene(dir, new TextIndexConfig(eDef)) ;
        dataset = TextDatasetFactory.create(ds1, tidx) ;
    }

    @After
    public void teardown() {
        dataset.close();
    }

}
