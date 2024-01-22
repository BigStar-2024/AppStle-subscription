package com.et.repository;

import com.apollographql.apollo.api.internal.ResponseFieldMapper;
import com.et.domain.ShopInfo;
import com.et.pojo.ExportMerchantsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ShopInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopInfoRepository extends JpaRepository<ShopInfo, Long> {
    Optional<ShopInfo> findByShop(String shop);

    List<ShopInfo> findAllByShop(String shop);

    Optional<ShopInfo> findByApiKey(String apiKey);

    @Modifying
    @Query("delete from ShopInfo si where si.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    List<ShopInfo> findAllByCountryCodeIsNullAndCountryNameIsNull();

    String commonGetMechantDetailQuery = "SELECT distinct *, usedRecOrderAmount + usedFirstOrderAmount as totalRevenue  from ( select pp.shop, public_domain as publicDomain, country_name as countryName, country_code as countryCode, pp.name, pp.shopify_plan_display_name as shopifyPlanDisplayName,\n" +
        "pp.shopify_plan_name as shopifyPlanName, pp.activation_date as activationDate, currency,\n" +
        "(select sum(sba.order_amount) from subscription_billing_attempt sba where sba.shop = shop_info.shop and upper(sba.status) = \"SUCCESS\" and sba.order_amount is not null) as usedRecOrderAmount,\n" +
        "(select sum(scd.order_amount_usd) from subscription_contract_details scd where scd.shop = shop_info.shop and scd.order_amount_usd is not null AND scd.imported_id is null) as usedFirstOrderAmount\n" +
        "from shop_info \n" +
        "inner join payment_plan pp on shop_info.shop = pp.shop";

    @Query(value = commonGetMechantDetailQuery +"\t inner join subscription_bundling sb on shop_info.shop = sb.shop)  result", nativeQuery = true)
    List<ExportMerchantsResponse> findAllShopWhichUseBuildABoxFeature(Pageable pageable);

    @Query(value = commonGetMechantDetailQuery +"\t inner join appstle_menu_settings am on shop_info.shop = am.shop and am.active = true)  result", nativeQuery = true)
    List<ExportMerchantsResponse> findAllShopWhichUseAppstleMenu(Pageable pageable);

}
