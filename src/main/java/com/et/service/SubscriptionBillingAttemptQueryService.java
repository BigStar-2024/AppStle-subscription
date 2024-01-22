package com.et.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.jhipster.service.QueryService;

import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.*; // for static metamodels
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.service.dto.SubscriptionBillingAttemptCriteria;
import com.et.service.dto.SubscriptionBillingAttemptDTO;
import com.et.service.mapper.SubscriptionBillingAttemptMapper;

/**
 * Service for executing complex queries for {@link SubscriptionBillingAttempt} entities in the database.
 * The main input is a {@link SubscriptionBillingAttemptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscriptionBillingAttemptDTO} or a {@link Page} of {@link SubscriptionBillingAttemptDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionBillingAttemptQueryService extends QueryService<SubscriptionBillingAttempt> {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBillingAttemptQueryService.class);

    private final SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    private final SubscriptionBillingAttemptMapper subscriptionBillingAttemptMapper;

    public SubscriptionBillingAttemptQueryService(SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository, SubscriptionBillingAttemptMapper subscriptionBillingAttemptMapper) {
        this.subscriptionBillingAttemptRepository = subscriptionBillingAttemptRepository;
        this.subscriptionBillingAttemptMapper = subscriptionBillingAttemptMapper;
    }

    /**
     * Return a {@link List} of {@link SubscriptionBillingAttemptDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionBillingAttemptDTO> findByCriteria(SubscriptionBillingAttemptCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubscriptionBillingAttempt> specification = createSpecification(criteria);
        return subscriptionBillingAttemptMapper.toDto(subscriptionBillingAttemptRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscriptionBillingAttemptDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionBillingAttemptDTO> findByCriteria(SubscriptionBillingAttemptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubscriptionBillingAttempt> specification = createSpecification(criteria);
        return subscriptionBillingAttemptRepository.findAll(specification, page)
            .map(subscriptionBillingAttemptMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscriptionBillingAttemptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubscriptionBillingAttempt> specification = createSpecification(criteria);
        return subscriptionBillingAttemptRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscriptionBillingAttemptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubscriptionBillingAttempt> createSpecification(SubscriptionBillingAttemptCriteria criteria) {
        Specification<SubscriptionBillingAttempt> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubscriptionBillingAttempt_.id));
            }
            if (criteria.getShop() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShop(), SubscriptionBillingAttempt_.shop));
            }
            if (criteria.getBillingAttemptId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillingAttemptId(), SubscriptionBillingAttempt_.billingAttemptId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), SubscriptionBillingAttempt_.status));
            }
            if (criteria.getBillingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBillingDate(), SubscriptionBillingAttempt_.billingDate));
            }
            if (criteria.getContractId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractId(), SubscriptionBillingAttempt_.contractId));
            }
            if (criteria.getAttemptCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttemptCount(), SubscriptionBillingAttempt_.attemptCount));
            }
            if (criteria.getAttemptTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttemptTime(), SubscriptionBillingAttempt_.attemptTime));
            }
            if (criteria.getGraphOrderId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGraphOrderId(), SubscriptionBillingAttempt_.graphOrderId));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderId(), SubscriptionBillingAttempt_.orderId));
            }
            if (criteria.getOrderAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderAmount(), SubscriptionBillingAttempt_.orderAmount));
            }
            if (criteria.getOrderName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderName(), SubscriptionBillingAttempt_.orderName));
            }
            if (criteria.getRetryingNeeded() != null) {
                specification = specification.and(buildSpecification(criteria.getRetryingNeeded(), SubscriptionBillingAttempt_.retryingNeeded));
            }
            if (criteria.getTransactionFailedEmailSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionFailedEmailSentStatus(), SubscriptionBillingAttempt_.transactionFailedEmailSentStatus));
            }
            if (criteria.getUpcomingOrderEmailSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getUpcomingOrderEmailSentStatus(), SubscriptionBillingAttempt_.upcomingOrderEmailSentStatus));
            }
            if (criteria.getApplyUsageCharge() != null) {
                specification = specification.and(buildSpecification(criteria.getApplyUsageCharge(), SubscriptionBillingAttempt_.applyUsageCharge));
            }
            if (criteria.getRecurringChargeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecurringChargeId(), SubscriptionBillingAttempt_.recurringChargeId));
            }
            if (criteria.getTransactionRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionRate(), SubscriptionBillingAttempt_.transactionRate));
            }
            if (criteria.getUsageChargeStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getUsageChargeStatus(), SubscriptionBillingAttempt_.usageChargeStatus));
            }
            if (criteria.getTransactionFailedSmsSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionFailedSmsSentStatus(), SubscriptionBillingAttempt_.transactionFailedSmsSentStatus));
            }
            if (criteria.getUpcomingOrderSmsSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getUpcomingOrderSmsSentStatus(), SubscriptionBillingAttempt_.upcomingOrderSmsSentStatus));
            }
            if (criteria.getProgressAttemptCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProgressAttemptCount(), SubscriptionBillingAttempt_.progressAttemptCount));
            }
            if (criteria.getSecurityChallengeSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getSecurityChallengeSentStatus(), SubscriptionBillingAttempt_.securityChallengeSentStatus));
            }
        }
        return specification;
    }
}
