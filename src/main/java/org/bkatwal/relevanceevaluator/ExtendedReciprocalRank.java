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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bkatwal.dto.DocRating;
import org.bkatwal.dto.QueryResultsRating;
import org.bkatwal.exceptions.RelevanceEvaluatorException;
import org.bkatwal.util.CollectionUtils;

public class ExtendedReciprocalRank extends RelevanceEvaluator {

    protected ExtendedReciprocalRank(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.EXTENDED_RECIPROCAL_RANKING);
    }

    protected ExtendedReciprocalRank() {
        super(RelevanceEvaluatorType.EXTENDED_RECIPROCAL_RANKING);
    }

    @Override
    protected double eval(QueryResultsRating queryResultsRating)
        throws RelevanceEvaluatorException {
        List<DocRating> queryResultsDocRating = queryResultsRating.getQueryResultsDocRating();

        if (CollectionUtils.isEmpty(queryResultsDocRating)) {
            throw new RelevanceEvaluatorException(
                "Extended Reciprocal Rank: Query results ratings can not be empty");
        }

        if (CollectionUtils.isEmpty(queryResultsRating.getKnownRelevantDocsRating())) {
            throw new RelevanceEvaluatorException(
                "Reciprocal Rank: missing known relevant docs list");
        }
        Map<String, Integer> docIdToPositionMap = getPositionByDocIDMap(queryResultsDocRating);

        List<DocRating> knownRelevantDocsRating = queryResultsRating.getKnownRelevantDocsRating();
        double extendedRR = 0.0d;
        for (int i = 0; i < knownRelevantDocsRating.size(); i++) {
            String docId = knownRelevantDocsRating.get(i).getDocId();

            if (docIdToPositionMap.containsKey(docId)) {
                int foundAt = docIdToPositionMap.get(docId);
                int maxPosition =
                    knownRelevantDocsRating.get(i).getMaxPosition() != null
                        ? knownRelevantDocsRating.get(i).getMaxPosition()
                        : i + 1;

                if (foundAt > maxPosition) {
                    extendedRR = extendedRR + (double) 1 / (foundAt - maxPosition + 1);
                } else {
                    extendedRR = extendedRR + 1.0D;
                }
            } else {
                extendedRR = extendedRR + 0.0d;
            }
        }

        return extendedRR / knownRelevantDocsRating.size();
    }

    private Map<String, Integer> getPositionByDocIDMap(List<DocRating> queryResultsDocRating) {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < queryResultsDocRating.size(); i++) {
            String docId = queryResultsDocRating.get(i).getDocId();
            map.put(docId, i + 1);
        }
        return map;
    }
}
