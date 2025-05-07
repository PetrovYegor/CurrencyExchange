package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao =  new ExchangeRateDao();

    public List<ExchangeRate> getAll() throws SQLException {
        return exchangeRateDao.getAll();
    }
}
