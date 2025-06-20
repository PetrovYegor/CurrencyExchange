CREATE TABLE IF NOT EXISTS ExchangeRates (
                                             Id INTEGER PRIMARY KEY AUTOINCREMENT,
                                             BaseCurrencyId INTEGER NOT NULL,
                                             TargetCurrencyId INTEGER NOT NULL,
                                             Rate DECIMAL NOT NULL CHECK (
                                             Rate > 0 AND
                                             Rate < 1000 AND
                                             Rate = ROUND(Rate, 6)--после запятой может быть не больше 6-ти знаков
    ),
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (Id) ON DELETE RESTRICT,
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (Id) ON DELETE RESTRICT
    );

CREATE UNIQUE INDEX idx_unique_pair_of_basecurrency_targetcurrency
    ON ExchangeRates(BaseCurrencyId, TargetCurrencyId);