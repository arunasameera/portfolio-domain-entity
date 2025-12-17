package com.example.portfolio.service;

import com.example.portfolio.domain.Stock;
import com.example.portfolio.domain.StockCriteria;
import com.example.portfolio.domain.StockType;
import com.example.portfolio.persistence.StockRepository;
import com.example.portfolio.persistence.StockSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;

@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

    private final StockRepository repository;

    public PortfolioServiceImpl(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(Stock stock) {
        repository.save(StockMapper.toEntity(stock));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stock> find(StockCriteria criteria) {
        if (criteria == null) throw new IllegalArgumentException("criteria cannot be null");
        return repository.findAll(StockSpecifications.fromCriteria(criteria))
                .stream()
                .map(StockMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioSummary summarize(StockCriteria criteria) {
        List<Stock> stocks = find(criteria);

        BigDecimal total = stocks.stream()
                .map(Stock::marketValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        EnumMap<StockType, BigDecimal> byType = new EnumMap<>(StockType.class);
        for (StockType t : StockType.values()) byType.put(t, BigDecimal.ZERO);

        for (Stock s : stocks) {
            byType.put(s.type(), byType.get(s.type()).add(s.marketValue()));
        }

        return new PortfolioSummary(total, byType, stocks.size());
    }
}
