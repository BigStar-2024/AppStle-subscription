package com.et.repository;

import com.apollographql.apollo.api.internal.ResponseFieldMapper;
import com.et.domain.CustomerPortalSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CustomerPortalSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerPortalSettingsRepository extends JpaRepository<CustomerPortalSettings, Long> {
    Optional<CustomerPortalSettings> findByShop(String shop);

    @Modifying
    @Query("delete from CustomerPortalSettings cps where cps.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    List<CustomerPortalSettings> findAllByShop(String shop);
}
