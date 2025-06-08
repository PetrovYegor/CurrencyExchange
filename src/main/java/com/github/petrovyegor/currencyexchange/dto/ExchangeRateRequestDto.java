package com.github.petrovyegor.currencyexchange.dto;

public class ExchangeRateRequestDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private double rate;

    public ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public double getRate() {
        return rate;
    }
}
