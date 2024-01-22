package com.et.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import com.et.domain.enumeration.ActivityLogEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.jhipster.service.QueryService;

import com.et.domain.ActivityLog;
import com.et.domain.*; // for static metamodels
import com.et.repository.ActivityLogRepository;
import com.et.service.dto.ActivityLogCriteria;
import com.et.service.dto.ActivityLogDTO;
import com.et.service.mapper.ActivityLogMapper;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service for executing complex queries for {@link ActivityLog} entities in the database.
 * The main input is a {@link ActivityLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActivityLogDTO} or a {@link Page} of {@link ActivityLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActivityLogQueryService extends QueryService<ActivityLog> {

    private final Logger log = LoggerFactory.getLogger(ActivityLogQueryService.class);

    private final ActivityLogRepository activityLogRepository;

    private final ActivityLogMapper activityLogMapper;

    public ActivityLogQueryService(ActivityLogRepository activityLogRepository, ActivityLogMapper activityLogMapper) {
        this.activityLogRepository = activityLogRepository;
        this.activityLogMapper = activityLogMapper;
    }

    /**
     * Return a {@link List} of {@link ActivityLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActivityLogDTO> findByCriteria(ActivityLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ActivityLog> specification = createSpecification(criteria);
        return activityLogMapper.toDto(activityLogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActivityLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActivityLogDTO> findByCriteria(ActivityLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ActivityLog> specification = createSpecification(criteria);
        return activityLogRepository.findAll(specification, page)
            .map(activityLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActivityLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ActivityLog> specification = createSpecification(criteria);
        return activityLogRepository.count(specification);
    }

    /**
     * Function to convert {@link ActivityLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ActivityLog> createSpecification(ActivityLogCriteria criteria) {
        Specification<ActivityLog> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ActivityLog_.id));
            }
            if (criteria.getShop() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShop(), ActivityLog_.shop));
            }
            if (criteria.getEntityId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntityId(), ActivityLog_.entityId));
            }
            if (criteria.getEntityType() != null) {
                specification = specification.and(buildSpecification(criteria.getEntityType(), ActivityLog_.entityType));
            }
            if (criteria.getEventSource() != null) {
                specification = specification.and(buildSpecification(criteria.getEventSource(), ActivityLog_.eventSource));
            }
            if (criteria.getEventType() != null) {
                specification = specification.and(buildSpecification(criteria.getEventType(), ActivityLog_.eventType));
            } else {
                ActivityLogCriteria.ActivityLogEventTypeFilter eventTypeFilter = new ActivityLogCriteria.ActivityLogEventTypeFilter();
                eventTypeFilter.setNotEquals(ActivityLogEventType.UPDATE_QUEUED_ATTEMPTS);
                criteria.setEventType(eventTypeFilter);
                specification = specification.and(buildSpecification(criteria.getEventType(), ActivityLog_.eventType));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ActivityLog_.status));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), ActivityLog_.createAt));
            }
        }
        return specification;
    }

    public Page<ActivityLogDTO> filterActivityLogs(String shop, ActivityLogCriteria criteria, Pageable pageable) {
        StringFilter shopFilter = new StringFilter();
        shopFilter.setEquals(shop);
        criteria.setShop(shopFilter);

        Page<ActivityLogDTO> page = findByCriteria(criteria, pageable);

        return page;
    }
}
