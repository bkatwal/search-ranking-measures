package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.QueryResultsRating;
import org.example.exceptions.RelevanceEvaluatorException;
import org.example.util.CollectionUtils;

import java.util.List;

public class Precision extends RelevanceEvaluator {

    public Precision(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.PRECISION);
    }

    public Precision() {
        super(RelevanceEvaluatorType.PRECISION);
    }

    @Override
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {

        if (queryResultsRating == null
                || CollectionUtils.isEmpty(queryResultsRating.getInputDocRatings())) {
            throw new RelevanceEvaluatorException("Precision: Input results ratings can not be " +
                    "empty");
        }

        List<DocRating> inputDocRatings = queryResultsRating.getInputDocRatings();
        if (probeSize == null) {
            probeSize = inputDocRatings.size();
        } else {
            probeSize = Math.min(probeSize, inputDocRatings.size());
        }

        int totalRelevant = totalRelevantDocsInProbeSize(inputDocRatings);

        return (double) totalRelevant / probeSize;
    }
}
