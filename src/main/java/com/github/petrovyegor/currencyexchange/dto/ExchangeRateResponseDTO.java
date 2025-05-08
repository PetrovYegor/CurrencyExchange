package com.github.petrovyegor.currencyexchange.dto;

import com.github.petrovyegor.currencyexchange.model.Currency;

public class ExchangeRateResponseDTO {

    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;

    public ExchangeRateResponseDTO(int id, Currency baseCurrency, Currency targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }
}
