package com.et.service.impl;

import com.et.domain.enumeration.PlanInfoDiscountType;
import com.et.service.PlanInfoDiscountService;
import com.et.service.PlanInfoService;
import com.et.domain.PlanInfo;
import com.et.repository.PlanInfoRepository;
import com.et.service.dto.PlanInfoDTO;
import com.et.service.dto.PlanInfoDiscountDTO;
import com.et.service.mapper.PlanInfoMapper;
import org.apache.commons.lang3.ObjectUtils;
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
 * Service Implementation for managing {@link PlanInfo}.
 */
@Service
@Transactional
public class PlanInfoServiceImpl implements PlanInfoService {

    private final Logger log = LoggerFactory.getLogger(PlanInfoServiceImpl.class);

    private final PlanInfoRepository planInfoRepository;

    private final PlanInfoMapper planInfoMapper;

    @Autowired
    private PlanInfoDiscountService planInfoDiscountService;

    public PlanInfoServiceImpl(PlanInfoRepository planInfoRepository, PlanInfoMapper planInfoMapper) {
        this.planInfoRepository = planInfoRepository;
        this.planInfoMapper = planInfoMapper;
    }

    /**
     * Save a planInfo.
     *
     * @param planInfoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PlanInfoDTO save(PlanInfoDTO planInfoDTO) {
        log.debug("Request to save PlanInfo : {}", planInfoDTO);
        PlanInfo planInfo = planInfoMapper.toEntity(planInfoDTO);
        planInfo = planInfoRepository.save(planInfo);
        return planInfoMapper.toDto(planInfo);
    }

    /**
     * Get all the planInfos.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlanInfoDTO> findAll() {
        log.debug("Request to get all PlanInfos");
        return planInfoRepository.findAll().stream()
            .map(planInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one planInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlanInfoDTO> findOne(Long id) {
        log.debug("Request to get PlanInfo : {}", id);
        return planInfoRepository.findById(id)
            .map(planInfoMapper::toDto);
    }

    /**
     * Delete the planInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlanInfo : {}", id);
        planInfoRepository.deleteById(id);
    }

    @Override
    public Optional<PlanInfoDTO> findByName(String name) {
        log.debug("Request to get PlanInfo by name : {}", name);
        return planInfoRepository.findByNameIgnoreCase(name)
            .map(planInfoMapper::toDto);
    }

    @Override
    public PlanInfoDTO createPlanInfoForDiscount(PlanInfoDTO planInfoDTO, PlanInfoDiscountDTO planInfoDiscountDTO) {
        if(planInfoDTO.isArchived() && planInfoDTO.getPrice() == 0.0) {
            return planInfoDTO;
        }

        String planName = planInfoDTO.getName() + "["+ planInfoDiscountDTO.getDiscountCode().toUpperCase() +"]";

        Optional<PlanInfoDTO> existingPlanInfoDTOOptional = findByName(planName);

        Double discountPrice = planInfoDTO.getPrice();
        Integer trialDays = planInfoDTO.getTrialDays();

        if(PlanInfoDiscountType.TRIAL_DAYS.equals(planInfoDiscountDTO.getDiscountType())) {
            trialDays = Optional.ofNullable(planInfoDiscountDTO.getTrialDays()).orElse(planInfoDTO.getTrialDays());
        } else {
            discountPrice = planInfoDiscountService.calculatePlanDiscountPrice(planInfoDTO, planInfoDiscountDTO);
        }

        if(existingPlanInfoDTOOptional.isPresent()) {
            PlanInfoDTO existingPlanDTO = existingPlanInfoDTOOptional.get();

            //update plan
            existingPlanDTO.setPrice(discountPrice);
            existingPlanDTO.setAdditionalDetails(planInfoDTO.getAdditionalDetails());
            existingPlanDTO.setFeatures(planInfoDTO.getFeatures());
            existingPlanDTO.setTrialDays(trialDays);
            existingPlanDTO = save(existingPlanDTO);

            return existingPlanDTO;
        } else {
            PlanInfoDTO newPlanInfoDTO = ObjectUtils.clone(planInfoDTO);
            newPlanInfoDTO.setId(null);
            newPlanInfoDTO.setName(planName);
            newPlanInfoDTO.setArchived(true);
            newPlanInfoDTO.setPrice(discountPrice);
            newPlanInfoDTO.setTrialDays(trialDays);
            newPlanInfoDTO = save(newPlanInfoDTO);

            return newPlanInfoDTO;
        }
    }
}
