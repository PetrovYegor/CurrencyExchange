package com.github.petrovyegor.currencyexchange.util;

public final class RequestPathValidator {
    private RequestPathValidator(){}

    public static boolean isPathNull(String path){
        return path == null;
    }
}
