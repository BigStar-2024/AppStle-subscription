package com.et.service.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.et.domain.ShopLabel;
import com.et.pojo.KeyLabelInfo;
import com.et.pojo.LabelValueInfo;
import com.et.repository.ShopLabelRepository;
import com.et.service.ShopLabelService;
import com.et.service.dto.ShopLabelDTO;
import com.et.service.mapper.ShopLabelMapper;
import com.et.utils.CommonUtils;
import com.et.utils.SubscribeItScriptUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShopLabel}.
 */
@Service
@Transactional
public class ShopLabelServiceImpl implements ShopLabelService {

    @Lazy
    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Value("classpath:templates/default-label.properties")
    Resource defaultLabelProperties;

    private final Logger log = LoggerFactory.getLogger(ShopLabelServiceImpl.class);

    private static final String ENTITY_NAME = "shopLabel";

    private final ShopLabelRepository shopLabelRepository;

    private final ShopLabelMapper shopLabelMapper;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ShopLabelServiceImpl(ShopLabelRepository shopLabelRepository, ShopLabelMapper shopLabelMapper) {
        this.shopLabelRepository = shopLabelRepository;
        this.shopLabelMapper = shopLabelMapper;
    }

    @Override
    public ShopLabelDTO save(ShopLabelDTO shopLabelDTO) {
        log.debug("Request to save ShopLabel : {}", shopLabelDTO);
        ShopLabel shopLabel = shopLabelMapper.toEntity(shopLabelDTO);
        shopLabel = shopLabelRepository.save(shopLabel);
        return shopLabelMapper.toDto(shopLabel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopLabelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShopLabels");
        return shopLabelRepository.findAll(pageable)
            .map(shopLabelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopLabelDTO> findAllByShop(String shop, Pageable pageable) {
        log.debug("Request to get all ShopLabels");
        return shopLabelRepository.findAllByShop(shop, pageable)
            .map(shopLabelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopLabelDTO> findAllByShop(String shop) {
        log.debug("Request to get all ShopLabels");
        return shopLabelRepository.findAllByShop(shop)
            .stream().map(shopLabelMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShopLabelDTO> findOne(Long id) {
        log.debug("Request to get ShopLabel : {}", id);
        return shopLabelRepository.findById(id)
            .map(shopLabelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShopLabelDTO> findByIdAndShop(Long id, String shop) {
        log.debug("Request to get ShopLabel for id: {}, shop: {}", id, shop);
        return shopLabelRepository.findByIdAndShop(id, shop)
            .map(shopLabelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShopLabelDTO> findFirstByShop(String shop) {
        log.debug("Request to get ShopLabel for shop: {}", shop);
        return shopLabelRepository.findFirstByShop(shop)
            .map(shopLabelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShopLabel : {}", id);
        shopLabelRepository.deleteById(id);
    }

    @Override
    public void deleteByIdAndShop(Long id, String shop) {
        log.debug("Request to delete ShopLabel for id: {}, shop: {}", id, shop);
        Optional<ShopLabelDTO> shopLabelDTO = findByIdAndShop(id, shop);
        if (shopLabelDTO.isPresent()) {
            delete(id);
        }
    }


    private void saveAll(List<ShopLabelDTO> shopLabelDTOList) {
        log.debug("Request to save ShopLabel List");
        List<ShopLabel> shopLabelList = shopLabelMapper.toEntity(shopLabelDTOList);
        shopLabelRepository.saveAll(shopLabelList);
    }

    @Override
    public void updateShopLabels(String shop, List<ShopLabelDTO> shopLabelDTOList) throws Exception {
        if (CollectionUtils.isEmpty(shopLabelDTOList)) {
            throw new BadRequestAlertException("Request body cannot be empty", ENTITY_NAME, "");
        }

        for (ShopLabelDTO shopLabelDTO : shopLabelDTOList) {
            if (Objects.isNull(shopLabelDTO.getId())) {
                throw new BadRequestAlertException("Id cannot be null", ENTITY_NAME, "idnull");
            }
            shopLabelDTO.setShop(shop);
        }

        this.saveAll(shopLabelDTOList);

        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
    }

    @Override
    public void addDefaultLabelsForShop(String shop) throws Exception {
        Optional<ShopLabel> shopLabel = shopLabelRepository.findFirstByShop(shop);
        if (shopLabel.isPresent()) {
            return;
        }

        Map<String, LabelValueInfo> defaultLabels = getDefaultLabels();

        if (!CollectionUtils.isEmpty(defaultLabels)) {
            ShopLabelDTO shopLabelDTO = new ShopLabelDTO();
            shopLabelDTO.setShop(shop);
            shopLabelDTO.setLabels(OBJECT_MAPPER.writeValueAsString(defaultLabels));
            this.save(shopLabelDTO);
        }
    }

    @Override
    public void addKeyForShop(String shop, String key, LabelValueInfo labelValueInfo) throws Exception {
        Map<String, LabelValueInfo> labels = getShopLabels(shop);
        Map<String, LabelValueInfo> defaultLabels = getDefaultLabels();

        if (!defaultLabels.containsKey(key)) {
            return;
        }

        labels.put(key, labelValueInfo);

        updateShopLabels(shop, labels);
    }

    @Override
    public void addKeysForShop(String shop, List<KeyLabelInfo> keyLabelInfo) throws Exception {

        Map<String, LabelValueInfo> labels = getShopLabels(shop);
        Map<String, LabelValueInfo> defaultLabels = getDefaultLabels();

        for (KeyLabelInfo labelInfo : keyLabelInfo) {
            String key = labelInfo.getKey();
            LabelValueInfo labelValueInfo = labelInfo.getLabelValueInfo();
            if (!defaultLabels.containsKey(key)) {
                continue;
            }

            labels.put(key, labelValueInfo);
        }

        updateShopLabels(shop, labels);
    }

    @Override
    public void addKey(String key) throws Exception {
        Map<String, LabelValueInfo> defaultLabels = getDefaultLabels();

        key = key.toLowerCase().trim();
        if (BooleanUtils.isFalse(defaultLabels.containsKey(key))) {
            throw new Exception("Key not present in default labels");
        }

        LabelValueInfo defaultLabelValueInfo = defaultLabels.get(key);
        //String labelDefaultValue = (String) labelValueInfo;

        List<String> shops = shopLabelRepository.findAllShopsWithMissingLabel(key);
        if (CollectionUtils.isEmpty(shops)) {
            throw new Exception("No Shop found that are missing label for key: " + key);
        }

        for (String shop : shops) {
            addKeyForShop(shop, key, defaultLabelValueInfo);
        }
    }

    @Override
    public void removeKeyForShop(String shop, String key) throws JsonProcessingException {
        Map<String, LabelValueInfo> labels = getShopLabels(shop);
        labels.remove(key);

        updateShopLabels(shop, labels);
    }

    @Override
    public void removeKey(String key) throws Exception {
        Map<String, LabelValueInfo> defaultLabels = getDefaultLabels();

        key = key.toLowerCase().trim();
        if (BooleanUtils.isTrue(defaultLabels.containsKey(key))) {
            throw new Exception("Key still present in label properties");
        }

        List<String> shops = shopLabelRepository.findAllShopsWithLabel(key);
        if (CollectionUtils.isEmpty(shops)) {
            throw new Exception("No Shop found that have label for key: " + key);
        }

        for (String shop : shops) {
            removeKeyForShop(shop, key);
        }
    }


    @Override
    public Map<String, LabelValueInfo> getDefaultLabels() throws Exception {
        HashMap<String, AttributeValue> map = new HashMap<>();
        map.put("key", new AttributeValue("default-labels"));
        GetItemResult itemResult = amazonDynamoDB.getItem(new GetItemRequest("shop_labels", map));
        String labelsJson = itemResult.getItem().get("value").getS();
        return CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<Map<String, LabelValueInfo>>() {
        }, labelsJson);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, LabelValueInfo> getShopLabels(String shop) {
        log.debug("Request to get Shop Labels for shop: {}", shop);

        Map<String, LabelValueInfo> labels = new LinkedHashMap<>();

        Optional<ShopLabelDTO> shopLabelDTO = findFirstByShop(shop);

        if (shopLabelDTO.isPresent() && StringUtils.isNotBlank(shopLabelDTO.get().getLabels())) {
            labels = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
            }, shopLabelDTO.get().getLabels());
        }

        return labels;
    }

    @Override
    public Map<String, LabelValueInfo> updateShopLabels(String shop, Map<String, LabelValueInfo> labels) throws JsonProcessingException {
        log.debug("Request to get Shop Labels for shop: {}", shop);

        ShopLabelDTO shopLabelDTO = findFirstByShop(shop).orElse(new ShopLabelDTO());
        shopLabelDTO.setShop(shop);
        shopLabelDTO.setLabels(OBJECT_MAPPER.writeValueAsString(labels));
        save(shopLabelDTO);

        return labels;
    }
}
