package com.github.petrovyegor.currencyexchange.util;

public final class Validator {
    private Validator(){}

    public static <T> boolean isNull(T source){
        return source == null;
    }

    private static boolean isEmpty(String source){
        return source.trim().isEmpty();
    }

    private static boolean isNullOrEmpty(String source){
        return isNull(source) || isEmpty(source);
    }

    private static boolean isUpperCase(String source){
        return source.matches("[A-Z]+");
    }

    public static void main(String[] args) {
        System.out.println(isUpperCase("AAAA"));
    }

    //порядок валидации - не нулл, заглавный, длина нужная

    public static void validateParameters(String... parameters){
        if (parameters == null){
            throw new IllegalArgumentException("Parameters can not be null");
        }
        for (int i = 0; i < parameters.length; i++){
            if (isNullOrEmpty(parameters[i])){
                throw new IllegalArgumentException("Parameter #" + (i + 1) + " is null or empty");//заменить на форматированный вывод
            }
        }
    }
}
