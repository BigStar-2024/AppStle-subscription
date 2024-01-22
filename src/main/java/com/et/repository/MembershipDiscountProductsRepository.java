package com.et.repository;

import com.et.domain.MembershipDiscountProducts;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MembershipDiscountProducts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipDiscountProductsRepository extends JpaRepository<MembershipDiscountProducts, Long> {
    List<MembershipDiscountProducts> findByShopAndMembershipDiscountId(String shop, Long membershipDiscountId);
}
