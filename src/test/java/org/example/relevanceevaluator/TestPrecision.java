package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.DocRatingBuilder;
import org.example.dto.QueryResultsRating;
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

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        queryResultsRating.setInputDocRatings(docsToEval);
        double metric = precisionEvaluator.evalQuery(queryResultsRating);
        Assert.assertEquals(0.66, metric, 0.1D);
    }
}
