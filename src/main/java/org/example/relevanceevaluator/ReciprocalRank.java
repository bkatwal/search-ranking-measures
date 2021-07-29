package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.QueryResultsRating;
import org.example.exceptions.RelevanceEvaluatorException;
import org.example.util.CollectionUtils;

import java.util.List;

public class ReciprocalRank extends RelevanceEvaluator {
    protected ReciprocalRank(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.RECIPROCAL_RANKING);
    }

    protected ReciprocalRank() {
        super(RelevanceEvaluatorType.RECIPROCAL_RANKING);
    }

    @Override
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {
        List<DocRating> inputDocRatings = queryResultsRating.getInputDocRatings();

        if (CollectionUtils.isEmpty(inputDocRatings)) {
            throw new RelevanceEvaluatorException(
                    "Reciprocal Rank: Input results ratings can not be empty");
        }

        if (CollectionUtils.isEmpty(queryResultsRating.getPreDefinedRatings())) {
            throw new RelevanceEvaluatorException("Reciprocal Rank: missing top relevant docs " +
                    "list");
        }
        int foundAt = -1;
        for (int rank = 0; rank < inputDocRatings.size(); rank++) {

            // if first relevant doc equals at any position, update foundAt
            if (inputDocRatings
                    .get(rank)
                    .getDocId()
                    .equals(queryResultsRating.getPreDefinedRatings().get(0).getDocId())) {
                foundAt = rank + 1;
                break;
            }
        }
        if (foundAt == -1) {
            return 0.0d;
        }
        return (double) 1 / foundAt;
    }
}
