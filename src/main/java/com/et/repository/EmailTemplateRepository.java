package com.et.repository;

import com.et.domain.EmailTemplate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmailTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
}
