package com.github.petrovyegor.currencyexchange.dto;

public class ExchangeRateResponseDto {

    private int id;
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;
    private double rate;

    public ExchangeRateResponseDto(int id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public CurrencyResponseDto getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyResponseDto getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }
}
