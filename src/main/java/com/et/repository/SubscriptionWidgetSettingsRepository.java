package com.et.repository;

import com.et.domain.SubscriptionWidgetSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SubscriptionWidgetSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionWidgetSettingsRepository extends JpaRepository<SubscriptionWidgetSettings, Long> {
    Optional<SubscriptionWidgetSettings> findByShop(String shop);

    @Modifying
    @Query("delete from SubscriptionWidgetSettings sws where sws.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    List<SubscriptionWidgetSettings> findAllByShop(String shop);
}
