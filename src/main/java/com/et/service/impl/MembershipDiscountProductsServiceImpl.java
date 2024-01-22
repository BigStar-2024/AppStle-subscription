package com.et.service.impl;

import com.et.service.MembershipDiscountProductsService;
import com.et.domain.MembershipDiscountProducts;
import com.et.repository.MembershipDiscountProductsRepository;
import com.et.service.dto.MembershipDiscountProductsDTO;
import com.et.service.mapper.MembershipDiscountProductsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MembershipDiscountProducts}.
 */
@Service
@Transactional
public class MembershipDiscountProductsServiceImpl implements MembershipDiscountProductsService {

    private final Logger log = LoggerFactory.getLogger(MembershipDiscountProductsServiceImpl.class);

    private final MembershipDiscountProductsRepository membershipDiscountProductsRepository;

    private final MembershipDiscountProductsMapper membershipDiscountProductsMapper;

    public MembershipDiscountProductsServiceImpl(MembershipDiscountProductsRepository membershipDiscountProductsRepository, MembershipDiscountProductsMapper membershipDiscountProductsMapper) {
        this.membershipDiscountProductsRepository = membershipDiscountProductsRepository;
        this.membershipDiscountProductsMapper = membershipDiscountProductsMapper;
    }

    @Override
    public MembershipDiscountProductsDTO save(MembershipDiscountProductsDTO membershipDiscountProductsDTO) {
        log.debug("Request to save MembershipDiscountProducts : {}", membershipDiscountProductsDTO);
        MembershipDiscountProducts membershipDiscountProducts = membershipDiscountProductsMapper.toEntity(membershipDiscountProductsDTO);
        membershipDiscountProducts = membershipDiscountProductsRepository.save(membershipDiscountProducts);
        return membershipDiscountProductsMapper.toDto(membershipDiscountProducts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipDiscountProductsDTO> findAll() {
        log.debug("Request to get all MembershipDiscountProducts");
        return membershipDiscountProductsRepository.findAll().stream()
            .map(membershipDiscountProductsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipDiscountProductsDTO> findOne(Long id) {
        log.debug("Request to get MembershipDiscountProducts : {}", id);
        return membershipDiscountProductsRepository.findById(id)
            .map(membershipDiscountProductsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipDiscountProducts : {}", id);
        membershipDiscountProductsRepository.deleteById(id);
    }
}
