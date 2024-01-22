package com.et.service.impl;

import com.et.domain.SubscriptionBillingAttempt;
import com.et.domain.enumeration.*;
import com.et.repository.SubscriptionBillingAttemptRepository;
import com.et.service.SubscriptionContractOneOffService;
import com.et.domain.SubscriptionContractOneOff;
import com.et.repository.SubscriptionContractOneOffRepository;
import com.et.service.dto.SubscriptionContractOneOffDTO;
import com.et.service.mapper.SubscriptionContractOneOffMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionContractOneOff}.
 */
@Service
@Transactional
public class SubscriptionContractOneOffServiceImpl implements SubscriptionContractOneOffService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionContractOneOffServiceImpl.class);

    private static final String ENTITY_NAME = "subscriptionContractOneOff";

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;

    private final SubscriptionContractOneOffRepository subscriptionContractOneOffRepository;

    private final SubscriptionContractOneOffMapper subscriptionContractOneOffMapper;

    public SubscriptionContractOneOffServiceImpl(SubscriptionContractOneOffRepository subscriptionContractOneOffRepository, SubscriptionContractOneOffMapper subscriptionContractOneOffMapper) {
        this.subscriptionContractOneOffRepository = subscriptionContractOneOffRepository;
        this.subscriptionContractOneOffMapper = subscriptionContractOneOffMapper;
    }

    @Override
    public SubscriptionContractOneOffDTO save(SubscriptionContractOneOffDTO subscriptionContractOneOffDTO) {
        log.debug("Request to save SubscriptionContractOneOff : {}", subscriptionContractOneOffDTO);
        SubscriptionContractOneOff subscriptionContractOneOff = subscriptionContractOneOffMapper.toEntity(subscriptionContractOneOffDTO);
        subscriptionContractOneOff = subscriptionContractOneOffRepository.save(subscriptionContractOneOff);
        return subscriptionContractOneOffMapper.toDto(subscriptionContractOneOff);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionContractOneOffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionContractOneOffs");
        return subscriptionContractOneOffRepository.findAll(pageable)
            .map(subscriptionContractOneOffMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionContractOneOffDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionContractOneOff : {}", id);
        return subscriptionContractOneOffRepository.findById(id)
            .map(subscriptionContractOneOffMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionContractOneOff : {}", id);
        subscriptionContractOneOffRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionContractOneOffDTO> findByShopAndSubscriptionContractId(String shop, Long subscriptionContractId) {
        log.debug("Request to get all SubscriptionContractOneOffs");
        return subscriptionContractOneOffRepository.findByShopAndSubscriptionContractId(shop, subscriptionContractId)
            .stream().map(t -> subscriptionContractOneOffMapper.toDto(t)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionContractOneOffDTO> findByShopAndSubscriptionContractIdAndBillingAttemptId(String shop, Long subscriptionContractId, Long billingAttemptId) {
        log.debug("Request to get all SubscriptionContractOneOffs");
        return subscriptionContractOneOffRepository.findByShopAndSubscriptionContractIdAndBillingAttemptId(shop, subscriptionContractId, billingAttemptId)
            .stream().map(subscriptionContractOneOffMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteByShopAndContractIdAndBillingAttemptIdAndVariantId(String shop, Long contractId, Long billingAttemptId, Long variantId) {
        subscriptionContractOneOffRepository.deleteByShopAndSubscriptionContractIdAndBillingAttemptIdAndVariantId(shop, contractId, billingAttemptId, variantId);
    }

    @Override
    public SubscriptionContractOneOffDTO subscriptionContractUpdateOneOffQuantity(String shop, Long contractId, Long billingAttemptId, Long variantId, Integer quantity, ActivityLogEventSource eventSource) throws JsonProcessingException {
        List<SubscriptionContractOneOffDTO> subscriptionContractsList = findByShopAndSubscriptionContractId(shop, contractId);

        Optional<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOOptional = subscriptionContractsList.stream().filter(sc -> sc.getBillingAttemptId().equals(billingAttemptId) && sc.getVariantId().equals(variantId)).findFirst();

        if (subscriptionContractOneOffDTOOptional.isEmpty()) {
            throw new BadRequestAlertException("One time product variant not found", ENTITY_NAME, "");
        }

        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = subscriptionContractOneOffDTOOptional.get();

        Integer oldQuantity = subscriptionContractOneOffDTO.getQuantity();
        subscriptionContractOneOffDTO.setQuantity(quantity);
        save(subscriptionContractOneOffDTO);

        Map<String, Object> activityLogMap = new HashMap<>();
        activityLogMap.put("variantId", variantId);
        activityLogMap.put("oldQuantity", oldQuantity);
        activityLogMap.put("newQuantity", quantity);

        commonUtils.writeActivityLog(shop, contractId, ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, eventSource, ActivityLogEventType.ONE_TIME_PURCHASE_PRODUCT_UPDATE, ActivityLogStatus.SUCCESS, activityLogMap);

        return subscriptionContractOneOffDTO;
    }



    @Override
    public List<SubscriptionContractOneOffDTO> getOneOffForContractNextOrder(String shop, Long contractId) {

        Optional<SubscriptionBillingAttempt> subscriptionBillingAttempt = subscriptionBillingAttemptRepository
            .findFirstByContractIdAndStatusEqualsOrderByBillingDateAsc(contractId, BillingAttemptStatus.QUEUED);

        Optional<Long> billingAttemptId = subscriptionBillingAttempt.map(SubscriptionBillingAttempt::getId);

        List<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOList = new ArrayList<>();
        if (billingAttemptId.isPresent()) {
            subscriptionContractOneOffDTOList = findByShopAndSubscriptionContractIdAndBillingAttemptId(shop, contractId, billingAttemptId.get());
        }
        return subscriptionContractOneOffDTOList;
    }
}
