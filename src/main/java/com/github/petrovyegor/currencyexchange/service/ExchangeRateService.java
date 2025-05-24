package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateDTO;
import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRateDTO> getAll() throws SQLException {
        List<ExchangeRate> source = exchangeRateDao.getAll();
        List<ExchangeRateDTO> result = new ArrayList<>();
        for (ExchangeRate exchangeRate : source) {
            result.add(toDTO(exchangeRate));
        }
        return result;
    }

    private ExchangeRateDTO toDTO(ExchangeRate source) throws SQLException {
        CurrencyDTO baseCurrency = toCurrencyDTO(currencyDao.getById(source.getBaseCurrencyId()));
        CurrencyDTO targetCurrency = toCurrencyDTO(currencyDao.getById(source.getTargetCurrencyId()));
        return new ExchangeRateDTO(source.getId(), baseCurrency, targetCurrency, source.getRate());
    }

    public ExchangeRateDTO getByCurrencies(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency) throws SQLException {
        ExchangeRate result = exchangeRateDao.getByCurrenciesIds(baseCurrency.getId(), targetCurrency.getId());
        return new ExchangeRateDTO(result.getId(), baseCurrency, targetCurrency, result.getRate());
    }

    public ExchangeRateDTO createExchangeRate(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, double rate) throws SQLException {
        ExchangeRate result = exchangeRateDao.save(baseCurrency.getId(), targetCurrency.getId(), rate);
        return new ExchangeRateDTO(result.getId(), baseCurrency, targetCurrency, rate);
    }

    public CurrencyDTO getCurrencyByCode(String code) throws SQLException, ClassNotFoundException {
        return toCurrencyDTO(currencyDao.getByCode(code));
    }

    public ExchangeRateDTO updateRate(double newRate, int exchangeRateId, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency) throws SQLException {
        exchangeRateDao.updateRate(newRate, exchangeRateId);
        return new ExchangeRateDTO(exchangeRateId, baseCurrency, targetCurrency, newRate);
    }

    private CurrencyDTO toCurrencyDTO(Currency currency) {
        return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }
}
