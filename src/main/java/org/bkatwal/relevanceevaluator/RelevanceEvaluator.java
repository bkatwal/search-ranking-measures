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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bkatwal.dto.DocRating;
import org.bkatwal.dto.QueryResultsRating;
import org.bkatwal.dto.RelevanceQualityMetric;
import org.bkatwal.exceptions.RelevanceEvaluatorException;
import org.bkatwal.util.CollectionUtils;

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

    protected abstract double eval(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException;

    public double evalQuery(QueryResultsRating queryResultsRating) throws RelevanceEvaluatorException {
        updateDocRatingsUsingExpectedRating(
            queryResultsRating.getQueryResultsDocRating(), queryResultsRating.getKnownRelevantDocsRating());
        return eval(queryResultsRating);
    }

    public RelevanceQualityMetric evalAveraged(Map<Integer, QueryResultsRating> queryResultsRatingMap) {

        if (CollectionUtils.isEmpty(queryResultsRatingMap)) {
            throw new RelevanceEvaluatorException("No query ratings passed.");
        }
        RelevanceQualityMetric relevanceQualityMetric =
            new RelevanceQualityMetric(relevanceEvaluatorType);

        Map<Integer, Double> qualityMetricByInstanceId = new LinkedHashMap<>();

        for (Map.Entry<Integer, QueryResultsRating> instanceGradeEntry : queryResultsRatingMap
            .entrySet()) {
            Integer instanceId = instanceGradeEntry.getKey();
            QueryResultsRating queryResultsRating = instanceGradeEntry.getValue();

            double val = eval(queryResultsRating);
            qualityMetricByInstanceId.put(instanceId, val);
        }
        relevanceQualityMetric.setMeanMetric(mean((qualityMetricByInstanceId.values())));
        relevanceQualityMetric.setQualityMetricByInstanceId(qualityMetricByInstanceId);
        return relevanceQualityMetric;
    }

    protected double mean(final Collection<Double> relevanceMetrics) {
        return relevanceMetrics.stream().mapToDouble(Double::doubleValue).average()
            .orElse(Double.NaN);
    }

    protected double mean(final double[] relevanceMetrics) {
        return Arrays.stream(relevanceMetrics).average().orElse(Double.NaN);
    }

    /***
     * This method rates the result of a query based on pre defined set of relevant result
     * @param queryResultsDocRating List of rating of all docs from a query instance results.
     * @param knownRelevantDocsRating pre defined list of expected results and their ratings
     */
    protected void updateDocRatingsUsingExpectedRating(
        List<DocRating> queryResultsDocRating, List<DocRating> knownRelevantDocsRating) {

        if (CollectionUtils.isEmpty(knownRelevantDocsRating)) {
            return;
        }
        Map<String, DocRating> knownDocsRatingMap =
            knownRelevantDocsRating.stream()
                .collect(Collectors.toMap(DocRating::getDocId, Function.identity()));
        double grade = knownRelevantDocsRating.size();
        for (DocRating docRating : queryResultsDocRating) {

            DocRating knownDocRating = knownDocsRatingMap.get(docRating.getDocId());

            if (knownDocRating != null) {
                docRating.setRelevant(true);
                if (knownDocRating.getGrade() == null || knownDocRating.getGrade() == 0.0D) {
                    docRating.setGrade(grade);
                    grade = grade - 1.0d;
                }
            }
        }
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
