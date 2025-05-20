package com.github.petrovyegor.currencyexchange.dto;

import com.github.petrovyegor.currencyexchange.model.Currency;

public class ExchangeRateResponseDTO {

    private int id;
    private CurrencyDTO baseCurrency;
    private CurrencyDTO targetCurrency;
    private double rate;

    public ExchangeRateResponseDTO(int id, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, double rate) {
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
