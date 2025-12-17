package com.example.portfolio.domain;

import java.math.BigDecimal;

public record Etf(String symbol, int quantity, BigDecimal unitPrice, BigDecimal expenseRatio) implements Stock {
    @Override public StockType type() { return StockType.ETF; }
}
