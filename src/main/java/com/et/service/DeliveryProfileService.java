package com.et.service;

import com.et.service.dto.DeliveryProfileDTO;
import com.et.web.rest.vm.shippingprofile.CreateShippingProfileRequestV3;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.DeliveryProfile}.
 */
public interface DeliveryProfileService {

    /**
     * Save a deliveryProfile.
     *
     * @param deliveryProfileDTO the entity to save.
     * @return the persisted entity.
     */
    DeliveryProfileDTO save(DeliveryProfileDTO deliveryProfileDTO);

    /**
     * Get all the deliveryProfiles.
     *
     * @return the list of entities.
     */
    List<DeliveryProfileDTO> findAll();


    /**
     * Get the "id" deliveryProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeliveryProfileDTO> findOne(Long id);

    /**
     * Delete the "id" deliveryProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<DeliveryProfileDTO> findByShop(String shop);

    CreateShippingProfileRequestV3 createShippingProfileV3Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception;

    CreateShippingProfileRequestV3 createShippingProfileV4Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception;

    CreateShippingProfileRequestV3 updateShippingProfileV3Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception;

    CreateShippingProfileRequestV3 updateShippingProfileV4Info(CreateShippingProfileRequestV3 createShippingProfileRequestVM, String shop) throws Exception;

    CreateShippingProfileRequestV3 getShippingProfileDetail(String shop, String deliveryProfileId) throws Exception;

    CreateShippingProfileRequestV3 getShippingProfileDetailV3(String shop, String deliveryProfileId, Long id) throws Exception;
}
