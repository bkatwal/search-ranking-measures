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

import java.util.List;

public class QueryResultsRating {

    // marked rating of each doc of a query
    private List<DocRating> inputDocRatings;

    // predefined list of relevant docs for a given query
    private List<DocRating> preDefinedRatings;

    // total relevant docs count for a given query, if null recall will be set to total relevant
    // docs
    // in inputDocRatings and preDefinedRatings
    private Long totalRelevantHits;

    // total hits that matched the query from service response
    private Long totalHits;

    public QueryResultsRating() {
    }

    public QueryResultsRating(
            List<DocRating> inputDocRatings,
            List<DocRating> preDefinedRatings,
            Long totalRelevantHits,
            Long totalHits) {
        this.inputDocRatings = inputDocRatings;
        this.preDefinedRatings = preDefinedRatings;
        this.totalRelevantHits = totalRelevantHits;
        this.totalHits = totalHits;
    }

    public List<DocRating> getInputDocRatings() {
        return inputDocRatings;
    }

    public List<DocRating> getPreDefinedRatings() {
        return preDefinedRatings;
    }

    public void setInputDocRatings(List<DocRating> inputDocRatings) {
        this.inputDocRatings = inputDocRatings;
    }

    public void setPreDefinedRatings(List<DocRating> preDefinedRatings) {
        this.preDefinedRatings = preDefinedRatings;
    }

    public Long getTotalRelevantHits() {

        if (this.totalRelevantHits != null) {
            return this.totalRelevantHits;
        }
        long totalRelevantHitsCalc = 0;
        if (inputDocRatings != null && !inputDocRatings.isEmpty()) {
            totalRelevantHitsCalc =
                    totalRelevantHitsCalc + inputDocRatings.stream().filter(DocRating::getRelevant).count();
        }
        if (preDefinedRatings != null && !preDefinedRatings.isEmpty()) {
            totalRelevantHitsCalc = totalRelevantHitsCalc + preDefinedRatings.size();
        }
        return totalRelevantHitsCalc;
    }

    public void setTotalRelevantHits(Long totalRelevantHits) {
        this.totalRelevantHits = totalRelevantHits;
    }

    public Long getTotalHits() {

        if (totalHits != null) {
            return totalHits;
        }
        long totalHitsCalc = 0;
        if (inputDocRatings != null && !inputDocRatings.isEmpty()) {
            totalHitsCalc = totalHitsCalc + inputDocRatings.size();
        }
        if (preDefinedRatings != null && !preDefinedRatings.isEmpty()) {
            totalHitsCalc = totalHitsCalc + preDefinedRatings.size();
        }
        return totalHitsCalc;
    }

    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }
}
