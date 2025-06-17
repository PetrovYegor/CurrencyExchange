package com.github.petrovyegor.currencyexchange.exception;

public class InvalidRequestException extends RuntimeException {
    private int code;
    private String message;

    public InvalidRequestException(int code, String message) {
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