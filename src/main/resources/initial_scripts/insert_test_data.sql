INSERT INTO Currencies (Id, Code, FullName, Sign) VALUES
                                                      (1,'RUB','Russian Ruble','₽'),
                                                      (2,'USD','United States Dollar','$'),
                                                      (3,'EUR','Euro','€'),
                                                      (4,'CNY','Chinese Yuan','¥'),
                                                      (5,'INR','Indian Rupee','₹');

INSERT INTO ExchangeRates (Id, BaseCurrencyId, TargetCurrencyId, Rate) VALUES
                                                                           (1,1,4,0.090),--рубль в юань
                                                                           (2,2,1, 79.88),--доллар в рубль
                                                                           (3,2,3, 0.89),--доллар в евро
                                                                           (4,5,1, 0.94);--рупии в рубли