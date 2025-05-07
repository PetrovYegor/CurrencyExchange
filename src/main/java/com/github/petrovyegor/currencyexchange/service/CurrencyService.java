package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.model.Currency;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public Currency createCurrency(String code, String name, String sign) throws SQLException {
        return currencyDao.save(code, name, sign);
    }

    public List<Currency> getAll() throws SQLException {
        return currencyDao.getAll();
    }

    public Currency getByCode(String code) throws SQLException {
        return currencyDao.getByCode(code);
    }
}
