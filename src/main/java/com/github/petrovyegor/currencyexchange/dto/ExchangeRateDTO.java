package com.github.petrovyegor.currencyexchange.dto;

public class ExchangeRateDTO {

    private int id;
    private CurrencyDTO baseCurrency;
    private CurrencyDTO targetCurrency;
    private double rate;

    public ExchangeRateDTO(int id, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public CurrencyDTO getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyDTO getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }
}
