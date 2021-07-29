package org.example.dto;


import org.example.relevanceevaluator.RelevanceEvaluatorType;

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
