package com.et.repository;

import com.et.domain.ShopAssetUrls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ShopAssetUrls entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopAssetUrlsRepository extends JpaRepository<ShopAssetUrls, Long> {

    Optional<ShopAssetUrls> findByShop(String shop);

    @Modifying
    @Query("delete from ShopAssetUrls sau where sau.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
