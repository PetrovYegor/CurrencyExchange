package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.model.Currency;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public Currency createCurrency(String code, String name, String sign) throws SQLException {
        return currencyDao.save(code, name, sign);
    }

    public List<CurrencyDTO> getAll() throws SQLException {
        List<CurrencyDTO> result = new ArrayList<>();
        List<Currency> currencies = currencyDao.getAll();
        for (Currency currency : currencies) {
            result.add(toDTO(currency));
        }
        return result;
    }

    public Currency getByCode(String code) throws SQLException {
        return currencyDao.getByCode(code);
    }

    private CurrencyDTO toDTO(Currency currency) {
        return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }

}
