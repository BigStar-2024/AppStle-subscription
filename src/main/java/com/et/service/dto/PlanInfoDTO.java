package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.PlanType;
import com.et.domain.enumeration.BillingType;
import com.et.domain.enumeration.BasedOn;
import com.et.domain.enumeration.BasePlan;

/**
 * A DTO for the {@link com.et.domain.PlanInfo} entity.
 */
public class PlanInfoDTO implements Serializable, Cloneable {

    private Long id;

    private String name;

    private Double price;

    private Boolean archived;

    private PlanType planType;

    private BillingType billingType;

    private BasedOn basedOn;

    @Lob
    private String additionalDetails;

    @Lob
    private String features;

    private Integer trialDays;

    @NotNull
    private BasePlan basePlan;

    private Boolean isApplicablePlan;

    private Double discountPrice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public void setBillingType(BillingType billingType) {
        this.billingType = billingType;
    }

    public BasedOn getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(BasedOn basedOn) {
        this.basedOn = basedOn;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public Integer getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(Integer trialDays) {
        this.trialDays = trialDays;
    }

    public BasePlan getBasePlan() {
        return basePlan;
    }

    public void setBasePlan(BasePlan basePlan) {
        this.basePlan = basePlan;
    }

    public Boolean getApplicablePlan() {
        return isApplicablePlan;
    }

    public void setApplicablePlan(Boolean applicablePlan) {
        isApplicablePlan = applicablePlan;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlanInfoDTO planInfoDTO = (PlanInfoDTO) o;
        if (planInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "PlanInfoDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", archived='" + isArchived() + "'" +
            ", planType='" + getPlanType() + "'" +
            ", billingType='" + getBillingType() + "'" +
            ", basedOn='" + getBasedOn() + "'" +
            ", additionalDetails='" + getAdditionalDetails() + "'" +
            ", features='" + getFeatures() + "'" +
            ", trialDays=" + getTrialDays() +
            ", basePlan='" + getBasePlan() + "'" +
            ", isApplicablePlan=" + getApplicablePlan() +
            ", discountPrice=" + getDiscountPrice() +
            "}";
    }
}
