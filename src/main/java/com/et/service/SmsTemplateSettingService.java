package com.et.service;

import ClickSend.ApiException;
import com.et.service.dto.SmsTemplateSettingDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SmsTemplateSetting}.
 */
public interface SmsTemplateSettingService {

    /**
     * Save a smsTemplateSetting.
     *
     * @param smsTemplateSettingDTO the entity to save.
     * @return the persisted entity.
     */
    SmsTemplateSettingDTO save(SmsTemplateSettingDTO smsTemplateSettingDTO);

    /**
     * Get all the smsTemplateSettings.
     *
     * @return the list of entities.
     */
    List<SmsTemplateSettingDTO> findAll();


    /**
     * Get the "id" smsTemplateSetting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SmsTemplateSettingDTO> findOne(Long id);

    /**
     * Delete the "id" smsTemplateSetting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteByShop(String shop);

    String getSmsContent(SmsTemplateSettingDTO smsTemplateSettingDTO) throws IOException;

    Boolean sendTestSMS(Long smsTemplateId, String phone) throws ApiException, IOException;

    List<SmsTemplateSettingDTO> findAllByShop(String shop);
}
