package org.example.relevanceevaluator;


import org.example.dto.DocRating;
import org.example.dto.QueryResultsRating;
import org.example.exceptions.RelevanceEvaluatorException;
import org.example.util.CollectionUtils;

import java.util.List;

public class AveragePrecision extends RelevanceEvaluator {

    protected AveragePrecision(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.AVERAGE_PRECISION);
    }

    protected AveragePrecision() {
        super(null, RelevanceEvaluatorType.AVERAGE_PRECISION);
    }

    @Override
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {

        if (queryResultsRating == null
                || CollectionUtils.isEmpty(queryResultsRating.getInputDocRatings())) {
            throw new RelevanceEvaluatorException(
                    "Average Precision: Input results ratings can not be empty");
        }

        List<DocRating> inputDocRatings = queryResultsRating.getInputDocRatings();
        if (probeSize == null) {
            probeSize = inputDocRatings.size();
        } else {
            probeSize = Math.min(probeSize, inputDocRatings.size());
        }

        double[] precisionAtRank = new double[probeSize];
        int totalRelevant = 0;
        for (int rank = 0; rank < probeSize; rank++) {
            if (inputDocRatings.get(rank).getRelevant()) {
                totalRelevant++;
                precisionAtRank[rank] = (double) totalRelevant / (rank + 1);
            }
        }
        return this.mean(precisionAtRank);
    }
}
