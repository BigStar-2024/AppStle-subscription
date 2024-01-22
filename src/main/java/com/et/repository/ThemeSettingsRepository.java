package com.et.repository;

import com.et.domain.ThemeSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ThemeSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThemeSettingsRepository extends JpaRepository<ThemeSettings, Long> {
    ThemeSettings findByShop(String shop);

    ThemeSettings findFirstByThemeName(String themeName);
}
