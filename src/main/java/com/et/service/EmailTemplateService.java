package com.et.service;

import com.et.domain.EmailTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link EmailTemplate}.
 */
public interface EmailTemplateService {

    /**
     * Save a emailTemplate.
     *
     * @param emailTemplate the entity to save.
     * @return the persisted entity.
     */
    EmailTemplate save(EmailTemplate emailTemplate);

    /**
     * Get all the emailTemplates.
     *
     * @return the list of entities.
     */
    List<EmailTemplate> findAll();


    /**
     * Get the "id" emailTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailTemplate> findOne(Long id);

    /**
     * Delete the "id" emailTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
