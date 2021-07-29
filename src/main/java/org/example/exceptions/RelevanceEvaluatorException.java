package org.example.exceptions;

public class RelevanceEvaluatorException extends RuntimeException {

    public RelevanceEvaluatorException(String errorMessage) {
        super(errorMessage);
    }

    public RelevanceEvaluatorException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
