package com.et.repository;

import com.et.domain.CustomerPortalDynamicScript;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the CustomerPortalDynamicScript entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerPortalDynamicScriptRepository extends JpaRepository<CustomerPortalDynamicScript, Long> {
    Optional<CustomerPortalDynamicScript> findByShop(String shop);

    @Modifying
    @Query("delete from CustomerPortalDynamicScript cpds where cpds.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
