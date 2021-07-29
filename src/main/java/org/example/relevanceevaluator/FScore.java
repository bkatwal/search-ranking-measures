package org.example.relevanceevaluator;


import org.example.dto.QueryResultsRating;
import org.example.exceptions.RelevanceEvaluatorException;

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
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {

        double precision = precisionEvaluator.eval(queryResultsRating);
        double recall = recallEvaluator.eval(queryResultsRating);

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
