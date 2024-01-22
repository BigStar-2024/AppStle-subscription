package com.et.repository;

import com.et.domain.ShopSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ShopSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopSettingsRepository extends JpaRepository<ShopSettings, Long> {

    Optional<ShopSettings> findByShop(String shop);

    @Modifying
    @Query("delete from ShopSettings ss where ss.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
