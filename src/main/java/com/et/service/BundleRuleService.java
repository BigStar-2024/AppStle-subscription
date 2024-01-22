package com.et.service;

import com.et.domain.BundleRule;
import com.et.domain.enumeration.BundleStatus;
import com.et.service.dto.BundleRuleDTO;

import com.et.web.rest.vm.bundling.DiscountCodeRequest;
import com.et.web.rest.vm.bundling.DiscountCodeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.BundleRule}.
 */
public interface BundleRuleService {

    /**
     * Save a bundleRule.
     *
     * @param bundleRuleDTO the entity to save.
     * @return the persisted entity.
     */
    BundleRuleDTO save(BundleRuleDTO bundleRuleDTO);

    /**
     * Get all the bundleRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BundleRuleDTO> findAll(Pageable pageable);

    Page<BundleRuleDTO> findAllByShop(String shop,Pageable pageable);


    @Transactional(readOnly = true)
    List<BundleRuleDTO> findAllByShop(String shop);

    List<BundleRule> findAllByShopAndStatus(String shop, BundleStatus status);

    /**
     * Get the "id" bundleRule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BundleRuleDTO> findOne(Long id);

    /**
     * Delete the "id" bundleRule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Boolean updateIndex(Long id, Integer sourceIndex, Integer destinationIndex);

    Boolean updateStatus(Long id, Boolean status);

}
