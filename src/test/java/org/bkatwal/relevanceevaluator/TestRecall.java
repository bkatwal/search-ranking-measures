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

public class TestRecall {

    @Test
    public void evalQueryGivenKnownDocsList() {
        RelevanceEvaluator recall = new Recall();

        QueryResultsRating queryResultsRating = new QueryResultsRating();

        List<DocRating> resultDocs = new ArrayList<>();
        resultDocs.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("3").relevant(false).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).build());
        resultDocs.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).build());

        queryResultsRating.setQueryResultsDocRating(resultDocs);

        List<DocRating> knownDocs = new ArrayList<>();
        knownDocs.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).build());
        knownDocs.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).build());
        knownDocs.add(DocRatingBuilder.aDocRating().docId("10").relevant(true).build());

        queryResultsRating.setKnownRelevantDocsRating(knownDocs);

        double metric = recall.evalQuery(queryResultsRating);
        Assert.assertEquals(0.85, metric, 0.01D);
    }
}
