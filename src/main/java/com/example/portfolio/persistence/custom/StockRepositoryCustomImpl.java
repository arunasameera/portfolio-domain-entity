package com.example.portfolio.persistence.custom;

import com.example.portfolio.domain.StockCriteria;
import com.example.portfolio.persistence.StockEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StockRepositoryCustomImpl implements StockRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<StockEntity> search(StockCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StockEntity> cq = cb.createQuery(StockEntity.class);
        Root<StockEntity> root = cq.from(StockEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        criteria.type().ifPresent(type ->
                predicates.add(cb.equal(root.get("type"), type)));

        criteria.minValue().ifPresent(min -> {
            Expression<java.math.BigDecimal> marketValue =
                    cb.prod(root.get("price"), root.get("quantity"));
            predicates.add(cb.greaterThanOrEqualTo(marketValue, min));
        });

        criteria.symbolPrefix().ifPresent(prefix ->
                predicates.add(cb.like(cb.lower(root.get("symbol")),
                        prefix.toLowerCase() + "%")));

        cq.where(predicates.toArray(Predicate[]::new));
        return em.createQuery(cq).getResultList();
    }
}
