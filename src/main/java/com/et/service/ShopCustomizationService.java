package com.et.service;

import com.et.domain.enumeration.CustomizationCategory;
import com.et.pojo.ShopCustomizationInfo;
import com.et.pojo.UpdateShopCustomizationRequest;
import com.et.service.dto.ShopCustomizationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ShopCustomization}.
 */
public interface ShopCustomizationService {

    /**
     * Save a shopCustomization.
     *
     * @param shopCustomizationDTO the entity to save.
     * @return the persisted entity.
     */
    ShopCustomizationDTO save(ShopCustomizationDTO shopCustomizationDTO);

    /**
     * Get all the shopCustomizations.
     *
     * @return the list of entities.
     */
    List<ShopCustomizationDTO> findAll();


    /**
     * Get the "id" shopCustomization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShopCustomizationDTO> findOne(Long id);

    /**
     * Delete the "id" shopCustomization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ShopCustomizationDTO> findByShopAndLabelId(String shop, Long labelId);

    List<ShopCustomizationDTO> findByShop(String shop);

    List<ShopCustomizationInfo> getShopCustomizationInfo(String shop, CustomizationCategory category);

    List<ShopCustomizationDTO> saveAll(List<ShopCustomizationDTO> shopCustomizationDTOList);

    void updateShopCustomizationData(List<UpdateShopCustomizationRequest> shopCustomizationRequests, String shop);

    List<String> getShopCustomizationCss(String shop, CustomizationCategory category);
}
