package com.example.portfolio.service;

import com.example.portfolio.domain.Stock;
import com.example.portfolio.domain.StockCriteria;

import java.util.List;

public interface PortfolioService {
    List<Stock> find(StockCriteria criteria);
    PortfolioSummary summarize(StockCriteria criteria);
    void add(Stock stock);
}
