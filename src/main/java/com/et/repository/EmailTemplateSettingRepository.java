package com.et.repository;

import com.et.domain.EmailTemplateSetting;
import com.et.domain.enumeration.EmailSettingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the EmailTemplateSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailTemplateSettingRepository extends JpaRepository<EmailTemplateSetting, Long> {
    @Modifying
    @Query("delete from EmailTemplateSetting ets where ets.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    List<EmailTemplateSetting> findByShop(String shop);

    Optional<EmailTemplateSetting> findByShopAndEmailSettingType(String shop, EmailSettingType emailSettingType);

    @Modifying
    @Query("delete from EmailTemplateSetting ets where ets.shop = :shop and ets.emailSettingType in :emailSettingTypes")
    void deleteByShopAndEmailSettingTypeIn(@Param("shop") String shop, @Param("emailSettingTypes") List<EmailSettingType> emailSettingTypes);

    @Modifying
    @Query("delete from EmailTemplateSetting ets where ets.shop = :shop and ets.emailSettingType not in :emailSettingTypes")
    void deleteByShopAndEmailSettingTypeNotIn(@Param("shop") String shop, @Param("emailSettingTypes") List<EmailSettingType> emailSettingTypes);
}
