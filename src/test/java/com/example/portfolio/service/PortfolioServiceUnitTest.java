package com.example.portfolio.service;

import com.example.portfolio.domain.*;
import com.example.portfolio.persistence.StockEntity;
import com.example.portfolio.persistence.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceUnitTest {

    @Mock
    StockRepository repository;

    PortfolioServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PortfolioServiceImpl(repository);
    }

    @Test
    void find_throws_when_criteria_is_null() {
        assertThatThrownBy(() -> service.find(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("criteria cannot be null");

        verifyNoInteractions(repository);
    }

    @Test
    void find_returns_mapped_domain_objects() {
        StockCriteria criteria = new StockCriteria(null, null, null);

        StockEntity e1 = new StockEntity(
                "AAPL",
                10,
                new BigDecimal("10.00"),
                StockType.COMMON,
                null,
                null
        );

        StockEntity e2 = new StockEntity(
                "ETF1",
                1,
                new BigDecimal("200.00"),
                StockType.ETF,
                null,
                new BigDecimal("0.01")
        );

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(e1, e2));

        List<Stock> result = service.find(criteria);

        // Domain mapping assertions
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isInstanceOf(CommonStock.class);
        assertThat(result.get(1)).isInstanceOf(Etf.class);

        assertThat(result.get(0).marketValue()).isEqualByComparingTo("100.00");
        assertThat(result.get(1).marketValue()).isEqualByComparingTo("200.00");

        // Repository interaction
        ArgumentCaptor<Specification<StockEntity>> captor =
                ArgumentCaptor.forClass(Specification.class);

        verify(repository).findAll(captor.capture());
        assertThat(captor.getValue()).isNotNull();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void summarize_computes_totals_and_groups_correctly() {
        StockCriteria criteria = new StockCriteria(null, null, null);

        StockEntity e1 = new StockEntity(
                "AAPL",
                10,
                new BigDecimal("10.00"),
                StockType.COMMON,
                null,
                null
        ); // 100

        StockEntity e2 = new StockEntity(
                "PREF",
                2,
                new BigDecimal("60.00"),
                StockType.PREFERRED,
                new BigDecimal("0.05"),
                null
        ); // 120

        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(e1, e2));

        PortfolioSummary summary = service.summarize(criteria);

        assertThat(summary.count()).isEqualTo(2);
        assertThat(summary.totalMarketValue()).isEqualByComparingTo("220.00");
        assertThat(summary.marketValueByType().get(StockType.COMMON))
                .isEqualByComparingTo("100.00");
        assertThat(summary.marketValueByType().get(StockType.PREFERRED))
                .isEqualByComparingTo("120.00");
        assertThat(summary.marketValueByType().get(StockType.ETF))
                .isEqualByComparingTo("0");
    }

    @Test
    void add_saves_mapped_entity() {
        Stock stock = new CommonStock("AAPL", 10, new BigDecimal("10.00"));

        service.add(stock);

        verify(repository).save(any(StockEntity.class));
        verifyNoMoreInteractions(repository);
    }
}
