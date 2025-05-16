package com.github.petrovyegor.currencyexchange.model;

public class Exchange {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public Exchange (Currency baseCurrency, Currency targetCurrency, double rate, double amount, double convertedAmount){
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = amount;
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

    public double getAmount() {
        return amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }
}
