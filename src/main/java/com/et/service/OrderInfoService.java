package com.et.service;

import com.et.service.dto.OrderInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.OrderInfo}.
 */
public interface OrderInfoService {

    /**
     * Save a orderInfo.
     *
     * @param orderInfoDTO the entity to save.
     * @return the persisted entity.
     */
    OrderInfoDTO save(OrderInfoDTO orderInfoDTO);

    /**
     * Get all the orderInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderInfoDTO> findAll(Pageable pageable);


    @Transactional(readOnly = true)
    Page<OrderInfoDTO> findAllByShop(String shop, Pageable pageable);

    /**
     * Get the "id" orderInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderInfoDTO> findOne(Long id);

    @Transactional(readOnly = true)
    Optional<OrderInfoDTO> findByShopAndOrderId(String shop, Long orderId);

    /**
     * Delete the "id" orderInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteByShopAndOrderId(String shop, Long orderId);

    List<OrderInfoDTO> findByShopAndOrderIdIn(String shop, List<Long> orderIdList);

    void saveAll(List<OrderInfoDTO> orderInfoDTOList);
}
