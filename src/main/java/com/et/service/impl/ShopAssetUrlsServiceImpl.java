package com.et.service.impl;

import com.et.service.ShopAssetUrlsService;
import com.et.domain.ShopAssetUrls;
import com.et.repository.ShopAssetUrlsRepository;
import com.et.service.dto.ShopAssetUrlsDTO;
import com.et.service.mapper.ShopAssetUrlsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShopAssetUrls}.
 */
@Service
@Transactional
public class ShopAssetUrlsServiceImpl implements ShopAssetUrlsService {

    private final Logger log = LoggerFactory.getLogger(ShopAssetUrlsServiceImpl.class);

    private final ShopAssetUrlsRepository shopAssetUrlsRepository;

    private final ShopAssetUrlsMapper shopAssetUrlsMapper;

    public ShopAssetUrlsServiceImpl(ShopAssetUrlsRepository shopAssetUrlsRepository, ShopAssetUrlsMapper shopAssetUrlsMapper) {
        this.shopAssetUrlsRepository = shopAssetUrlsRepository;
        this.shopAssetUrlsMapper = shopAssetUrlsMapper;
    }

    /**
     * Save a shopAssetUrls.
     *
     * @param shopAssetUrlsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ShopAssetUrlsDTO save(ShopAssetUrlsDTO shopAssetUrlsDTO) {
        log.debug("Request to save ShopAssetUrls : {}", shopAssetUrlsDTO);
        ShopAssetUrls shopAssetUrls = shopAssetUrlsMapper.toEntity(shopAssetUrlsDTO);
        shopAssetUrls = shopAssetUrlsRepository.save(shopAssetUrls);
        return shopAssetUrlsMapper.toDto(shopAssetUrls);
    }

    /**
     * Get all the shopAssetUrls.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShopAssetUrlsDTO> findAll() {
        log.debug("Request to get all ShopAssetUrls");
        return shopAssetUrlsRepository.findAll().stream()
            .map(shopAssetUrlsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shopAssetUrls by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ShopAssetUrlsDTO> findOne(Long id) {
        log.debug("Request to get ShopAssetUrls : {}", id);
        return shopAssetUrlsRepository.findById(id)
            .map(shopAssetUrlsMapper::toDto);
    }

    /**
     * Delete the shopAssetUrls by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShopAssetUrls : {}", id);
        shopAssetUrlsRepository.deleteById(id);
    }

    @Override
    public Optional<ShopAssetUrlsDTO> findByShop(String shop) {
        return shopAssetUrlsRepository.findByShop(shop)
            .map(shopAssetUrlsMapper::toDto);
    }

    @Override
    public void deleteByShop(String shop) {
        shopAssetUrlsRepository.deleteByShop(shop);
    }
}
