package com.et.service.impl;

import com.et.service.MemberOnlyService;
import com.et.domain.MemberOnly;
import com.et.repository.MemberOnlyRepository;
import com.et.service.dto.MemberOnlyDTO;
import com.et.service.mapper.MemberOnlyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MemberOnly}.
 */
@Service
@Transactional
public class MemberOnlyServiceImpl implements MemberOnlyService {

    private final Logger log = LoggerFactory.getLogger(MemberOnlyServiceImpl.class);

    private final MemberOnlyRepository memberOnlyRepository;

    private final MemberOnlyMapper memberOnlyMapper;

    public MemberOnlyServiceImpl(MemberOnlyRepository memberOnlyRepository, MemberOnlyMapper memberOnlyMapper) {
        this.memberOnlyRepository = memberOnlyRepository;
        this.memberOnlyMapper = memberOnlyMapper;
    }

    @Override
    public MemberOnlyDTO save(MemberOnlyDTO memberOnlyDTO) {
        log.debug("Request to save MemberOnly : {}", memberOnlyDTO);
        MemberOnly memberOnly = memberOnlyMapper.toEntity(memberOnlyDTO);
        memberOnly = memberOnlyRepository.save(memberOnly);
        return memberOnlyMapper.toDto(memberOnly);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberOnlyDTO> findAll() {
        log.debug("Request to get all MemberOnlies");
        return memberOnlyRepository.findAll().stream()
            .map(memberOnlyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberOnlyDTO> findAllByShop(String shop) {
        log.debug("Request to get all MemberOnlies");
        return memberOnlyRepository.findAllByShop(shop).stream()
            .map(memberOnlyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MemberOnlyDTO> findOne(Long id) {
        log.debug("Request to get MemberOnly : {}", id);
        return memberOnlyRepository.findById(id)
            .map(memberOnlyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MemberOnly : {}", id);
        memberOnlyRepository.deleteById(id);
    }
}
