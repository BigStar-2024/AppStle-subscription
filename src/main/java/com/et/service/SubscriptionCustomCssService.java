package com.et.service;

import com.et.service.dto.SubscriptionCustomCssDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionCustomCss}.
 */
public interface SubscriptionCustomCssService {

    /**
     * Save a subscriptionCustomCss.
     *
     * @param subscriptionCustomCssDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionCustomCssDTO save(SubscriptionCustomCssDTO subscriptionCustomCssDTO);

    /**
     * Get all the subscriptionCustomCsses.
     *
     * @return the list of entities.
     */
    List<SubscriptionCustomCssDTO> findAll();


    /**
     * Get the "id" subscriptionCustomCss.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionCustomCssDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionCustomCss.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<SubscriptionCustomCssDTO> findByShop(String shop);

    void deleteByShop(String shop);

    List<SubscriptionCustomCssDTO> findAllByShop(String shop);
}
