package org.example.dto;

import java.io.Serializable;


public class DocRating implements Serializable {
    private String docId;

    private Double grade = 0.0D;

    private Boolean relevant = false;

    private Integer maxPosition;

    public String getDocId() {
        return docId;
    }

    public Double getGrade() {
        return grade;
    }

    public Boolean getRelevant() {
        return relevant;
    }

    public Integer getMaxPosition() {
        return maxPosition;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public void setRelevant(Boolean relevant) {
        this.relevant = relevant;
    }

    public void setMaxPosition(Integer maxPosition) {
        this.maxPosition = maxPosition;
    }
}
