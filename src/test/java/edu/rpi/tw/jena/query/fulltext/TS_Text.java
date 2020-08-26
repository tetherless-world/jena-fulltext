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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.rpi.tw.jena.query.fulltext.assembler.TestEntityMapAssembler;
import edu.rpi.tw.jena.query.fulltext.assembler.TestGenericAnalyzerAssembler;
import edu.rpi.tw.jena.query.fulltext.assembler.TestPropListsAssembler;
import edu.rpi.tw.jena.query.fulltext.assembler.TestTextDatasetAssembler;
import edu.rpi.tw.jena.query.fulltext.assembler.TestTextIndexLuceneAssembler;

@RunWith(Suite.class)
@SuiteClasses({

    TestBuildTextDataset.class
    , TestDatasetWithLuceneTextIndex.class
    , TestDatasetWithLuceneMultilingualTextIndex.class
    , TestDatasetWithLuceneTextIndexWithLangField.class
    , TestDatasetWithLuceneGraphTextIndex.class
    , TestDatasetWithLuceneTextIndexDeletionSupport.class
    , TestDatasetWithLuceneStoredLiterals.class
    
    , TestTextNonTxn.class
    , TestTextTxn.class
    , TestTextNonTxnTDB1.class
    , TestTextTxnTDB.class

    , TestEntityMapAssembler.class
    , TestTextDatasetAssembler.class
    , TestTextIndexLuceneAssembler.class
    , TestDatasetWithSimpleAnalyzer.class
    , TestDatasetWithStandardAnalyzer.class
    , TestDatasetWithKeywordAnalyzer.class
    , TestDatasetWithLowerCaseKeywordAnalyzer.class
    , TestLuceneWithMultipleThreads.class
    , TestDatasetWithLocalizedAnalyzer.class
    , TestDatasetWithConfigurableAnalyzer.class
    , TestDatasetWithAnalyzingQueryParser.class
    , TestDatasetWithComplexPhraseQueryParser.class
    , TestDatasetWithSurroundQueryParser.class
    , TestGenericAnalyzerAssembler.class
    , TestTextGraphIndexExtra.class
    , TestTextGraphIndexExtra2.class
    , TestTextDefineAnalyzers.class
    , TestTextMultilingualEnhancements.class
    
    , TestPropListsAssembler.class
    , TestTextMultilingualEnhancements02.class
})

public class TS_Text
{}
