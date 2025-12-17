package com.example.portfolio.domain;

import java.math.BigDecimal;

public sealed interface Stock permits CommonStock, PreferredStock, Etf {

    String symbol();
    int quantity();
    BigDecimal unitPrice();
    StockType type();

    default BigDecimal marketValue() {
        return unitPrice().multiply(BigDecimal.valueOf(quantity()));
    }
}
