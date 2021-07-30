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
import org.bkatwal.util.CalculateUtil;
import org.bkatwal.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DCG extends RelevanceEvaluator {

    protected DCG(Integer probeSize) {
        super(probeSize, RelevanceEvaluatorType.DCG);
    }

    protected DCG() {
        super(RelevanceEvaluatorType.DCG);
    }

    @Override
    protected double eval(QueryRating queryRating) throws RelevanceEvaluatorException {

        if (CollectionUtils.isEmpty(queryRating.getQueryResultsDocRating())) {
            throw new RelevanceEvaluatorException("DCG: Input results ratings can not be empty");
        }

        List<DocRating> inputDocRatings = queryRating.getQueryResultsDocRating();
        if (probeSize == null) {
            probeSize = inputDocRatings.size();
        } else {
            probeSize = Math.min(probeSize, inputDocRatings.size());
        }
        double iDCG = idealDCG(inputDocRatings);

        if (iDCG == 0) {
            return 0.0d;
        }
        List<Double> inputDocGrades = new ArrayList<>();
        for (int i = 0; i < probeSize; i++) {
            inputDocGrades.add(inputDocRatings.get(i).getGrade());
        }

        double dcg = calculateDCG(inputDocGrades);

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

        for (int i = 0; i < inputDocGrades.size(); i++) {
            double denominator = CalculateUtil.log2(i + 1);
            double numerator = Math.pow(2, inputDocGrades.get(i)) - 1;

            iDCG = iDCG + numerator / denominator;
        }

        return iDCG;
    }
}
