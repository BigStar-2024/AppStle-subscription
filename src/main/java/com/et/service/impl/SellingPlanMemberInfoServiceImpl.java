package com.et.service.impl;

import com.et.domain.SellingPlanMemberInfo;
import com.et.repository.SellingPlanMemberInfoRepository;
import com.et.repository.SubscriptionGroupPlanRepository;
import com.et.service.SellingPlanMemberInfoService;
import com.et.service.dto.FrequencyInfoDTO;
import com.et.service.dto.SellingPlanMemberInfoDTO;
import com.et.service.dto.SubscriptionGroupPlanDTO;
import com.et.service.mapper.SellingPlanMemberInfoMapper;
import com.et.service.mapper.SubscriptionGroupPlanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SellingPlanMemberInfo}.
 */
@Service
@Transactional
public class SellingPlanMemberInfoServiceImpl implements SellingPlanMemberInfoService {

    private final Logger log = LoggerFactory.getLogger(SellingPlanMemberInfoServiceImpl.class);

    private final SellingPlanMemberInfoRepository sellingPlanMemberInfoRepository;

    private final SellingPlanMemberInfoMapper sellingPlanMemberInfoMapper;

    @Autowired
    private SubscriptionGroupPlanMapper subscriptionGroupPlanMapper;

    @Autowired
    private SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;

    public SellingPlanMemberInfoServiceImpl(SellingPlanMemberInfoRepository sellingPlanMemberInfoRepository, SellingPlanMemberInfoMapper sellingPlanMemberInfoMapper) {
        this.sellingPlanMemberInfoRepository = sellingPlanMemberInfoRepository;
        this.sellingPlanMemberInfoMapper = sellingPlanMemberInfoMapper;
    }

    @Override
    public SellingPlanMemberInfoDTO save(SellingPlanMemberInfoDTO sellingPlanMemberInfoDTO) {
        log.debug("Request to save SellingPlanMemberInfo : {}", sellingPlanMemberInfoDTO);
        SellingPlanMemberInfo sellingPlanMemberInfo = sellingPlanMemberInfoMapper.toEntity(sellingPlanMemberInfoDTO);
        sellingPlanMemberInfo = sellingPlanMemberInfoRepository.save(sellingPlanMemberInfo);
        return sellingPlanMemberInfoMapper.toDto(sellingPlanMemberInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellingPlanMemberInfoDTO> findAll() {
        log.debug("Request to get all SellingPlanMemberInfos");
        return sellingPlanMemberInfoRepository.findAll().stream()
            .map(sellingPlanMemberInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SellingPlanMemberInfoDTO> findOne(Long id) {
        log.debug("Request to get SellingPlanMemberInfo : {}", id);
        return sellingPlanMemberInfoRepository.findById(id)
            .map(sellingPlanMemberInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SellingPlanMemberInfo : {}", id);
        sellingPlanMemberInfoRepository.deleteById(id);
    }

    @Override
    public Optional<SellingPlanMemberInfoDTO> findOneBySellingPlanId(String shop, Long sellingPlanId) {
        return sellingPlanMemberInfoRepository.findByShopAndSellingPlanId(shop, sellingPlanId)
            .map(sellingPlanMemberInfoMapper::toDto);
    }

    @Override
    public List<SellingPlanMemberInfoDTO> findByShop(String shop) {
        return sellingPlanMemberInfoRepository.findByShop(shop).stream()
            .map(sellingPlanMemberInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
