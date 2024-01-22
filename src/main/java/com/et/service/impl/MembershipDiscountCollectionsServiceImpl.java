package com.et.service.impl;

import com.et.service.MembershipDiscountCollectionsService;
import com.et.domain.MembershipDiscountCollections;
import com.et.repository.MembershipDiscountCollectionsRepository;
import com.et.service.dto.MembershipDiscountCollectionsDTO;
import com.et.service.mapper.MembershipDiscountCollectionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MembershipDiscountCollections}.
 */
@Service
@Transactional
public class MembershipDiscountCollectionsServiceImpl implements MembershipDiscountCollectionsService {

    private final Logger log = LoggerFactory.getLogger(MembershipDiscountCollectionsServiceImpl.class);

    private final MembershipDiscountCollectionsRepository membershipDiscountCollectionsRepository;

    private final MembershipDiscountCollectionsMapper membershipDiscountCollectionsMapper;

    public MembershipDiscountCollectionsServiceImpl(MembershipDiscountCollectionsRepository membershipDiscountCollectionsRepository, MembershipDiscountCollectionsMapper membershipDiscountCollectionsMapper) {
        this.membershipDiscountCollectionsRepository = membershipDiscountCollectionsRepository;
        this.membershipDiscountCollectionsMapper = membershipDiscountCollectionsMapper;
    }

    @Override
    public MembershipDiscountCollectionsDTO save(MembershipDiscountCollectionsDTO membershipDiscountCollectionsDTO) {
        log.debug("Request to save MembershipDiscountCollections : {}", membershipDiscountCollectionsDTO);
        MembershipDiscountCollections membershipDiscountCollections = membershipDiscountCollectionsMapper.toEntity(membershipDiscountCollectionsDTO);
        membershipDiscountCollections = membershipDiscountCollectionsRepository.save(membershipDiscountCollections);
        return membershipDiscountCollectionsMapper.toDto(membershipDiscountCollections);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipDiscountCollectionsDTO> findAll() {
        log.debug("Request to get all MembershipDiscountCollections");
        return membershipDiscountCollectionsRepository.findAll().stream()
            .map(membershipDiscountCollectionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipDiscountCollectionsDTO> findOne(Long id) {
        log.debug("Request to get MembershipDiscountCollections : {}", id);
        return membershipDiscountCollectionsRepository.findById(id)
            .map(membershipDiscountCollectionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipDiscountCollections : {}", id);
        membershipDiscountCollectionsRepository.deleteById(id);
    }
}
