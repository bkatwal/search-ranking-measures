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

public final class DocRatingBuilder {

    private String docId;
    private Double grade = 0.0d;
    private boolean relevant = false;
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

    public DocRatingBuilder relevant(boolean relevant) {
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
