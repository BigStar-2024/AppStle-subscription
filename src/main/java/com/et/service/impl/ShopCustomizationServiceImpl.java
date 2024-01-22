package com.et.service.impl;

import com.et.domain.enumeration.CustomizationCategory;
import com.et.liquid.LiquidUtils;
import com.et.pojo.CustomizationModel;
import com.et.pojo.ShopCustomizationInfo;
import com.et.pojo.UpdateShopCustomizationRequest;
import com.et.service.CustomizationService;
import com.et.service.ShopCustomizationService;
import com.et.domain.ShopCustomization;
import com.et.repository.ShopCustomizationRepository;
import com.et.service.dto.CustomizationDTO;
import com.et.service.dto.ShopCustomizationDTO;
import com.et.service.mapper.ShopCustomizationMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShopCustomization}.
 */
@Service
@Transactional
public class ShopCustomizationServiceImpl implements ShopCustomizationService {

    private final Logger log = LoggerFactory.getLogger(ShopCustomizationServiceImpl.class);

    private final ShopCustomizationRepository shopCustomizationRepository;

    private final ShopCustomizationMapper shopCustomizationMapper;

    @Autowired
    private CustomizationService customizationService;

    @Autowired
    private LiquidUtils liquidUtils;

    public ShopCustomizationServiceImpl(ShopCustomizationRepository shopCustomizationRepository, ShopCustomizationMapper shopCustomizationMapper) {
        this.shopCustomizationRepository = shopCustomizationRepository;
        this.shopCustomizationMapper = shopCustomizationMapper;
    }

    /**
     * Save a shopCustomization.
     *
     * @param shopCustomizationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ShopCustomizationDTO save(ShopCustomizationDTO shopCustomizationDTO) {
        log.debug("Request to save ShopCustomization : {}", shopCustomizationDTO);
        ShopCustomization shopCustomization = shopCustomizationMapper.toEntity(shopCustomizationDTO);
        shopCustomization = shopCustomizationRepository.save(shopCustomization);
        return shopCustomizationMapper.toDto(shopCustomization);
    }

    /**
     * Save All a shopCustomization.
     *
     * @param shopCustomizationDTOList the entity to save All.
     * @return the persisted entity.
     */
    @Override
    public List<ShopCustomizationDTO> saveAll(List<ShopCustomizationDTO> shopCustomizationDTOList) {
        log.debug("Request to save ShopCustomization : {}", shopCustomizationDTOList);
        List<ShopCustomization> shopCustomizationList = shopCustomizationMapper.toEntity(shopCustomizationDTOList);
        shopCustomizationList = shopCustomizationRepository.saveAll(shopCustomizationList);
        return shopCustomizationMapper.toDto(shopCustomizationList);
    }

    @Override
    public void updateShopCustomizationData(List<UpdateShopCustomizationRequest> shopCustomizationRequests, String shop) {
        try {
            if (ObjectUtils.isNotEmpty(shopCustomizationRequests)) {
                List<ShopCustomizationDTO> shopCustomizationDTOList = new ArrayList<>();
                for (UpdateShopCustomizationRequest request : shopCustomizationRequests) {
                    if (ObjectUtils.isNotEmpty(request)) {
                        ShopCustomizationDTO shopCustomizationDTO = new ShopCustomizationDTO();
                        if (request.getId() != null) {
                            shopCustomizationDTO.setId(request.getId());
                        } else {
                            Optional<ShopCustomizationDTO> shopCustomization = findByShopAndLabelId(shop, request.getLabelId());
                            shopCustomization.ifPresent(customization -> shopCustomizationDTO.setId(customization.getId()));
                        }
                        shopCustomizationDTO.setLabelId(request.getLabelId());
                        shopCustomizationDTO.setValue(request.getValue());
                        shopCustomizationDTO.setShop(shop);
                        shopCustomizationDTOList.add(shopCustomizationDTO);
                    }
                }
                saveAll(shopCustomizationDTOList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all the shopCustomizations.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShopCustomizationDTO> findAll() {
        log.debug("Request to get all ShopCustomizations");
        return shopCustomizationRepository.findAll().stream()
            .map(shopCustomizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shopCustomization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ShopCustomizationDTO> findOne(Long id) {
        log.debug("Request to get ShopCustomization : {}", id);
        return shopCustomizationRepository.findById(id)
            .map(shopCustomizationMapper::toDto);
    }

    /**
     * Delete the shopCustomization by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShopCustomization : {}", id);
        shopCustomizationRepository.deleteById(id);
    }

    @Override
    public Optional<ShopCustomizationDTO> findByShopAndLabelId(String shop, Long labelId) {
        return shopCustomizationRepository.findByShopAndLabelId(shop, labelId)
            .map(shopCustomizationMapper::toDto);
    }

    @Override
    public List<ShopCustomizationDTO> findByShop(String shop) {
        return shopCustomizationRepository.findByShop(shop).stream()
            .map(shopCustomizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ShopCustomizationInfo> getShopCustomizationInfo(String shop, CustomizationCategory category) {
        return shopCustomizationRepository.getShopCustomizationInfo(shop, category.toString());
    }

    @Override
    public List<String> getShopCustomizationCss(String shop, CustomizationCategory category) {
        List<ShopCustomizationDTO> shopCustomizationDTOList = findByShop(shop);
        List<Long> labelIds = shopCustomizationDTOList.stream().map(ShopCustomizationDTO::getLabelId).collect(Collectors.toList());

        List<CustomizationDTO> customizationDTOList = customizationService.findAllById(labelIds);
        Map<Long, CustomizationDTO> customizationByLabelId = customizationDTOList.stream().filter(c -> c.getCategory().equals(category)).collect(Collectors.toMap(CustomizationDTO::getId, l -> l));
        List<String> cssList = new ArrayList<>();

        for (ShopCustomizationDTO shopCustomizationDTO : shopCustomizationDTOList) {
            CustomizationDTO customizationDTO = customizationByLabelId.getOrDefault(shopCustomizationDTO.getLabelId(), null);
            if (customizationDTO == null) {
                continue;
            }
            if (StringUtils.isNotBlank(shopCustomizationDTO.getValue())) {
                CustomizationModel customizationModel = new CustomizationModel();
                customizationModel.setValue(shopCustomizationDTO.getValue());
                String cssValue = liquidUtils.getValue(customizationModel, customizationDTO.getCss());
                cssList.add(cssValue);
            }
        }

        return cssList;
    }

}
