package com.example.portfolio.persistence;

import com.example.portfolio.domain.StockType;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * JPA persistence model.
 * Kept separate from the domain model to maintain loose coupling.
 */
@Entity
@Table(name = "stocks")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String symbol;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StockType type;

    // --- Type-specific optional fields ---

    @Column(precision = 19, scale = 6)
    private BigDecimal dividendRate;   // PREFERRED only

    @Column(precision = 19, scale = 6)
    private BigDecimal expenseRatio;   // ETF only

    /**
     * Required by JPA.
     * Protected to prevent accidental use outside persistence layer.
     */
    protected StockEntity() {
    }

    /**
     * Preferred constructor for application and tests.
     */
    public StockEntity(
            String symbol,
            int quantity,
            BigDecimal unitPrice,
            StockType type,
            BigDecimal dividendRate,
            BigDecimal expenseRatio
    ) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.type = type;
        this.dividendRate = dividendRate;
        this.expenseRatio = expenseRatio;
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public StockType getType() {
        return type;
    }

    public BigDecimal getDividendRate() {
        return dividendRate;
    }

    public BigDecimal getExpenseRatio() {
        return expenseRatio;
    }

    // --- Setters (used by JPA / tests only) ---

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setType(StockType type) {
        this.type = type;
    }

    public void setDividendRate(BigDecimal dividendRate) {
        this.dividendRate = dividendRate;
    }

    public void setExpenseRatio(BigDecimal expenseRatio) {
        this.expenseRatio = expenseRatio;
    }
}
