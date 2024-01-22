package com.et.repository;

import com.et.domain.MembershipDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MembershipDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipDiscountRepository extends JpaRepository<MembershipDiscount, Long> {

    List<MembershipDiscount> findByShop(String shop);

}
