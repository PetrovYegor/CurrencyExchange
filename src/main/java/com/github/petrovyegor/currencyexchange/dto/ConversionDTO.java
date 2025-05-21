package com.github.petrovyegor.currencyexchange.dto;

public class ConversionDTO {
    private CurrencyDTO baseCurrency;
    private CurrencyDTO targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ConversionDTO(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, double rate, double amount, double convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
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

    public double getAmount() {
        return amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }
}
