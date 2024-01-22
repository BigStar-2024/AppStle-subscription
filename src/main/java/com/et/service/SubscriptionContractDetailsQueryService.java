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

import com.et.domain.SubscriptionContractDetails;
import com.et.domain.*; // for static metamodels
import com.et.repository.SubscriptionContractDetailsRepository;
import com.et.service.dto.SubscriptionContractDetailsCriteria;
import com.et.service.dto.SubscriptionContractDetailsDTO;
import com.et.service.mapper.SubscriptionContractDetailsMapper;

/**
 * Service for executing complex queries for {@link SubscriptionContractDetails} entities in the database.
 * The main input is a {@link SubscriptionContractDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscriptionContractDetailsDTO} or a {@link Page} of {@link SubscriptionContractDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionContractDetailsQueryService extends QueryService<SubscriptionContractDetails> {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractDetailsQueryService.class);

    private final SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    private final SubscriptionContractDetailsMapper subscriptionContractDetailsMapper;

    public SubscriptionContractDetailsQueryService(SubscriptionContractDetailsRepository subscriptionContractDetailsRepository, SubscriptionContractDetailsMapper subscriptionContractDetailsMapper) {
        this.subscriptionContractDetailsRepository = subscriptionContractDetailsRepository;
        this.subscriptionContractDetailsMapper = subscriptionContractDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link SubscriptionContractDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionContractDetailsDTO> findByCriteria(SubscriptionContractDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubscriptionContractDetails> specification = createSpecification(criteria);
        return subscriptionContractDetailsMapper.toDto(subscriptionContractDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscriptionContractDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionContractDetailsDTO> findByCriteria(SubscriptionContractDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubscriptionContractDetails> specification = createSpecification(criteria);
        return subscriptionContractDetailsRepository.findAll(specification, page)
            .map(subscriptionContractDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscriptionContractDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubscriptionContractDetails> specification = createSpecification(criteria);
        return subscriptionContractDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscriptionContractDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubscriptionContractDetails> createSpecification(SubscriptionContractDetailsCriteria criteria) {
        Specification<SubscriptionContractDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubscriptionContractDetails_.id));
            }
            if (criteria.getShop() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShop(), SubscriptionContractDetails_.shop));
            }
            if (criteria.getGraphSubscriptionContractId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGraphSubscriptionContractId(), SubscriptionContractDetails_.graphSubscriptionContractId));
            }
            if (criteria.getSubscriptionContractId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubscriptionContractId(), SubscriptionContractDetails_.subscriptionContractId));
            }
            if (criteria.getBillingPolicyInterval() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBillingPolicyInterval(), SubscriptionContractDetails_.billingPolicyInterval));
            }
            if (criteria.getBillingPolicyIntervalCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBillingPolicyIntervalCount(), SubscriptionContractDetails_.billingPolicyIntervalCount));
            }
            if (criteria.getCurrencyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyCode(), SubscriptionContractDetails_.currencyCode));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), SubscriptionContractDetails_.customerId));
            }
            if (criteria.getGraphCustomerId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGraphCustomerId(), SubscriptionContractDetails_.graphCustomerId));
            }
            if (criteria.getDeliveryPolicyInterval() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeliveryPolicyInterval(), SubscriptionContractDetails_.deliveryPolicyInterval));
            }
            if (criteria.getDeliveryPolicyIntervalCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryPolicyIntervalCount(), SubscriptionContractDetails_.deliveryPolicyIntervalCount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), SubscriptionContractDetails_.status));
            }
            if (criteria.getGraphOrderId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGraphOrderId(), SubscriptionContractDetails_.graphOrderId));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderId(), SubscriptionContractDetails_.orderId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SubscriptionContractDetails_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SubscriptionContractDetails_.updatedAt));
            }
            if (criteria.getNextBillingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNextBillingDate(), SubscriptionContractDetails_.nextBillingDate));
            }
            if (criteria.getOrderAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderAmount(), SubscriptionContractDetails_.orderAmount));
            }
            if (criteria.getOrderName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderName(), SubscriptionContractDetails_.orderName));
            }
            if (criteria.getCustomerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerName(), SubscriptionContractDetails_.customerName));
            }
            if (criteria.getCustomerEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerEmail(), SubscriptionContractDetails_.customerEmail));
            }
            if (criteria.getSubscriptionCreatedEmailSent() != null) {
                specification = specification.and(buildSpecification(criteria.getSubscriptionCreatedEmailSent(), SubscriptionContractDetails_.subscriptionCreatedEmailSent));
            }
            if (criteria.getEndsAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndsAt(), SubscriptionContractDetails_.endsAt));
            }
            if (criteria.getStartsAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartsAt(), SubscriptionContractDetails_.startsAt));
            }
            if (criteria.getSubscriptionCreatedEmailSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getSubscriptionCreatedEmailSentStatus(), SubscriptionContractDetails_.subscriptionCreatedEmailSentStatus));
            }
            if (criteria.getMinCycles() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinCycles(), SubscriptionContractDetails_.minCycles));
            }
            if (criteria.getMaxCycles() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxCycles(), SubscriptionContractDetails_.maxCycles));
            }
            if (criteria.getCustomerFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerFirstName(), SubscriptionContractDetails_.customerFirstName));
            }
            if (criteria.getCustomerLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerLastName(), SubscriptionContractDetails_.customerLastName));
            }
            if (criteria.getAutoCharge() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoCharge(), SubscriptionContractDetails_.autoCharge));
            }
            if (criteria.getImportedId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImportedId(), SubscriptionContractDetails_.importedId));
            }
            if (criteria.getStopUpComingOrderEmail() != null) {
                specification = specification.and(buildSpecification(criteria.getStopUpComingOrderEmail(), SubscriptionContractDetails_.stopUpComingOrderEmail));
            }
            if (criteria.getPausedFromActive() != null) {
                specification = specification.and(buildSpecification(criteria.getPausedFromActive(), SubscriptionContractDetails_.pausedFromActive));
            }
            if (criteria.getSubscriptionCreatedSmsSentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getSubscriptionCreatedSmsSentStatus(), SubscriptionContractDetails_.subscriptionCreatedSmsSentStatus));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), SubscriptionContractDetails_.phone));
            }
            if (criteria.getActivatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivatedOn(), SubscriptionContractDetails_.activatedOn));
            }
            if (criteria.getPausedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPausedOn(), SubscriptionContractDetails_.pausedOn));
            }
            if (criteria.getCancelledOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCancelledOn(), SubscriptionContractDetails_.cancelledOn));
            }
            if (criteria.getAllowDeliveryPriceOverride() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowDeliveryPriceOverride(), SubscriptionContractDetails_.allowDeliveryPriceOverride));
            }
        }
        return specification;
    }
}
