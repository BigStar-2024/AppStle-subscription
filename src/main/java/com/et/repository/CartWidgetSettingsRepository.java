package com.et.repository;

import com.et.domain.CartWidgetSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the CartWidgetSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartWidgetSettingsRepository extends JpaRepository<CartWidgetSettings, Long> {
    @Modifying
    @Query("delete from CartWidgetSettings cws where cws.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    Optional<CartWidgetSettings> findByShop(String shop);
}
