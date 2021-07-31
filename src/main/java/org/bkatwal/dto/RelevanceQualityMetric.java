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
package org.bkatwal.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bkatwal.relevanceevaluator.RelevanceEvaluatorType;

public class RelevanceQualityMetric implements Serializable {

    private final RelevanceEvaluatorType relevanceEvaluatorType;
    private double meanMetric;
    private Map<Integer, Double> qualityMetricByInstanceId;

    private Map<Integer, List<String>> assertionFailedDocsByInstanceId;

    public RelevanceQualityMetric(RelevanceEvaluatorType relevanceEvaluatorType) {
        this.relevanceEvaluatorType = relevanceEvaluatorType;
        qualityMetricByInstanceId = new HashMap<>();
        assertionFailedDocsByInstanceId = new HashMap<>();
    }

    public Map<Integer, List<String>> getAssertionFailedDocsByInstanceId() {
        return assertionFailedDocsByInstanceId;
    }

    public void setAssertionFailedDocsByInstanceId(
        Map<Integer, List<String>> assertionFailedDocsByInstanceId) {
        this.assertionFailedDocsByInstanceId = assertionFailedDocsByInstanceId;
    }

    public RelevanceEvaluatorType getScorerEnum() {
        return relevanceEvaluatorType;
    }

    public RelevanceEvaluatorType getRelevanceEvaluatorType() {
        return relevanceEvaluatorType;
    }

    public double getMeanMetric() {
        return meanMetric;
    }

    public void setMeanMetric(double meanMetric) {
        this.meanMetric = meanMetric;
    }

    public Map<Integer, Double> getQualityMetricByInstanceId() {
        return qualityMetricByInstanceId;
    }

    public void setQualityMetricByInstanceId(Map<Integer, Double> qualityMetricByInstanceId) {
        this.qualityMetricByInstanceId = qualityMetricByInstanceId;
    }
}
