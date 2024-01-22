package com.et.repository;

import com.et.domain.AsyncUpdateEventProcessing;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AsyncUpdateEventProcessing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AsyncUpdateEventProcessingRepository extends JpaRepository<AsyncUpdateEventProcessing, Long> {
}
