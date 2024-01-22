package com.et.service.impl;

import com.et.service.EmailTemplateService;
import com.et.domain.EmailTemplate;
import com.et.repository.EmailTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link EmailTemplate}.
 */
@Service
@Transactional
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);

    private final EmailTemplateRepository emailTemplateRepository;

    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public EmailTemplate save(EmailTemplate emailTemplate) {
        log.debug("Request to save EmailTemplate : {}", emailTemplate);
        return emailTemplateRepository.save(emailTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailTemplate> findAll() {
        log.debug("Request to get all EmailTemplates");
        return emailTemplateRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmailTemplate> findOne(Long id) {
        log.debug("Request to get EmailTemplate : {}", id);
        return emailTemplateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmailTemplate : {}", id);
        emailTemplateRepository.deleteById(id);
    }
}
