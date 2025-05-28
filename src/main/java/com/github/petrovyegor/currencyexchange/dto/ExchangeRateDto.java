package com.github.petrovyegor.currencyexchange.dto;

public class ExchangeRateDto {

    private int id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private double rate;

    public ExchangeRateDto(int id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public CurrencyDto getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyDto getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }
}
