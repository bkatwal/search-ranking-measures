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
import org.bkatwal.dto.QueryResultsRating;
import org.bkatwal.dto.RelevanceVal;
import org.bkatwal.exceptions.RelevanceEvaluatorException;
import org.bkatwal.util.CollectionUtils;

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
