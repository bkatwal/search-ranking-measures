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

import org.bkatwal.dto.DocRating;
import org.bkatwal.dto.DocRatingBuilder;
import org.bkatwal.dto.QueryRating;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestPrecision {

    @Test
    public void precision() {
        RelevanceEvaluator precisionEvaluator = new Precision(6);
        List<DocRating> docsToEval = new ArrayList<>();

        docsToEval.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("3").relevant(false).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("5").relevant(false).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).build());

        QueryRating queryRating = new QueryRating();
        queryRating.setQueryResultsDocRating(docsToEval);
        double metric = precisionEvaluator.evalQuery(queryRating);
        Assert.assertEquals(0.66, metric, 0.1D);
    }
}
