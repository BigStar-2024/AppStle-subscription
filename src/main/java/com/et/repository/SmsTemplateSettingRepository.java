package com.et.repository;

import com.et.domain.SmsTemplateSetting;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SmsTemplateSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsTemplateSettingRepository extends JpaRepository<SmsTemplateSetting, Long> {
    @Modifying
    @Query("delete from SmsTemplateSetting sts where sts.shop = :shop")
    void deleteByShop(@Param("shop") String shop);

    List<SmsTemplateSetting> findAllByShop(String shop);
}
