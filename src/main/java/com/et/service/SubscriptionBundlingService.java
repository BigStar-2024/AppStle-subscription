package com.et.service;

import com.et.domain.SubscriptionBundling;
import com.et.domain.enumeration.BuildABoxType;
import com.et.service.dto.SubscriptionBundlingDTO;
import com.et.service.dto.SubscriptionBundlingResponse;
import com.et.service.dto.SubscriptionBundlingResponseV3;
import com.et.service.dto.TieredDiscountDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.SubscriptionBundling}.
 */
public interface SubscriptionBundlingService {

    /**
     * Save a subscriptionBundling.
     *
     * @param subscriptionBundlingDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionBundlingDTO save(SubscriptionBundlingDTO subscriptionBundlingDTO);

    /**
     * Get all the subscriptionBundlings.
     *
     * @return the list of entities.
     */
    List<SubscriptionBundlingDTO> findAll();

    List<SubscriptionBundlingDTO> findAllByShop(String shop);


    /**
     * Get the "id" subscriptionBundling.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionBundlingDTO> findOne(Long id);

    Optional<SubscriptionBundlingDTO> findOneByToken(String shop, String token);

    List<SubscriptionBundlingDTO> findByShopAndSubscriptionId(String shop, Long subscriptionId);

    List<SubscriptionBundlingDTO> findByShopAndBuildABoxType(String shop, BuildABoxType buildABoxType);

    ;

    /**
     * Delete the "id" subscriptionBundling.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    SubscriptionBundlingResponse getBundleDetails(String shop, String handle);

    SubscriptionBundlingResponseV3 getBundleDetailsV3(String shop, String handle);

    Double prepareDiscountAmount(String shop, SubscriptionBundling bundle, TieredDiscountDTO applicableDiscount, double totalCartPrice, long cartQuantity);

    void changeBuildABoxStatus(Long buildABoxId, Boolean status);
}
