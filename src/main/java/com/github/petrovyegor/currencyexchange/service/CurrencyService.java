package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.model.Currency;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public CurrencyDto createCurrency(CurrencyDto currencyDTO) throws SQLException {
        Currency currency = currencyDao.save(toCurrency(currencyDTO));
        return toCurrencyDTO(currency);
    }

    public List<CurrencyDto> getAll() throws SQLException, ClassNotFoundException {
        List<CurrencyDto> result = new ArrayList<>();
        List<Currency> currencies = currencyDao.getAll();
        for (Currency currency : currencies) {
            result.add(toCurrencyDTO(currency));
        }
        return result;
    }

    public CurrencyDto getByCode(String code) throws SQLException, ClassNotFoundException {
        return toCurrencyDTO(currencyDao.getByCode(code));
    }

    private CurrencyDto toCurrencyDTO(Currency currency) {
        return new CurrencyDto(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }

    private Currency toCurrency(CurrencyDto source) {
        return new Currency(source.getId(), source.getCode(), source.getFullName(), source.getSign());
    }

    public boolean isCurrencyExists(String code) throws SQLException, ClassNotFoundException {
        return currencyDao.getByCode(code) != null;
    }
}
