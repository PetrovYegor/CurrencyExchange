package com.github.petrovyegor.currencyexchange.exception;

public final class InvalidParamException extends RuntimeException {
    private int code;
    private String message;

    public InvalidParamException(int code, String message) {
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
