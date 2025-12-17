package com.example.portfolio.persistence;

import com.example.portfolio.domain.StockCriteria;
import com.example.portfolio.domain.StockType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StockRepositoryStateTest {

    @Autowired StockRepository repo;

    @Test
    void spec_filters_by_type_prefix_and_min_market_value_statefully() {
        repo.save(new StockEntity("AAPL", 10, new BigDecimal("10.00"), StockType.COMMON, null, null));          // mv=100
        repo.save(new StockEntity("AAP",  2,  new BigDecimal("60.00"), StockType.PREFERRED, new BigDecimal("0.05"), null)); // mv=120
        repo.save(new StockEntity("MSFT", 1,  new BigDecimal("200.00"), StockType.ETF, null, new BigDecimal("0.01")));     // mv=200

        StockCriteria c = new StockCriteria(StockType.PREFERRED, new BigDecimal("110"), "AA");

        var results = repo.findAll(StockSpecifications.fromCriteria(c));

        // Assert on returned DATA STATE, not method calls
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getType()).isEqualTo(StockType.PREFERRED);
        assertThat(results.getFirst().getSymbol()).startsWith("AA");
        assertThat(results.getFirst().getUnitPrice().multiply(BigDecimal.valueOf(results.getFirst().getQuantity())))
                .isGreaterThanOrEqualTo(new BigDecimal("110"));
    }
}
