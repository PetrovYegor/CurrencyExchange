package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDTO;
import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRateResponseDTO> getAll() throws SQLException {
        List<ExchangeRate> source = exchangeRateDao.getAll();
        List<ExchangeRateResponseDTO> result = new ArrayList<>();
        for (ExchangeRate exchangeRate : source) {
            result.add(toDTO(exchangeRate));
        }
        return result;
    }

    private ExchangeRateResponseDTO toDTO(ExchangeRate source) throws SQLException {
        CurrencyDTO baseCurrency = toCurrencyDTO(currencyDao.getById(source.getBaseCurrencyId()));
        CurrencyDTO targetCurrency = toCurrencyDTO(currencyDao.getById(source.getTargetCurrencyId()));
        return new ExchangeRateResponseDTO(source.getId(), baseCurrency, targetCurrency, source.getRate());
    }

    public ExchangeRateResponseDTO getByCurrencies(Currency baseCurrency, Currency targetCurrency) throws SQLException {
        ExchangeRate result = exchangeRateDao.getByCurrenciesIds(baseCurrency.getId(), targetCurrency.getId());
        if (result != null) {
            return new ExchangeRateResponseDTO(result.getId(), toCurrencyDTO(baseCurrency), toCurrencyDTO(targetCurrency), result.getRate());
        }

        return null;
    }

    public ExchangeRateResponseDTO createExchangeRate(Currency baseCurrency, Currency targetCurrency, double rate) throws SQLException {
        ExchangeRate result = exchangeRateDao.save(baseCurrency.getId(), targetCurrency.getId(), rate);
        return new ExchangeRateResponseDTO(result.getId(), toCurrencyDTO(baseCurrency), toCurrencyDTO(targetCurrency), rate);
    }

    public Currency getCurrencyByCode(String code) throws SQLException, ClassNotFoundException {
        return currencyDao.getByCode(code);
    }

    public ExchangeRateResponseDTO updateRate(double newRate, int exchangeRateId, Currency baseCurrency, Currency targetCurrency) throws SQLException {
        exchangeRateDao.updateRate(newRate, exchangeRateId);
        return new ExchangeRateResponseDTO(exchangeRateId, toCurrencyDTO(baseCurrency), toCurrencyDTO(targetCurrency), newRate);
    }

    private CurrencyDTO toCurrencyDTO(Currency currency) {
        if (currency != null){
            return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
        }
        return null;
    }
}
