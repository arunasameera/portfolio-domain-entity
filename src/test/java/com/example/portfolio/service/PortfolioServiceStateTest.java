package com.example.portfolio.service;

import com.example.portfolio.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PortfolioServiceStateTest {

    @Autowired
    PortfolioService service;

    @Test
    void summarize_groups_and_totals_by_state() {
        service.add(new CommonStock("AAPL", 10, new BigDecimal("10.00"))); // 100.00
        service.add(new PreferredStock("PREF", 2, new BigDecimal("60.00"), new BigDecimal("0.05"))); // 120.00
        service.add(new Etf("ETF1", 1, new BigDecimal("200.00"), new BigDecimal("0.01"))); // 200.00

        PortfolioSummary summary = service.summarize(new StockCriteria(null, null, null));

        assertThat(summary.count()).isEqualTo(3);
        assertThat(summary.totalMarketValue()).isEqualByComparingTo("420.00");

        assertThat(summary.marketValueByType().get(StockType.COMMON)).isEqualByComparingTo("100.00");
        assertThat(summary.marketValueByType().get(StockType.PREFERRED)).isEqualByComparingTo("120.00");
        assertThat(summary.marketValueByType().get(StockType.ETF)).isEqualByComparingTo("200.00");
    }

    @Test
    void find_returns_domain_objects_not_entities() {
        service.add(new CommonStock("AAPL", 1, new BigDecimal("10.00")));

        var result = service.find(new StockCriteria(StockType.COMMON, null, "AA"));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(CommonStock.class);
        assertThat(result.getFirst().symbol()).isEqualTo("AAPL");
    }

    @Test
    void find_throws_when_criteria_is_null() {
        assertThatThrownBy(() -> service.find(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("criteria cannot be null");
    }

    @Test
    void find_blank_prefix_is_ignored() {
        service.add(new CommonStock("AAPL", 1, new BigDecimal("10.00")));
        service.add(new CommonStock("MSFT", 1, new BigDecimal("10.00")));

        var result = service.find(new StockCriteria(StockType.COMMON, null, "   "));

        assertThat(result).hasSize(2);
    }

    @Test
    void find_minValue_boundary_includes_equal_value() {
        service.add(new CommonStock("AAPL", 10, new BigDecimal("10.00"))); // 100.00

        var result = service.find(new StockCriteria(null, new BigDecimal("100.00"), null));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().symbol()).isEqualTo("AAPL");
    }
}
