package org.example.relevanceevaluator;

import org.example.dto.DocRating;
import org.example.dto.QueryResultsRating;
import org.example.dto.RelevanceVal;
import org.example.exceptions.RelevanceEvaluatorException;
import org.example.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class RelevanceEvaluator {

    protected Integer probeSize;
    protected final RelevanceEvaluatorType relevanceEvaluatorType;

    protected RelevanceEvaluator(Integer probeSize, RelevanceEvaluatorType relevanceEvaluatorType) {
        isValidProbeSize(probeSize);
        this.relevanceEvaluatorType = relevanceEvaluatorType;
        this.probeSize = probeSize;
    }

    protected RelevanceEvaluator(RelevanceEvaluatorType relevanceEvaluatorType) {
        this.relevanceEvaluatorType = relevanceEvaluatorType;
    }

    protected abstract double eval(QueryResultsRating queryResultsRating)
            throws RelevanceEvaluatorException;

    public double evalQuery(QueryResultsRating queryResultsRating)
            throws RelevanceEvaluatorException {
        updateDocRatingsUsingExpectedRating(
                queryResultsRating.getInputDocRatings(), queryResultsRating.getPreDefinedRatings());
        return eval(queryResultsRating);
    }

    public RelevanceVal evalAveraged(Map<Integer, QueryResultsRating> queryResultsRatingMap) {

        if (CollectionUtils.isEmpty(queryResultsRatingMap)) {
            throw new RelevanceEvaluatorException("No query ratings passed.");
        }
        RelevanceVal relevanceVal = new RelevanceVal(relevanceEvaluatorType);

        Map<Integer, Double> queryInstanceToMetric = new LinkedHashMap<>();

        for (Map.Entry<Integer, QueryResultsRating> instanceGradeEntry :
                queryResultsRatingMap.entrySet()) {
            Integer instanceId = instanceGradeEntry.getKey();
            QueryResultsRating queryResultsRating = instanceGradeEntry.getValue();

            double val = eval(queryResultsRating);
            queryInstanceToMetric.put(instanceId, val);
        }
        relevanceVal.setMeanMetric(mean((queryInstanceToMetric.values())));
        relevanceVal.setQueryInstanceToMetric(queryInstanceToMetric);
        return relevanceVal;
    }

    protected double mean(final Collection<Double> relevanceMetrics) {
        return relevanceMetrics.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    protected double mean(final double[] relevanceMetrics) {
        return Arrays.stream(relevanceMetrics).average().orElse(Double.NaN);
    }

    /***
     * This method rates the result of a query based on pre defined set of relevant result
     * @param inputDocRatings List of rating of all docs from a query instance results.
     * @param preDefinedDocRatings pre defined list of expected results and their ratings
     */
    protected void updateDocRatingsUsingExpectedRating(
            List<DocRating> inputDocRatings, List<DocRating> preDefinedDocRatings) {

        if (CollectionUtils.isEmpty(preDefinedDocRatings)) {
            return;
        }
        Map<String, DocRating> expectedRatingsMap =
                preDefinedDocRatings.stream()
                        .collect(Collectors.toMap(DocRating::getDocId, Function.identity()));
        double grade = preDefinedDocRatings.size();
        for (DocRating docRating : inputDocRatings) {

            DocRating expectedDocRating = expectedRatingsMap.get(docRating.getDocId());

            if (expectedDocRating != null) {
                docRating.setRelevant(true);
                if (expectedDocRating.getGrade() == null || expectedDocRating.getGrade() == 0.0D) {
                    docRating.setGrade(grade);
                    grade = grade - 1.0d;
                }
            }
        }
    }

    protected int totalRelevantDocsInProbeSize(List<DocRating> inputDocRatings) {
        int totalRelevant = 0;

        for (int i = 0; i < probeSize; i++) {
            if (inputDocRatings.get(i).getRelevant()) {
                totalRelevant++;
            }
        }
        return totalRelevant;
    }

    private void isValidProbeSize(Integer probeSize) {
        if (probeSize == null) {
            return;
        }
        if (probeSize <= 0) {
            throw new RelevanceEvaluatorException("probe size must be greater than 0.");
        }
    }
}
