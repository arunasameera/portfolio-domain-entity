package com.example.portfolio.service;

import com.example.portfolio.domain.*;
import com.example.portfolio.persistence.StockEntity;

import java.math.BigDecimal;

public final class StockMapper {

    private StockMapper() {}

    public static Stock toDomain(StockEntity e) {
        return switch (e.getType()) {
            case COMMON -> new CommonStock(e.getSymbol(), e.getQuantity(), e.getUnitPrice());
            case PREFERRED -> new PreferredStock(
                    e.getSymbol(), e.getQuantity(), e.getUnitPrice(),
                    safe(e.getDividendRate())
            );
            case ETF -> new Etf(
                    e.getSymbol(), e.getQuantity(), e.getUnitPrice(),
                    safe(e.getExpenseRatio())
            );
        };
    }

    public static StockEntity toEntity(Stock s) {
        return switch (s.type()) {
            case COMMON -> new StockEntity(s.symbol(), s.quantity(), s.unitPrice(), StockType.COMMON, null, null);
            case PREFERRED -> {
                PreferredStock ps = (PreferredStock) s;
                yield new StockEntity(ps.symbol(), ps.quantity(), ps.unitPrice(), StockType.PREFERRED, ps.dividendRate(), null);
            }
            case ETF -> {
                Etf etf = (Etf) s;
                yield new StockEntity(etf.symbol(), etf.quantity(), etf.unitPrice(), StockType.ETF, null, etf.expenseRatio());
            }
        };
    }

    private static BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
