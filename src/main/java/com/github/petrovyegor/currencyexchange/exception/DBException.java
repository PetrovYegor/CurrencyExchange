package com.github.petrovyegor.currencyexchange.exception;

public final class DBException extends RuntimeException {
    private static final String FAILED_QUERY_MESSAGE = "Failed to execute query '%s'. Something went wrong";
    public DBException(String message) {
        super(FAILED_QUERY_MESSAGE.formatted(message));
    }
}
