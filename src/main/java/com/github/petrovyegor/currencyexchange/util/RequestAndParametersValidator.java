package com.github.petrovyegor.currencyexchange.util;

import com.github.petrovyegor.currencyexchange.exception.ErrorMessage;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class RequestAndParametersValidator {
    private static final String CURRENCY_CODE_PATTERN = "[a-zA-Z]{3}";
    private static final String PAIR_OF_CURRENCY_CODES_PATTERN = "[a-zA-Z]{6}";
    private static final String CURRENCY_NAME_PATTERN = "[a-zA-Z ]{1,50}";
    private static final String CURRENCY_SIGN_PATTERN = "[a-zA-Z ]{1,10}";
    private static final BigDecimal MIN_RATE_VALUE = new BigDecimal(0);
    private static final BigDecimal MAX_RATE_VALUE = new BigDecimal(1000);
    private static final BigDecimal MIN_AMOUNT_VALUE = new BigDecimal(0);
    private static final BigDecimal MAX_AMOUNT_VALUE = new BigDecimal(100000);

    public static void validateCurrenciesPostParameters(String code, String name, String sign) {
        validateCode(code);
        validateName(name);
        validateSign(sign);
    }

    public static void validateExchangeRatesPostParameters(String baseCode, String targetCode, BigDecimal rate) {
        validateCode(baseCode);
        validateCode(targetCode);
        validateRate(rate);
    }

    public static void validateExchangeRatesPatchParameters(String pairOfCodes, BigDecimal rate) {
        validatePairOfCodes(pairOfCodes);
        validateRate(rate);
    }

    public static void validateExchageGetParameters(String baseCode, String targetCode, BigDecimal amount) {
        validateCode(baseCode);
        validateCode(targetCode);
        validateAmount(amount);
    }

    public static void validateCurrenciesPostRequest(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("code", "name", "sign");
        boolean isValid = parameters.keySet().containsAll(requiredParameters);
        if (!isValid) {
            throw new InvalidRequestException(SC_BAD_REQUEST, ErrorMessage.INVALID_CURRENCY_POST_REQUEST);
        }
    }

    public static void validateExchangeRatesPostRequest(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("baseCurrencyCode", "targetCurrencyCode", "rate");
        boolean isValid = parameters.keySet().containsAll(requiredParameters);
        if (!isValid) {
            throw new InvalidRequestException(SC_BAD_REQUEST, ErrorMessage.INVALID_EXCHANGE_RATE_POST_REQUEST);
        }
    }

    public static void validateExchangeGetRequest(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("from", "to", "amount");
        boolean isValid = parameters.keySet().containsAll(requiredParameters);
        if (!isValid) {
            throw new InvalidRequestException(SC_BAD_REQUEST, ErrorMessage.INVALID_EXCHANGE_GET_REQUEST);
        }
    }

    public static void validateCode(String code) {
        if (!isCodeValid(code)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_CURRENCY_CODE);
        }
    }

    public static void validateName(String name) {
        if (!isNameValid(name)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_CURRENCY_NAME);
        }
    }

    public static void validateExchangeRatePatchRequestBody(String body) {
        if (!isPatchRequestBodyValid(body)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, ErrorMessage.INVALID_EXCHANGE_RATE_PATCH_REQUEST);
        }
    }

    public static void validateRate(BigDecimal rate) {
        if (!isRateValid(rate)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_RATE_FORMAT);
        }
    }

    public static void validateAmount(BigDecimal amount) {
        if (!isAmountValid(amount)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_AMOUNT);
        }
    }

    public static void validateSign(String sign) {
        if (!isSignValid(sign)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_CURRENCY_SIGN);
        }
    }

    public static void validatePath(String path) {
        if (path == null) {
            throw new RestErrorException(SC_BAD_REQUEST, ErrorMessage.UNSOPPORTED_URL);
        }
    }

    public static void validatePairOfCodes(String pair) {
        if (!isPairOfCodesValid(pair)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_PAIR_OF_CURRENCY_CODES);
        }
    }

    private static boolean isPairOfCodesValid(String pair) {
        return pair.matches(PAIR_OF_CURRENCY_CODES_PATTERN);
    }

    public static boolean isNameValid(String name) {
        return name.matches(CURRENCY_NAME_PATTERN);
    }

    public static boolean isSignValid(String sign) {
        return sign.matches(CURRENCY_SIGN_PATTERN);
    }

    public static boolean isRateValid(BigDecimal rate) {
        return rate.compareTo(MIN_RATE_VALUE) > 0 && rate.compareTo(MAX_RATE_VALUE) < 0;
    }

    private static boolean isAmountValid(BigDecimal amount) {
        return amount.compareTo(MIN_AMOUNT_VALUE) > 0 && amount.compareTo(MAX_AMOUNT_VALUE) < 0;
    }

    private static boolean isPatchRequestBodyValid(String body) {
        if (body.isEmpty() || body.length() < 5) {
            return false;
        }
        return body.substring(0, 5).equals("rate=");
    }

    private static boolean isCodeValid(String code) {
        return code.matches(CURRENCY_CODE_PATTERN);
    }
}
