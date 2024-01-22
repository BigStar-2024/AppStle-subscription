package com.et.repository;

import com.et.domain.PlanInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the PlanInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanInfoRepository extends JpaRepository<PlanInfo, Long> {
    Optional<PlanInfo> findByNameIgnoreCase(String name);
}
