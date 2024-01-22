package com.et.repository;

import com.et.domain.AppstleMenuSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the AppstleMenuSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppstleMenuSettingsRepository extends JpaRepository<AppstleMenuSettings, Long> {

    Optional<AppstleMenuSettings> findByShop(String shop);
}
