package com.et.service;

import com.et.domain.ProductInfo;
import com.et.pojo.ProductFilterDTO;
import com.et.service.dto.CollectionVM;
import com.et.service.dto.ProductInfoDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link com.et.domain.ProductInfo}.
 */
public interface ProductInfoService {

    /**
     * Save a productInfo.
     *
     * @param productInfoDTO the entity to save.
     * @return the persisted entity.
     */
    ProductInfoDTO save(ProductInfoDTO productInfoDTO);

    /**
     * Get all the productInfos.
     *
     * @return the list of entities.
     */
    List<ProductInfoDTO> findAll();


    /**
     * Get the "id" productInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductInfoDTO> findOne(Long id);

    /**
     * Delete the "id" productInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void createOrUpdateProductByIds(String shop, Set<Long> productIds);

    void syncAllProductInfo();

    List<ProductInfo> findByShopAndProductIds(String shop, Set<Long> productIds);

    ProductFilterDTO getAllProductFilterData(String shop) throws Exception;

    List<CollectionVM> getAllCollectionList(String shop, String searchText);

}
