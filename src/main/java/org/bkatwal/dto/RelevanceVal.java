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


import org.bkatwal.relevanceevaluator.RelevanceEvaluatorType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class RelevanceVal implements Serializable {

    private final RelevanceEvaluatorType relevanceEvaluatorType;
    private double meanMetric;
    private Map<Integer, Double> queryInstanceToMetric;

    public RelevanceVal(RelevanceEvaluatorType relevanceEvaluatorType) {
        this.relevanceEvaluatorType = relevanceEvaluatorType;
        queryInstanceToMetric = new HashMap<>();
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

    public Map<Integer, Double> getQueryInstanceToMetric() {
        return queryInstanceToMetric;
    }

    public void setQueryInstanceToMetric(Map<Integer, Double> queryInstanceToMetric) {
        this.queryInstanceToMetric = queryInstanceToMetric;
    }
}
