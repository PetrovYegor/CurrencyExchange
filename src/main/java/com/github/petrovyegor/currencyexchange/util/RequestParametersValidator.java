package com.github.petrovyegor.currencyexchange.util;

import java.math.BigDecimal;

public class RequestParametersValidator {
    private static final String CURRENCY_CODE_PATTERN = "[a-zA-Z]{3}";
    private static final String PAIR_CURRENCY_CODES_PATTERN = "[a-zA-Z]{6}";
    private static final String CURRENCY_NAME_PATTERN = "[a-zA-Z]{1,50}";
    private static final String CURRENCY_SIGN_PATTERN = "[a-zA-Z]{1,10}";
    private static final BigDecimal MIN_RATE_VALUE = new BigDecimal(0);
    private static final BigDecimal MAX_RATE_VALUE = new BigDecimal(1000);
    private static final BigDecimal MIN_AMOUNT_VALUE = new BigDecimal(0);

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

    public static boolean isRateValid(BigDecimal rate) {
        return rate.compareTo(MIN_RATE_VALUE) > 0  && rate.compareTo(MAX_RATE_VALUE) < 0;
    }


    public static boolean isExchangeRatePostParametersValid(String sourceCode, String targetCode, BigDecimal rate) {
        return isCodeValid(sourceCode) && isCodeValid(targetCode) && isRateValid(rate);
    }

    public static boolean isExchangeGetParametersValid(String sourceCode, String targetCode, BigDecimal amount) {
        return isCodeValid(sourceCode) && isCodeValid(targetCode) && isAmountValid(amount);
    }

    private static boolean isAmountValid(BigDecimal amount) {
        return amount.compareTo(MIN_AMOUNT_VALUE) > 0;
    }
}
