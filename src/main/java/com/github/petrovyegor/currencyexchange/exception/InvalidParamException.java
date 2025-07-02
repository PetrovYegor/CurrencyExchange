package com.github.petrovyegor.currencyexchange.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class InvalidParamException extends RuntimeException {
    private int code;
    private String message;
}
