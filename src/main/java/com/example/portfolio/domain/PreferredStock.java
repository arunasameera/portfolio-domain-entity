package com.example.portfolio.domain;

import java.math.BigDecimal;

public record PreferredStock(String symbol, int quantity, BigDecimal unitPrice, BigDecimal dividendRate) implements Stock {
    @Override public StockType type() { return StockType.PREFERRED; }
}
