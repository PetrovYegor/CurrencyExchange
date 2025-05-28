package com.github.petrovyegor.currencyexchange.exception;

public final class WrongUrlException extends RuntimeException {
    public WrongUrlException(String message) {
        super(message);
    }
}
