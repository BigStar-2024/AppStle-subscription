package com.et.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.PlanInfoDiscountType;

/**
 * A DTO for the {@link com.et.domain.PlanInfoDiscount} entity.
 */
public class PlanInfoDiscountDTO implements Serializable {

    private Long id;

    
    private String discountCode;

    @Lob
    private String description;

    private PlanInfoDiscountType discountType;

    private BigDecimal discount;

    private BigDecimal maxDiscountAmount;

    private Integer trialDays;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Boolean archived;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlanInfoDiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(PlanInfoDiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanInfoDiscountDTO planInfoDiscountDTO = (PlanInfoDiscountDTO) o;
        if (planInfoDiscountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planInfoDiscountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanInfoDiscountDTO{" +
            "id=" + getId() +
            ", discountCode='" + getDiscountCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discount=" + getDiscount() +
            ", maxDiscountAmount=" + getMaxDiscountAmount() +
            ", trialDays=" + getTrialDays() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", archived='" + isArchived() + "'" +
            "}";
    }
}
