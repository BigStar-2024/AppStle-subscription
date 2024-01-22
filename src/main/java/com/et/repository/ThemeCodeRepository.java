package com.et.repository;

import com.et.domain.ThemeCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the ThemeCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeCodeRepository extends JpaRepository<ThemeCode, Long> {
    Optional<ThemeCode> findByThemeName(String shop);
    Optional<ThemeCode> findByThemeStoreId(Integer themeStoreId);
}
