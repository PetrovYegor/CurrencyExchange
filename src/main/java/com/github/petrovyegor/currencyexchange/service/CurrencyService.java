package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.model.Currency;

import java.sql.SQLException;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public Currency createCurrency(String code, String name, String sign) throws SQLException {
        if (currencyDao.findByCode(code) != null) {
            throw new IllegalArgumentException("");
        }
        return currencyDao.save(code, name, sign);
    }
}
