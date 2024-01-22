package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.PlanInfoDiscountType;

/**
 * A PlanInfoDiscount.
 */
@Entity
@Table(name = "plan_info_discount")
public class PlanInfoDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "discount_code", unique = true)
    private String discountCode;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private PlanInfoDiscountType discountType;

    @Column(name = "discount", precision = 21, scale = 2)
    private BigDecimal discount;

    @Column(name = "max_discount_amount", precision = 21, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "trial_days")
    private Integer trialDays;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "archived")
    private Boolean archived;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public PlanInfoDiscount discountCode(String discountCode) {
        this.discountCode = discountCode;
        return this;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDescription() {
        return description;
    }

    public PlanInfoDiscount description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlanInfoDiscountType getDiscountType() {
        return discountType;
    }

    public PlanInfoDiscount discountType(PlanInfoDiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(PlanInfoDiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public PlanInfoDiscount discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public PlanInfoDiscount maxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
        return this;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public PlanInfoDiscount trialDays(Integer trialDays) {
        this.trialDays = trialDays;
        return this;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public PlanInfoDiscount startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public PlanInfoDiscount endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isArchived() {
        return archived;
    }

    public PlanInfoDiscount archived(Boolean archived) {
        this.archived = archived;
        return this;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanInfoDiscount)) {
            return false;
        }
        return id != null && id.equals(((PlanInfoDiscount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlanInfoDiscount{" +
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
