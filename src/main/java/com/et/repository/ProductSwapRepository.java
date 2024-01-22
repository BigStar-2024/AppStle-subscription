package com.et.repository;

import com.et.domain.ProductSwap;

import com.et.service.dto.ProductSwapDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ProductSwap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductSwapRepository extends JpaRepository<ProductSwap, Long> {
    List<ProductSwap> findByShop(String shop);

    List<ProductSwap> findByShopAndCheckForEveryRecurringOrder(String shop, Boolean checkForEveryRecurringOrder);
}
