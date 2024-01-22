package com.et.repository;

import com.et.domain.SubscriptionBundleSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the SubscriptionBundleSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionBundleSettingsRepository extends JpaRepository<SubscriptionBundleSettings, Long> {

    Optional<SubscriptionBundleSettings> findByShop(String shop);

    @Modifying
    @Query("delete from SubscriptionBundleSettings sbs where sbs.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
