package com.et.service.impl;

import com.et.domain.PlanInfoDiscount;
import com.et.domain.enumeration.PlanInfoDiscountType;
import com.et.repository.PlanInfoDiscountRepository;
import com.et.service.PlanInfoDiscountService;
import com.et.service.dto.PlanInfoDTO;
import com.et.service.dto.PlanInfoDiscountDTO;
import com.et.service.mapper.PlanInfoDiscountMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PlanInfoDiscount}.
 */
@Service
@Transactional
public class PlanInfoDiscountServiceImpl implements PlanInfoDiscountService {

    private final Logger log = LoggerFactory.getLogger(PlanInfoDiscountServiceImpl.class);

    private final PlanInfoDiscountRepository planInfoDiscountRepository;

    private final PlanInfoDiscountMapper planInfoDiscountMapper;

    public PlanInfoDiscountServiceImpl(PlanInfoDiscountRepository planInfoDiscountRepository, PlanInfoDiscountMapper planInfoDiscountMapper) {
        this.planInfoDiscountRepository = planInfoDiscountRepository;
        this.planInfoDiscountMapper = planInfoDiscountMapper;
    }

    /**
     * Save a planInfoDiscount.
     *
     * @param planInfoDiscountDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PlanInfoDiscountDTO save(PlanInfoDiscountDTO planInfoDiscountDTO) {
        log.debug("Request to save PlanInfoDiscount : {}", planInfoDiscountDTO);
        PlanInfoDiscount planInfoDiscount = planInfoDiscountMapper.toEntity(planInfoDiscountDTO);
        planInfoDiscount = planInfoDiscountRepository.save(planInfoDiscount);
        return planInfoDiscountMapper.toDto(planInfoDiscount);
    }

    /**
     * Get all the planInfoDiscounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlanInfoDiscountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlanInfoDiscounts");
        return planInfoDiscountRepository.findAll(pageable)
            .map(planInfoDiscountMapper::toDto);
    }


    /**
     * Get one planInfoDiscount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlanInfoDiscountDTO> findOne(Long id) {
        log.debug("Request to get PlanInfoDiscount : {}", id);
        return planInfoDiscountRepository.findById(id)
            .map(planInfoDiscountMapper::toDto);
    }

    /**
     * Delete the planInfoDiscount by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlanInfoDiscount : {}", id);
        planInfoDiscountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanInfoDiscountDTO> findByDiscountCode(String discountCode) {
        log.debug("Request to get PlanInfoDiscount by discountCode : {}", discountCode);
        return planInfoDiscountRepository.findByDiscountCodeIgnoreCase(discountCode)
            .map(planInfoDiscountMapper::toDto);
    }

    @Override
    public boolean isValidDiscountCode(String discountCode) {
        boolean isValid = false;

        if(StringUtils.isNotBlank(discountCode) && !discountCode.contains(" ")) {
            Optional<PlanInfoDiscount> planInfoDiscountOptional = planInfoDiscountRepository.findByDiscountCodeIgnoreCase(discountCode.trim());

            if(planInfoDiscountOptional.isPresent()) {
                PlanInfoDiscount planInfoDiscount = planInfoDiscountOptional.get();

                if(
                    !planInfoDiscount.isArchived()
                    && (planInfoDiscount.getStartDate() == null || planInfoDiscount.getStartDate().isBefore(ZonedDateTime.now()))
                    && (planInfoDiscount.getEndDate() == null || planInfoDiscount.getEndDate().isAfter(ZonedDateTime.now()))
                ) {
                    isValid = true;
                }
            }
        }

        return isValid;
    }

    @Override
    public Double calculatePlanDiscountPrice(PlanInfoDTO planInfoDTO, PlanInfoDiscountDTO planInfoDiscountDTO) {
        Double discountPrice = null;
        if(planInfoDTO.getPrice() != null && planInfoDTO.getPrice() > 0) {
            BigDecimal price = BigDecimal.valueOf(planInfoDTO.getPrice());
            BigDecimal discountAmount = BigDecimal.ZERO;

            if(PlanInfoDiscountType.PERCENTAGE.equals(planInfoDiscountDTO.getDiscountType())) {
                BigDecimal discountPercentage = Optional.ofNullable(planInfoDiscountDTO.getDiscount()).orElse(BigDecimal.ZERO);
                discountAmount = price.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
            } else if(PlanInfoDiscountType.FIXED_AMOUNT.equals(planInfoDiscountDTO.getDiscountType())) {
                discountAmount = planInfoDiscountDTO.getDiscount();
            }

            if(discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                discountPrice = price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
        }

        return discountPrice;
    }
}
