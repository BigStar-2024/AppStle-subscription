package com.et.repository;

import com.et.domain.WidgetTemplate;
import com.et.domain.enumeration.WidgetTemplateType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WidgetTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WidgetTemplateRepository extends JpaRepository<WidgetTemplate, Long> {
    WidgetTemplate findByType(WidgetTemplateType type);
}
