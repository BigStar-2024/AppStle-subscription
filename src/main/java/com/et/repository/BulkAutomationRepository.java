package com.et.repository;

import com.et.domain.BulkAutomation;

import com.et.domain.enumeration.BulkAutomationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the BulkAutomation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BulkAutomationRepository extends JpaRepository<BulkAutomation, Long> {

    Page<BulkAutomation> findAllByShop(String shop, Pageable page);

    Optional<BulkAutomation> findByShopAndAutomationType(String shop, BulkAutomationType automationType);
}
