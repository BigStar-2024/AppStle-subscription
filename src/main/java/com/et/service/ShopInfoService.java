package com.et.service;

import com.et.pojo.ExportMerchantsResponse;
import com.et.service.dto.ShopInfoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ShopInfo}.
 */
public interface ShopInfoService {

    /**
     * Save a shopInfo.
     *
     * @param shopInfoDTO the entity to save.
     * @return the persisted entity.
     */
    ShopInfoDTO save(ShopInfoDTO shopInfoDTO);

    /**
     * Get all the shopInfos.
     *
     * @return the list of entities.
     */
    List<ShopInfoDTO> findAll();


    /**
     * Get the "id" shopInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShopInfoDTO> findOne(Long id);

    /**
     * Delete the "id" shopInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ShopInfoDTO> findByShop(String shop);

    Optional<ShopInfoDTO> findByApiKey(String apiKey);

    List<ShopInfoDTO> findAllByCountryCodeIsNullAndCountryNameIsNull();

    void deleteByShop(String shop);

    List<ShopInfoDTO> findAllByShop(String shop);

    List<ExportMerchantsResponse> findAllShopWhichUseBuildABoxFeature(Pageable pageable);

    List<ExportMerchantsResponse> findAllShopWhichUseAppstleMenu(Pageable pageable);
}
