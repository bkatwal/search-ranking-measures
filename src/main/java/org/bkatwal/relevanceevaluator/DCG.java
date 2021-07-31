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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bkatwal.dto.DocRating;
import org.bkatwal.dto.QueryResultsRating;
import org.bkatwal.exceptions.RelevanceEvaluatorException;
import org.bkatwal.util.CalculateUtil;
import org.bkatwal.util.CollectionUtils;

public class DCG extends RelevanceEvaluator {

    protected DCG(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.DCG);
    }

    protected DCG() {
        super(RelevanceEvaluatorType.DCG);
    }

    @Override
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {

        if (CollectionUtils.isEmpty(queryResultsRating.getQueryResultsDocRating())) {
            throw new RelevanceEvaluatorException("DCG: query results ratings can not be empty");
        }

        List<DocRating> queryResultsDocRating = queryResultsRating.getQueryResultsDocRating();
        if (probeSize == null) {
            probeSize = queryResultsDocRating.size();
        } else {
            probeSize = Math.min(probeSize, queryResultsDocRating.size());
        }

        if (probeSize < queryResultsDocRating.size()) {
            queryResultsDocRating = queryResultsDocRating.subList(0, probeSize);
        }

        double iDCG = idealDCG(queryResultsDocRating);

        if (iDCG == 0) {
            return 0.0d;
        }
        List<Double> queryResultsDocGrades = new ArrayList<>();
        for (int i = 0; i < probeSize; i++) {
            queryResultsDocGrades.add(queryResultsDocRating.get(i).getGrade());
        }

        double dcg = calculateDCG(queryResultsDocGrades);

        return dcg / iDCG;
    }

    private double idealDCG(List<DocRating> docRatingList) {

        List<Double> inputDocGrades = new ArrayList<>();
        for (int i = 0; i < probeSize; i++) {
            inputDocGrades.add(docRatingList.get(i).getGrade());
        }
        inputDocGrades.sort(Comparator.reverseOrder());

        return calculateDCG(inputDocGrades);
    }

    private double calculateDCG(List<Double> inputDocGrades) {
        double iDCG = 0.0d;

        for (int i = 1; i <= inputDocGrades.size(); i++) {
            double denominator = CalculateUtil.log2(i + 1);
            double numerator = Math.pow(2, inputDocGrades.get(i - 1)) - 1;

            iDCG = iDCG + numerator / denominator;
        }

        return iDCG;
    }
}
