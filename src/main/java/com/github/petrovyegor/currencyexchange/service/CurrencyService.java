package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.model.Currency;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public Currency createCurrency(String code, String name, String sign) throws SQLException {
        if (currencyDao.getByCode(code) != null) {
            throw new IllegalArgumentException("Currency already exists");
        }
        return currencyDao.save(code, name, sign);
    }

    public List<Currency> getAllCurrencies() throws SQLException {
        return currencyDao.getAll();
    }

    public Currency getByCode(String code) throws SQLException {
        return currencyDao.getByCode(code);
    }
}
