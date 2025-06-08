package com.github.petrovyegor.currencyexchange.util;

public class RequestParametersValidator {
    private static final String CURRENCY_CODE_PATTERN = "[a-zA-Z]{3}";
    private static final String PAIR_CURRENCY_CODES_PATTERN = "[a-zA-Z]{6}";
    private static final String CURRENCY_NAME_PATTERN = "[a-zA-Z]{1,50}";
    private static final String CURRENCY_SIGN_PATTERN = "[a-zA-Z]{1,10}";
    private static final double MIN_RATE_VALUE = 1;
    private static final double MAX_RATE_VALUE = 1000;

    public static boolean isCodeValid(String code) {
        return code.matches(CURRENCY_CODE_PATTERN);
    }

    public static boolean isPairOfCodesValid(String pair) {
        return pair.matches(PAIR_CURRENCY_CODES_PATTERN);
    }

    public static boolean isNameValid(String name) {
        return name.matches(CURRENCY_NAME_PATTERN);
    }

    public static boolean isSignValid(String sign) {
        return sign.matches(CURRENCY_SIGN_PATTERN);
    }

    public static boolean isCurrencyGetParametersValid(String code, String name, String sign) {
        return isCodeValid(code) && isNameValid(name) && isSignValid(sign);
    }

    private static boolean isRateValid(double rate) {
        return rate >= MIN_RATE_VALUE && rate <= MAX_RATE_VALUE;
    }

    public static boolean isExchangeRatePostParametersValid(String sourceCode, String targetCode, double rate) {
        return isCodeValid(sourceCode) && isCodeValid(targetCode) && isRateValid(rate);
    }
}
