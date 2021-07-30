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


import org.bkatwal.dto.QueryRating;
import org.bkatwal.exceptions.RelevanceEvaluatorException;

public class FScore extends RelevanceEvaluator {

    private final Integer beta;
    private final RelevanceEvaluator precisionEvaluator;
    private final RelevanceEvaluator recallEvaluator;

    public FScore(Integer probeSize, int beta) {
        super(probeSize, RelevanceEvaluatorType.F_SCORE);
        isValidBeta(beta);
        this.beta = beta;
        this.precisionEvaluator = new Precision(probeSize);
        this.recallEvaluator = new Recall(probeSize);
    }

    public FScore(int beta) {
        super(RelevanceEvaluatorType.F_SCORE);
        isValidBeta(beta);
        this.beta = beta;
        this.precisionEvaluator = new Precision(probeSize);
        this.recallEvaluator = new Recall(probeSize);
    }

    public FScore() {
        super(RelevanceEvaluatorType.F_SCORE);
        this.beta = 1;
        this.precisionEvaluator = new Precision(probeSize);
        this.recallEvaluator = new Recall(probeSize);
    }

    @Override
    protected double eval(QueryRating queryRating) throws RelevanceEvaluatorException {

        double precision = precisionEvaluator.eval(queryRating);
        double recall = recallEvaluator.eval(queryRating);

        if (precision == 0.0d || recall == 0.0d) {
            return 0.0d;
        }
        double betSquare = Math.pow(beta, 2);
        double denominator = (betSquare * precision) + recall;
        double numerator = (betSquare + 1) * precision * recall;

        return numerator / denominator;
    }

    private void isValidBeta(int beta) {
        if (beta < 0) {
            throw new RelevanceEvaluatorException("F_SCORE: Beta value can not be negative.");
        }
    }
}
