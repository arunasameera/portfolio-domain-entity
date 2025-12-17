package com.example.portfolio.domain;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Search criteria used by the service layer.
 * - null fields mean "no constraint".
 */
public class StockCriteria {
    private final StockType type;              // nullable => any type
    private final BigDecimal minMarketValue;   // nullable => any value
    private final String symbolPrefix;         // null/blank => any symbol

    public StockCriteria(StockType type, BigDecimal minMarketValue, String symbolPrefix) {
        this.type = type;
        this.minMarketValue = minMarketValue;
        this.symbolPrefix = symbolPrefix;
    }

    public Optional<StockType> type() { return Optional.ofNullable(type); }
    public Optional<BigDecimal> minMarketValue() { return Optional.ofNullable(minMarketValue); }
    public Optional<String> symbolPrefix() {
        return (symbolPrefix == null || symbolPrefix.isBlank())
                ? Optional.empty()
                : Optional.of(symbolPrefix);
    }
}
