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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueryResultsRating {

    // marked rating of each doc of a query
    private List<DocRating> queryResultsDocRating;

    // known relevant list of relevant docs for a given query
    private List<DocRating> knownRelevantDocsRating;

    public QueryResultsRating() {
    }

    public QueryResultsRating(
        List<DocRating> queryResultsDocRating,
        List<DocRating> knownRelevantDocsRating) {
        this.queryResultsDocRating = queryResultsDocRating;
        this.knownRelevantDocsRating = knownRelevantDocsRating;
    }

    public List<DocRating> getQueryResultsDocRating() {
        return queryResultsDocRating;
    }

    public List<DocRating> getKnownRelevantDocsRating() {
        return knownRelevantDocsRating;
    }

    public void setQueryResultsDocRating(List<DocRating> queryResultsDocRating) {
        this.queryResultsDocRating = queryResultsDocRating;
    }

    public void setKnownRelevantDocsRating(List<DocRating> knownRelevantDocsRating) {
        this.knownRelevantDocsRating = knownRelevantDocsRating;
    }

    public int truePositiveInSize(int size) {
        int totalRelevant = 0;

        for (int i = 0; i < size; i++) {
            if (queryResultsDocRating.get(i).getRelevant()) {
                totalRelevant++;
            }
        }
        return totalRelevant;
    }

    public int falseNegative() {

        if (knownRelevantDocsRating == null) {
            knownRelevantDocsRating = Collections.emptyList();
        }
        Map<String, DocRating> queryResultsDocRatingMap =
            queryResultsDocRating.stream()
                .collect(Collectors.toMap(DocRating::getDocId, Function.identity()));

        int count = 0;
        for (DocRating docRating : knownRelevantDocsRating) {
            if (!queryResultsDocRatingMap.containsKey(docRating.getDocId()) && docRating
                .getRelevant()) {
                count++;
            }
        }
        return count;
    }
}
