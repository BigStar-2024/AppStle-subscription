package com.et.repository;

import com.et.domain.ShopCustomization;
import com.et.domain.enumeration.CustomizationCategory;
import com.et.pojo.ShopCustomizationInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ShopCustomization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopCustomizationRepository extends JpaRepository<ShopCustomization, Long> {

    List<ShopCustomization> findByShop(String shop);

    Optional<ShopCustomization> findByShopAndLabelId(String shop, Long labelId);

    @Query(value = "SELECT c.*, sc.id as shopCustomizationId, sc.value FROM customization c left join shop_customization sc on sc.label_id = c.id and shop = :shop WHERE c.category = :category ORDER BY c.type", nativeQuery = true)
    List<ShopCustomizationInfo> getShopCustomizationInfo(@Param("shop") String shop, @Param("category") String category);
}
