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


import java.util.List;
import org.bkatwal.dto.DocRating;
import org.bkatwal.dto.QueryResultsRating;
import org.bkatwal.exceptions.RelevanceEvaluatorException;
import org.bkatwal.util.CollectionUtils;

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
            || CollectionUtils.isEmpty(queryResultsRating.getQueryResultsDocRating())) {
            throw new RelevanceEvaluatorException(
                "Average Precision: query results ratings can not be empty");
        }

        List<DocRating> queryResultsDocRating = queryResultsRating.getQueryResultsDocRating();
        if (probeSize == null) {
            probeSize = queryResultsDocRating.size();
        } else {
            probeSize = Math.min(probeSize, queryResultsDocRating.size());
        }

        double[] precisionAtRank = new double[probeSize];
        int totalRelevant = 0;
        for (int rank = 0; rank < probeSize; rank++) {
            if (queryResultsDocRating.get(rank).getRelevant()) {
                totalRelevant++;
                precisionAtRank[rank] = (double) totalRelevant / (rank + 1);
            }
        }
        return this.mean(precisionAtRank);
    }
}
