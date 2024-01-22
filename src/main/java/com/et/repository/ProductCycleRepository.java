package com.et.repository;

import com.et.domain.ProductCycle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ProductCycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCycleRepository extends JpaRepository<ProductCycle, Long> {

    List<ProductCycle> findByShop(String shop);
}
