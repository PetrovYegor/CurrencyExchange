package com.github.petrovyegor.currencyexchange.dto;

import com.github.petrovyegor.currencyexchange.model.Currency;

public class ExchangeRateResponseDTO {

    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
}
