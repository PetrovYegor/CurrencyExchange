package com.github.petrovyegor.currencyexchange.dto;

public class CurrencyRequestDto {
    private String code;
    private String fullName;
    private String sign;

    public CurrencyRequestDto(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }
}
