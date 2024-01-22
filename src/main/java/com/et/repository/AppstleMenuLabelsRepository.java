package com.et.repository;

import com.et.domain.AppstleMenuLabels;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the AppstleMenuLabels entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppstleMenuLabelsRepository extends JpaRepository<AppstleMenuLabels, Long> {

    Optional<AppstleMenuLabels> findByShop(String shop);

}
