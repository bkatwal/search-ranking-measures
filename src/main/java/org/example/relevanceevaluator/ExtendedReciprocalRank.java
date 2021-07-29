package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.QueryResultsRating;
import org.example.exceptions.RelevanceEvaluatorException;
import org.example.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedReciprocalRank extends RelevanceEvaluator {
    protected ExtendedReciprocalRank(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.RECIPROCAL_RANKING);
    }

    protected ExtendedReciprocalRank() {
        super(RelevanceEvaluatorType.RECIPROCAL_RANKING);
    }

    @Override
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {
        List<DocRating> inputDocRatings = queryResultsRating.getInputDocRatings();

        if (CollectionUtils.isEmpty(inputDocRatings)) {
            throw new RelevanceEvaluatorException(
                    "Extended Reciprocal Rank: Input results ratings can not be empty");
        }

        if (CollectionUtils.isEmpty(queryResultsRating.getPreDefinedRatings())) {
            throw new RelevanceEvaluatorException(
                    "Reciprocal Rank: missing predefined relevant docs list");
        }
        Map<String, Integer> docIdToPositionMap = getPositionByDocIDMap(inputDocRatings);

        List<DocRating> preDefinedDocRatings = queryResultsRating.getPreDefinedRatings();
        double err = 0.0d;
        for (int i = 0; i < preDefinedDocRatings.size(); i++) {

            int foundAt = -1;
            String docId = preDefinedDocRatings.get(i).getDocId();

            if (docIdToPositionMap.containsKey(docId)) {
                foundAt = docIdToPositionMap.get(docId);
            }
            if (foundAt == -1) {
                err = err + 0.0d;
            } else {
                int maxPosition =
                        preDefinedDocRatings.get(i).getMaxPosition() != null
                                ? preDefinedDocRatings.get(i).getMaxPosition()
                                : i + 1;

                if (foundAt > maxPosition) {
                    err = err + (double) 1 / (foundAt - maxPosition + 1);
                } else {
                    err = err + 1.0D;
                }
            }
        }

        return err / preDefinedDocRatings.size();
    }

    private Map<String, Integer> getPositionByDocIDMap(List<DocRating> inputDocRatings) {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < inputDocRatings.size(); i++) {
            String docId = inputDocRatings.get(i).getDocId();
            map.put(docId, i + 1);
        }
        return map;
    }
}
