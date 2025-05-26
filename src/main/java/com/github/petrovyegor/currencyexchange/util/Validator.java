package com.github.petrovyegor.currencyexchange.util;

public final class Validator {
    private static final String CURRENCY_CODE_PATTERN = "[A-Z]{3}";
    private static final String CURRENCY_NAME_PATTERN = "[a-zA-Z]{1,50}";
    private static final String CURRENCY_SIGN_PATTERN = ".{1,10}";
    private Validator(){}

    public static <T> boolean isNull(T source){
        return source == null;
    }

    private static boolean isEmpty(String source){
        return source.trim().isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("1234567890d".matches(CURRENCY_SIGN_PATTERN));
    }

    private static boolean isNullOrEmpty(String source){
        return isNull(source) || isEmpty(source);
    }

    private static boolean isCodeValid(String code){
        return code.matches(CURRENCY_CODE_PATTERN);
    }

    public static void validateCode(String code){
        if (!isCodeValid(code)){
            throw new IllegalArgumentException("The currency code must contain three capital letters of the Latin alphabet");//кастомный эксепшн
        }
    }

    private static boolean isNameValid(String name){
        return name.matches(CURRENCY_NAME_PATTERN);
    }

    public static void validateName(String name){
        if (!isNameValid(name)){
            throw new IllegalArgumentException("Currency name must contain only Latin letters and not exceed 50 characters");//кастомный эксп сделать
        }
    }

    private static boolean isSignValid(String sign){
        return sign.matches(CURRENCY_SIGN_PATTERN);
    }

    public static void validateSign(String sign){
        if (!isSignValid(sign)){
            throw new IllegalArgumentException("The currency symbol must not exceed 10 characters");
        }
    }
    public static void validateCurrencyPostParameters(String code, String name, String sign){
        Validator.isNullOrEmptyParameters(code, name, sign);
        Validator.validateCode(code);
        Validator.validateName(name);
        Validator.validateSign(sign);
    }


    public static void isNullOrEmptyParameters(String... parameters){
        if (parameters == null){
            throw new IllegalArgumentException("Parameters can not be null");
        }
        for (int i = 0; i < parameters.length; i++){
            if (isNullOrEmpty(parameters[i])){
                throw new IllegalArgumentException("Parameter #" + (i + 1) + " is null or empty");//заменить на форматированный вывод
            }
        }
    }

    private static boolean isPathValid(String path){
        return !path.equals("/");
    }

    public static void validatePath(String path){
        if(!isPathValid(path)){
            throw new IllegalArgumentException("The path in the URL must not end with '/'");
        }
    }
}
