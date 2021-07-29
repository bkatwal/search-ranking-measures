package org.example.dto;

public final class DocRatingBuilder {
    private String docId;
    private Double grade;
    private Boolean relevant;
    private Integer maxPosition;

    private DocRatingBuilder() {
    }

    public static DocRatingBuilder aDocRating() {
        return new DocRatingBuilder();
    }

    public DocRatingBuilder docId(String docId) {
        this.docId = docId;
        return this;
    }

    public DocRatingBuilder grade(Double grade) {
        this.grade = grade;
        return this;
    }

    public DocRatingBuilder relevant(Boolean relevant) {
        this.relevant = relevant;
        return this;
    }

    public DocRatingBuilder maxPosition(Integer maxPosition) {
        this.maxPosition = maxPosition;
        return this;
    }

    public DocRating build() {
        DocRating docRating = new DocRating();
        docRating.setDocId(docId);
        docRating.setGrade(grade);
        docRating.setRelevant(relevant);
        docRating.setMaxPosition(maxPosition);
        return docRating;
    }
}
