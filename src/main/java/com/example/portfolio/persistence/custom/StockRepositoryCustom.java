package com.example.portfolio.persistence.custom;

import com.example.portfolio.domain.StockCriteria;
import com.example.portfolio.persistence.StockEntity;

import java.util.List;

public interface StockRepositoryCustom {
    List<StockEntity> search(StockCriteria criteria);
}
