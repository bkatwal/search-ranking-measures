package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.DocRatingBuilder;
import org.example.dto.QueryResultsRating;
import org.example.dto.RelevanceVal;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAveragePrecision {

    @Test
    public void testAveragePrecision() {
        RelevanceEvaluator relevanceEvaluator = new AveragePrecision();

        List<DocRating> docsToEval = new ArrayList<>();

        docsToEval.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("3").relevant(false).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).build());

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        queryResultsRating.setInputDocRatings(docsToEval);
        double metric = relevanceEvaluator.evalQuery(queryResultsRating);
        Assert.assertEquals(0.75, metric, 0.1D);

        List<DocRating> docsToEval1 = new ArrayList<>();

        docsToEval1.add(DocRatingBuilder.aDocRating().docId("1").relevant(false).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("3").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).build());

        QueryResultsRating queryResultsRating1 = new QueryResultsRating();
        queryResultsRating1.setInputDocRatings(docsToEval1);
        double metric1 = relevanceEvaluator.evalQuery(queryResultsRating1);
        Assert.assertTrue(metric > metric1);
    }

    @Test
    public void testMeanAveragePrecision() {
        RelevanceEvaluator relevanceEvaluator = new AveragePrecision();
        Map<Integer, QueryResultsRating> queryResultsRatingMap = new HashMap<>();

        List<DocRating> docsToEval = new ArrayList<>();

        docsToEval.add(DocRatingBuilder.aDocRating().docId("1").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("3").relevant(false).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).build());
        docsToEval.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).build());

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        queryResultsRating.setInputDocRatings(docsToEval);
        queryResultsRatingMap.put(1, queryResultsRating);

        List<DocRating> docsToEval1 = new ArrayList<>();
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("1").relevant(false).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("2").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("3").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("4").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("5").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("6").relevant(true).build());
        docsToEval1.add(DocRatingBuilder.aDocRating().docId("7").relevant(true).build());

        QueryResultsRating queryResultsRating1 = new QueryResultsRating();
        queryResultsRating1.setInputDocRatings(docsToEval1);
        queryResultsRatingMap.put(2, queryResultsRating1);

        RelevanceVal relevanceVal = relevanceEvaluator.evalAveraged(queryResultsRatingMap);

        Assert.assertNotNull(relevanceVal);
        Assert.assertEquals(0.75D, relevanceVal.getQueryInstanceToMetric().get(1), 0.1D);
        Assert.assertEquals(0.62D, relevanceVal.getQueryInstanceToMetric().get(2), 0.1D);
        Assert.assertEquals(0.68, relevanceVal.getMeanMetric(), 0.1D);
    }
}
