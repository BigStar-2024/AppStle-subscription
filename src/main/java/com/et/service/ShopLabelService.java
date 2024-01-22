package com.et.service;

import com.et.pojo.KeyLabelInfo;
import com.et.pojo.LabelValueInfo;
import com.et.service.dto.ShopLabelDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ShopLabel}.
 */
public interface ShopLabelService {

    /**
     * Save a shopLabel.
     *
     * @param shopLabelDTO the entity to save.
     * @return the persisted entity.
     */
    ShopLabelDTO save(ShopLabelDTO shopLabelDTO);

    /**
     * Get all the shopLabels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShopLabelDTO> findAll(Pageable pageable);


    @Transactional(readOnly = true)
    Page<ShopLabelDTO> findAllByShop(String shop, Pageable pageable);

    @Transactional(readOnly = true)
    List<ShopLabelDTO> findAllByShop(String shop);

    /**
     * Get the "id" shopLabel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShopLabelDTO> findOne(Long id);

    @Transactional(readOnly = true)
    Optional<ShopLabelDTO> findByIdAndShop(Long id, String shop);

    @Transactional(readOnly = true)
    Optional<ShopLabelDTO> findFirstByShop(String shop);

    /**
     * Delete the "id" shopLabel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteByIdAndShop(Long id, String shop);

    void updateShopLabels(String shop, List<ShopLabelDTO> shopLabelDTOList) throws Exception;

    void addDefaultLabelsForShop(String shop) throws Exception;

    void addKeyForShop(String shop, String key, LabelValueInfo labelValueInfo) throws Exception;

    void addKeysForShop(String shop, List<KeyLabelInfo> keyLabelInfo) throws Exception;

    void addKey(String key) throws Exception;

    void removeKeyForShop(String shop, String key) throws JsonProcessingException;

    void removeKey(String key) throws Exception;

    Map<String, LabelValueInfo> getDefaultLabels() throws Exception;

    @Transactional(readOnly = true)
    Map<String, LabelValueInfo> getShopLabels(String shop);

    Map<String, LabelValueInfo> updateShopLabels(String shop, Map<String, LabelValueInfo> labels) throws JsonProcessingException;
}
