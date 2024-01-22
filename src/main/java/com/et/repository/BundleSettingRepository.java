package com.et.repository;

import com.et.domain.BundleSetting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the BundleSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BundleSettingRepository extends JpaRepository<BundleSetting, Long> {

    Optional<BundleSetting> findByShop(String shop);

    Page<BundleSetting> findAllByShop(String shop, Pageable pageable);


    @Modifying
    @Query("delete from BundleSetting bs where bs.shop = :shop")
    void deleteByShop(@Param("shop") String shop);
}
