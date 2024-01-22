package com.et.repository;

import com.et.domain.Analytics;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Analytics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    List<Analytics> findByShop(String shop);
}
