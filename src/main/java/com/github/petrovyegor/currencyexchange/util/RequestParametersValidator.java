package com.github.petrovyegor.currencyexchange.util;

public class RequestParametersValidator {
    private static final String CURRENCY_CODE_PATTERN = "[a-zA-Z]{3}";
    private static final String CURRENCY_NAME_PATTERN = "[a-zA-Z]{1,50}";
    private static final String CURRENCY_SIGN_PATTERN = "[a-zA-Z]{1,10}";

    public static boolean isCodeValid(String code) {
        return code.matches(CURRENCY_CODE_PATTERN);
    }

    public static boolean isNameValid(String name){
        return  name.matches(CURRENCY_NAME_PATTERN);
    }

    public static boolean isSignValid(String sign){
        return sign.matches(CURRENCY_SIGN_PATTERN);
    }

    public static boolean isCurrencyGetParametersValid(String code, String name, String sign){
        return isCodeValid(code) && isNameValid(name) && isSignValid(sign);
    }
}
