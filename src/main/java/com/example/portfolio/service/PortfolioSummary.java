package com.example.portfolio.service;

import com.example.portfolio.domain.StockType;

import java.math.BigDecimal;
import java.util.Map;

public record PortfolioSummary(
        BigDecimal totalMarketValue,
        Map<StockType, BigDecimal> marketValueByType,
        int count
) {}
