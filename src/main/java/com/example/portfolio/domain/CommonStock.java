package com.example.portfolio.domain;

import java.math.BigDecimal;

public record CommonStock(String symbol, int quantity, BigDecimal unitPrice) implements Stock {
    @Override public StockType type() { return StockType.COMMON; }
}
