package com.example.portfolio.persistence;

import com.example.portfolio.domain.StockCriteria;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class StockSpecifications {

    private StockSpecifications() {}

    public static Specification<StockEntity> fromCriteria(StockCriteria c) {
        return Specification.where(typeIs(c))
                .and(symbolStartsWith(c))
                .and(marketValueAtLeast(c));
    }

    private static Specification<StockEntity> typeIs(StockCriteria c) {
        return (root, query, cb) ->
                c.type()
                        .map(t -> cb.equal(root.get("type"), t))
                        .orElseGet(cb::conjunction);
        // If your IDE complains, use: .orElseGet(() -> cb.conjunction())
    }

    private static Specification<StockEntity> symbolStartsWith(StockCriteria c) {
        return (root, query, cb) ->
                c.symbolPrefix()
                        .map(p -> cb.like(cb.upper(root.get("symbol")), p.toUpperCase() + "%"))
                        .orElseGet(cb::conjunction);
        // If your IDE complains, use: .orElseGet(() -> cb.conjunction())
    }

    private static Specification<StockEntity> marketValueAtLeast(StockCriteria c) {
        return (root, query, cb) -> {
            BigDecimal min = c.minMarketValue().orElse(null);
            if (min == null) return cb.conjunction();

            Expression<BigDecimal> unitPrice =
                    root.get("unitPrice").as(BigDecimal.class);

            Expression<Integer> quantity =
                    root.get("quantity").as(Integer.class);

            Expression<BigDecimal> marketValue =
                    cb.prod(unitPrice, cb.toBigDecimal(quantity));

            return cb.greaterThanOrEqualTo(marketValue, min);
        };
    }

}
