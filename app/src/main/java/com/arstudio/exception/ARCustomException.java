package com.arstudio.exception;

/**
 * Base custom exception for AR Studio application.
 * Demonstrates Custom Exception Handling for Academic Review Rubric.
 */
public class ARCustomException extends Exception {
    private final String errorCode;

    public ARCustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ARCustomException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
