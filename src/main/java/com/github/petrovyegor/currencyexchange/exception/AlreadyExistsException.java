package com.github.petrovyegor.currencyexchange.exception;

public final class AlreadyExistsException extends RuntimeException {
    private int code;
    private String message;

    public AlreadyExistsException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
