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

import com.et.domain.CurrencyConversionInfo;
import com.et.domain.*; // for static metamodels
import com.et.repository.CurrencyConversionInfoRepository;
import com.et.service.dto.CurrencyConversionInfoCriteria;
import com.et.service.dto.CurrencyConversionInfoDTO;
import com.et.service.mapper.CurrencyConversionInfoMapper;

/**
 * Service for executing complex queries for {@link CurrencyConversionInfo} entities in the database.
 * The main input is a {@link CurrencyConversionInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CurrencyConversionInfoDTO} or a {@link Page} of {@link CurrencyConversionInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyConversionInfoQueryService extends QueryService<CurrencyConversionInfo> {

    private final Logger log = LoggerFactory.getLogger(CurrencyConversionInfoQueryService.class);

    private final CurrencyConversionInfoRepository currencyConversionInfoRepository;

    private final CurrencyConversionInfoMapper currencyConversionInfoMapper;

    public CurrencyConversionInfoQueryService(CurrencyConversionInfoRepository currencyConversionInfoRepository, CurrencyConversionInfoMapper currencyConversionInfoMapper) {
        this.currencyConversionInfoRepository = currencyConversionInfoRepository;
        this.currencyConversionInfoMapper = currencyConversionInfoMapper;
    }

    /**
     * Return a {@link List} of {@link CurrencyConversionInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CurrencyConversionInfoDTO> findByCriteria(CurrencyConversionInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CurrencyConversionInfo> specification = createSpecification(criteria);
        return currencyConversionInfoMapper.toDto(currencyConversionInfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CurrencyConversionInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyConversionInfoDTO> findByCriteria(CurrencyConversionInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CurrencyConversionInfo> specification = createSpecification(criteria);
        return currencyConversionInfoRepository.findAll(specification, page)
            .map(currencyConversionInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyConversionInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CurrencyConversionInfo> specification = createSpecification(criteria);
        return currencyConversionInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyConversionInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CurrencyConversionInfo> createSpecification(CurrencyConversionInfoCriteria criteria) {
        Specification<CurrencyConversionInfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CurrencyConversionInfo_.id));
            }
            if (criteria.getFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFrom(), CurrencyConversionInfo_.from));
            }
            if (criteria.getTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTo(), CurrencyConversionInfo_.to));
            }
            if (criteria.getStoredOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStoredOn(), CurrencyConversionInfo_.storedOn));
            }
            if (criteria.getCurrencyRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrencyRate(), CurrencyConversionInfo_.currencyRate));
            }
        }
        return specification;
    }
}
