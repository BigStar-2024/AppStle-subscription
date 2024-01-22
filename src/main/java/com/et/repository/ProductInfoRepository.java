package com.et.repository;

import com.et.domain.ProductInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ProductInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

    Optional<ProductInfo> findOneByShopAndProductId(String shop, Long productId);

    @Modifying
    @Query("delete from ProductInfo pp where pp.shop = :shop and pp.productId = :productId")
    void deleteByShopAndProductId(@Param("shop") String shop, @Param("productId") Long productId);

    @Query("select pi from ProductInfo pi where pi.shop = :shop and pi.productId = :productId")
    List<ProductInfo> findOneByShopAndProductIdList(@Param("shop") String shop, @Param("productId") Long productId);

    List<ProductInfo> findByShopAndProductIdIn(String shop, List<Long> productList);
}
