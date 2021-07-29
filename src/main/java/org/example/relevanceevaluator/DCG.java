package org.example.relevanceevaluator;


import org.example.dto.DocRating;
import org.example.dto.QueryResultsRating;
import org.example.exceptions.RelevanceEvaluatorException;
import org.example.util.CalculateUtil;
import org.example.util.CollectionUtils;

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
    protected double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {

        if (CollectionUtils.isEmpty(queryResultsRating.getInputDocRatings())) {
            throw new RelevanceEvaluatorException("DCG: Input results ratings can not be empty");
        }

        List<DocRating> inputDocRatings = queryResultsRating.getInputDocRatings();
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
