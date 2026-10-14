package com.arstudio.exception;

/**
 * Exception thrown when database CRUD operations encounter errors.
 */
public class DatabaseOperationException extends ARCustomException {
    public DatabaseOperationException(String message) {
        super("ERR_DB_OP", message);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super("ERR_DB_OP", message, cause);
    }
}
