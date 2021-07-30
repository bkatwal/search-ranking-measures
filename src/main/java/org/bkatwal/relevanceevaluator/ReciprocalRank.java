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
import org.bkatwal.dto.QueryRating;
import org.bkatwal.exceptions.RelevanceEvaluatorException;
import org.bkatwal.util.CollectionUtils;

import java.util.List;

public class ReciprocalRank extends RelevanceEvaluator {
    protected ReciprocalRank(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.RECIPROCAL_RANKING);
    }

    protected ReciprocalRank() {
        super(RelevanceEvaluatorType.RECIPROCAL_RANKING);
    }

    @Override
    protected double eval(QueryRating queryRating) throws RelevanceEvaluatorException {
        List<DocRating> inputDocRatings = queryRating.getQueryResultsDocRating();

        if (CollectionUtils.isEmpty(inputDocRatings)) {
            throw new RelevanceEvaluatorException(
                    "Reciprocal Rank: Input results ratings can not be empty");
        }

        if (CollectionUtils.isEmpty(queryRating.getKnownRelevantDocsRating())) {
            throw new RelevanceEvaluatorException("Reciprocal Rank: missing top relevant docs " +
                    "list");
        }
        int foundAt = -1;
        for (int rank = 0; rank < inputDocRatings.size(); rank++) {

            // if first relevant doc equals at any position, update foundAt
            if (inputDocRatings
                    .get(rank)
                    .getDocId()
                    .equals(queryRating.getKnownRelevantDocsRating().get(0).getDocId())) {
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
