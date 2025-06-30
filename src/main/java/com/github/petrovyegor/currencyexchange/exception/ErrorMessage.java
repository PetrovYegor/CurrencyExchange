package com.github.petrovyegor.currencyexchange.exception;

public class ErrorMessage {
    public static final String INVALID_CURRENCY_CODE = "The currency code cannot be empty, must contain 3 Latin letters";
    public static final String INVALID_CURRENCY_NAME = "The currency name cannot be empty, must contain only Latin letters and spaces, length from 1 to 50 characters";
    public static final String INVALID_CURRENCY_SIGN = "The currency sign cannot be empty, must contain only Latin letters and spaces, length from 1 to 10 characters";
    public static final String INVALID_RATE = "The exchange rate should be from 1 to 1000";
    public static final String INVALID_AMOUNT = "The amount cannot be empty, must contain only digits. The amount should be from 1 to 100000";
    public static final String INVALID_PAIR_OF_CURRENCY_CODES = "The pair of currency codes cannot be empty, must contain 6 Latin letters";
    public static final String INVALID_CURRENCY_POST_REQUEST = "Currency code, name or sign are missing";
    public static final String INVALID_EXCHANGE_RATE_POST_REQUEST = "Base currency code, target currency code or exchange rate are missing";
    public static final String INVALID_EXCHANGE_RATE_PATCH_REQUEST = "There is no rate parameter in patch request body";
    public static final String INVALID_EXCHANGE_GET_REQUEST = "Base currency code, target currency code or amount are missing";
    public static final String UNSOPPORTED_URL = "Unsupported URL given. Missing slash and currency code";
    public static final String UNSUPPORTED_CONVERSION_OPERATION = "There is no direct, opposite or cross exchange rate for currency codes '%s' and '%s'";
    public static final String EXCHANGE_RATE_DOES_NOT_EXIST = "Exchange rate with base currency code '%s' and target currency code '%s' does not exist!";
    public static final String CURRENCY_NOT_FOUND = "Currency with code '%s' does not exist!";
    public static final String EXCHANGE_RATE_ALREADY_EXISTS = "Exchange rate with base currency code '%s' and target currency code '%s' already exists!";
    public static final String EQUAL_CODES = "Currency codes should not be equals";
    public static final String INVALID_RATE_FORMAT = "Rate must be a positive number with up to 6 decimal places";
    public static final String CURRENCY_ALREADY_EXISTS = "Currency with code '%s' already exists!";
    public static final String FAILED_TO_GET_CURRENCY_BY_CODE = "Failed to get currency by code '%s'";
    public static final String FAILED_TO_GET_ALL_CURRENCIES = "Failed to get all currencies";
    public static final String FAILED_TO_SAVE_CURRENCY = "Failed to save currency with code '%s', fullname '%s', sign '%s'";
    public static final String FAILED_TO_GET_CURRENCY_BY_ID = "Failed to get currency by id '%s'";
    public static final String CURRENCY_NOT_FOUND_BY_CODE = "Currency with code '%s' does not exist!";
    public static final String CURRENCY_NOT_FOUND_BY_ID = "Currency with id '%s' does not exist!";

    private ErrorMessage() {
    }
}
