package com.et.service.impl;

import com.et.service.OrderInfoService;
import com.et.domain.OrderInfo;
import com.et.repository.OrderInfoRepository;
import com.et.service.dto.OrderInfoDTO;
import com.et.service.mapper.OrderInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link OrderInfo}.
 */
@Service
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService {

    private final Logger log = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    private final OrderInfoRepository orderInfoRepository;

    private final OrderInfoMapper orderInfoMapper;

    public OrderInfoServiceImpl(OrderInfoRepository orderInfoRepository, OrderInfoMapper orderInfoMapper) {
        this.orderInfoRepository = orderInfoRepository;
        this.orderInfoMapper = orderInfoMapper;
    }

    /**
     * Save a orderInfo.
     *
     * @param orderInfoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OrderInfoDTO save(OrderInfoDTO orderInfoDTO) {
        log.debug("Request to save OrderInfo : {}", orderInfoDTO);
        OrderInfo orderInfo = orderInfoMapper.toEntity(orderInfoDTO);
        orderInfo = orderInfoRepository.save(orderInfo);
        return orderInfoMapper.toDto(orderInfo);
    }

    /**
     * Get all the orderInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderInfos");
        return orderInfoRepository.findAll(pageable)
            .map(orderInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderInfoDTO> findAllByShop(String shop, Pageable pageable) {
        log.debug("Request to get all OrderInfos by shop {}", shop);
        return orderInfoRepository.findAllByShop(shop, pageable)
            .map(orderInfoMapper::toDto);
    }


    /**
     * Get one orderInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderInfoDTO> findOne(Long id) {
        log.debug("Request to get OrderInfo : {}", id);
        return orderInfoRepository.findById(id)
            .map(orderInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderInfoDTO> findByShopAndOrderId(String shop, Long orderId) {
        log.debug("Request to get OrderInfo : {}", orderId);
        List<OrderInfoDTO> orderInfoDTOList = findByShopAndOrderIdIn(shop, List.of(orderId));
        return Optional.ofNullable(orderInfoDTOList.isEmpty() ? null : orderInfoDTOList.get(0));
    }

    /**
     * Delete the orderInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderInfo : {}", id);
        orderInfoRepository.deleteById(id);
    }

    @Override
    public void deleteByShopAndOrderId(String shop, Long orderId) {
        log.debug("Request to delete OrderInfo : {}", orderId);
        orderInfoRepository.deleteByShopAndOrderId(shop, orderId);
    }

    @Override
    public List<OrderInfoDTO> findByShopAndOrderIdIn(String shop, List<Long> orderIdList) {
        log.debug("Request to get all OrderInfo by shop: {} and order ids: {}", shop, orderIdList);
        return orderInfoMapper.toDto(orderInfoRepository.findByShopAndOrderIdIn(shop, orderIdList));
    }

    @Override
    public void saveAll(List<OrderInfoDTO> orderInfoDTOList) {
        log.debug("Request to save OrderInfo List");
        List<OrderInfo> orderInfoList = orderInfoMapper.toEntity(orderInfoDTOList);
        orderInfoRepository.saveAll(orderInfoList);
    }
}
