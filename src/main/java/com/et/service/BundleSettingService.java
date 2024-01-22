package com.et.service;

import com.et.service.dto.BundleSettingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.BundleSetting}.
 */
public interface BundleSettingService {

    /**
     * Save a bundleSetting.
     *
     * @param bundleSettingDTO the entity to save.
     * @return the persisted entity.
     */
    BundleSettingDTO save(BundleSettingDTO bundleSettingDTO);

    /**
     * Get all the bundleSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BundleSettingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bundleSetting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BundleSettingDTO> findOne(Long id);

    Optional<BundleSettingDTO> findByShop(String shop);

    @Transactional(readOnly = true)
    Page<BundleSettingDTO> findAllByShop(String shop, Pageable pageable);

    /**
     * Delete the "id" bundleSetting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteByShop(String shop);
}
