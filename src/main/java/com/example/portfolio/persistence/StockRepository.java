package com.example.portfolio.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaRepository<StockEntity, Long>, JpaSpecificationExecutor<StockEntity> {





}
