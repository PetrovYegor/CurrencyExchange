package com.github.petrovyegor.currencyexchange.exception;

public class RestErrorException extends RuntimeException {
    private int code;
    private String message;

    public RestErrorException(int code, String message) {
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