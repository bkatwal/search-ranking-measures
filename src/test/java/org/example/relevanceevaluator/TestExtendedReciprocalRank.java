package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.DocRatingBuilder;
import org.example.dto.QueryResultsRating;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestExtendedReciprocalRank {

    @Test
    public void testEvalQuery() {

        RelevanceEvaluator extendedReciprocalRank = new ExtendedReciprocalRank();
        List<DocRating> resultSetRatings = new ArrayList<>();

        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 1").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 2").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 3").relevant(false).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 4").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 5").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 6").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 7").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 8").relevant(true).build());
        resultSetRatings.add(DocRatingBuilder.aDocRating().docId("doc 9").relevant(true).build());

        QueryResultsRating queryResultsRating = new QueryResultsRating();
        queryResultsRating.setInputDocRatings(resultSetRatings);

        List<DocRating> topDocsRatings = new ArrayList<>();
        topDocsRatings.add(DocRatingBuilder.aDocRating().docId("doc 1").maxPosition(2).build());
        topDocsRatings.add(DocRatingBuilder.aDocRating().docId("doc 5").maxPosition(4).build());
        topDocsRatings.add(DocRatingBuilder.aDocRating().docId("doc 8").maxPosition(6).build());
        topDocsRatings.add(DocRatingBuilder.aDocRating().docId("doc 11").maxPosition(8).build());

        queryResultsRating.setPreDefinedRatings(topDocsRatings);

        double metric = extendedReciprocalRank.evalQuery(queryResultsRating);
        Assert.assertEquals(0.46, metric, 0.1D);
    }
}
