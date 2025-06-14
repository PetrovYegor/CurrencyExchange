package com.github.petrovyegor.currencyexchange.dto;

public class ExchangeRateRequestDto {
    private int id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private double rate;

    public ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        this.id = 0;
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public ExchangeRateRequestDto(int id, String baseCurrencyCode, String targetCurrencyCode, double rate) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
