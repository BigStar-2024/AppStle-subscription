package com.et.repository;

import com.apollographql.apollo.api.internal.ResponseFieldMapper;
import com.et.domain.BundleDynamicScript;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the BundleDynamicScript entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BundleDynamicScriptRepository extends JpaRepository<BundleDynamicScript, Long> {
    Optional<BundleDynamicScript> findByShop(String shop);

    @Modifying
    @Query("delete from BundleDynamicScript bds where bds.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
