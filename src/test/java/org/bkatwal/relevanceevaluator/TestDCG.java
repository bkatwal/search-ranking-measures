/*
The MIT License (MIT)
Copyright (c) Bikas Katwal - bikas.katwal10@gmail.com
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package org.bkatwal.relevanceevaluator;

import java.util.ArrayList;
import java.util.List;
import org.bkatwal.dto.DocRating;
import org.bkatwal.dto.DocRatingBuilder;
import org.bkatwal.dto.QueryResultsRating;
import org.junit.Assert;
import org.junit.Test;

public class TestDCG {

    @Test
    public void testEvalQueryBasedOnResultSetRating() {
        RelevanceEvaluator dcg = new DCG();

        List<DocRating> docsToEval = new ArrayList<>();

        docsToEval.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).grade(8D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).grade(10D).build());
        docsToEval
            .add(DocRatingBuilder.aDocRating().docId("3").relevant(false).grade(0.0D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).grade(6D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).grade(4D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).grade(5D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).grade(1D).build());

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        queryResultsRating.setQueryResultsDocRating(docsToEval);
        double metric = dcg.evalQuery(queryResultsRating);
        Assert.assertEquals(0.765, metric, 0.01D);
    }

    @Test
    public void testEvalIdealResult() {
        RelevanceEvaluator dcg = new DCG();

        List<DocRating> docsToEval = new ArrayList<>();

        docsToEval.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).grade(10D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).grade(9D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("3").relevant(false).grade(8D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).grade(7D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).grade(6D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).grade(5D).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).grade(4D).build());

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        queryResultsRating.setQueryResultsDocRating(docsToEval);
        double metric = dcg.evalQuery(queryResultsRating);
        Assert.assertEquals(1, metric, 0.01D);
    }

    @Test
    public void testEvalWithKnownGradedDocs() {
        RelevanceEvaluator dcg = new DCG();

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        List<DocRating> resultDocs = new ArrayList<>();

        resultDocs.add(DocRatingBuilder.aDocRating().docId("1").build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("2").build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("3").grade(8D).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("4").grade(7D).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("5").grade(6D).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("6").grade(5D).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("7").grade(4D).build());
        queryResultsRating.setQueryResultsDocRating(resultDocs);

        List<DocRating> knownRelevantDocs = new ArrayList<>();

        knownRelevantDocs.add(DocRatingBuilder.aDocRating().docId("1").grade(8D).build());
        knownRelevantDocs.add(DocRatingBuilder.aDocRating().docId("2").grade(7D).build());
        knownRelevantDocs.add(DocRatingBuilder.aDocRating().docId("6").grade(10D).build());
        knownRelevantDocs.add(DocRatingBuilder.aDocRating().docId("7").grade(9D).build());
        queryResultsRating.setKnownRelevantDocsRating(knownRelevantDocs);

        double metric = dcg.evalQuery(queryResultsRating);
        Assert.assertEquals(0.57, metric, 0.01D);
    }
}
