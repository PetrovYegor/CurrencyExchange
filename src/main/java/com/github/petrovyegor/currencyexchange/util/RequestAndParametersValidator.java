package com.github.petrovyegor.currencyexchange.util;

import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class RequestAndParametersValidator {
    private static final String CURRENCY_CODE_PATTERN = "[a-zA-Z]{3}";
    private static final String PAIR_CURRENCY_CODES_PATTERN = "[a-zA-Z]{6}";
    private static final String CURRENCY_NAME_PATTERN = "[a-zA-Z ]{1,50}";
    private static final String CURRENCY_SIGN_PATTERN = "[a-zA-Z ]{1,10}";
    private static final BigDecimal MIN_RATE_VALUE = new BigDecimal(0);
    private static final BigDecimal MAX_RATE_VALUE = new BigDecimal(1000);
    private static final BigDecimal MIN_AMOUNT_VALUE = new BigDecimal(0);

    private static String INVALID_CURRENCY_CODE_MESSAGE = "The currency code cannot be empty, must contain 3 Latin letters";
    private static String INVALID_CURRENCY_NAME_MESSAGE = "The currency name cannot be empty, must contain only Latin letters and spaces, length from 1 to 50 characters";
    private static String INVALID_CURRENCY_SIGN_MESSAGE = "The currency sign cannot be empty, must contain only Latin letters and spaces, length from 1 to 10 characters";
    private static String INVALID_CURRENCY_POST_REQUEST_MESSAGE = "Currency code, name or sign are missing";


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

    public static void validateCurrenciesPostParameters(String code, String name, String sign){
        if (!isCodeValid(code)){
            throw new InvalidParamException(SC_BAD_REQUEST, INVALID_CURRENCY_CODE_MESSAGE);
        }
        if (!isNameValid(name)){
            throw new InvalidParamException(SC_BAD_REQUEST, INVALID_CURRENCY_NAME_MESSAGE);
        }
        if (!isSignValid(sign)){
            throw new InvalidParamException(SC_BAD_REQUEST, INVALID_CURRENCY_SIGN_MESSAGE);
        }
    }

    public static void validateCurrenciesPostRequest(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("code", "name", "sign");
        boolean isValid = parameters.keySet().containsAll(requiredParameters);
        if (!isValid){
            throw new InvalidRequestException(SC_BAD_REQUEST, INVALID_CURRENCY_POST_REQUEST_MESSAGE);
        }
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
