package com.et.repository;

import com.et.domain.ShopLabel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ShopLabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopLabelRepository extends JpaRepository<ShopLabel, Long> {

    @Query("select distinct(sl.shop) from ShopLabel sl where JSON_SEARCH(sl.labels, 'one', :key) is null")
    List<String> findAllShopsWithMissingLabel(@Param("key") String key);

    @Query("select distinct(sl.shop) from ShopLabel sl where JSON_SEARCH(sl.labels, 'one', :key) is not null")
    List<String> findAllShopsWithLabel(@Param("key") String key);

    Optional<ShopLabel> findFirstByShop(String shop);

    Page<ShopLabel> findAllByShop(String shop, Pageable page);

    List<ShopLabel> findAllByShop(String shop);

    Optional<ShopLabel> findByIdAndShop(Long id, String shop);
}
