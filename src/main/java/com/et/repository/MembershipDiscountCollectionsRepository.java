package com.et.repository;

import com.et.domain.MembershipDiscountCollections;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MembershipDiscountCollections entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipDiscountCollectionsRepository extends JpaRepository<MembershipDiscountCollections, Long> {
}
