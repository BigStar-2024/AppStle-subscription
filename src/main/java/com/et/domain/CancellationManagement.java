package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.CancellationTypeStatus;

/**
 * A CancellationManagement.
 */
@Entity
@Table(name = "cancellation_management")
public class CancellationManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancellation_type")
    private CancellationTypeStatus cancellationType;

    @Lob
    @Column(name = "cancellation_instructions_text")
    private String cancellationInstructionsText;

    @Lob
    @Column(name = "cancellation_reasons_json")
    private String cancellationReasonsJSON;

    @Lob
    @Column(name = "pause_instructions_text")
    private String pauseInstructionsText;

    @Column(name = "pause_duration_cycle")
    private Integer pauseDurationCycle;

    @Column(name = "enable_discount_email")
    private Boolean enableDiscountEmail;

    @Column(name = "discount_email_address")
    private String discountEmailAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public CancellationManagement shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public CancellationTypeStatus getCancellationType() {
        return cancellationType;
    }

    public CancellationManagement cancellationType(CancellationTypeStatus cancellationType) {
        this.cancellationType = cancellationType;
        return this;
    }

    public void setCancellationType(CancellationTypeStatus cancellationType) {
        this.cancellationType = cancellationType;
    }

    public String getCancellationInstructionsText() {
        return cancellationInstructionsText;
    }

    public CancellationManagement cancellationInstructionsText(String cancellationInstructionsText) {
        this.cancellationInstructionsText = cancellationInstructionsText;
        return this;
    }

    public void setCancellationInstructionsText(String cancellationInstructionsText) {
        this.cancellationInstructionsText = cancellationInstructionsText;
    }

    public String getCancellationReasonsJSON() {
        return cancellationReasonsJSON;
    }

    public CancellationManagement cancellationReasonsJSON(String cancellationReasonsJSON) {
        this.cancellationReasonsJSON = cancellationReasonsJSON;
        return this;
    }

    public void setCancellationReasonsJSON(String cancellationReasonsJSON) {
        this.cancellationReasonsJSON = cancellationReasonsJSON;
    }

    public String getPauseInstructionsText() {
        return pauseInstructionsText;
    }

    public CancellationManagement pauseInstructionsText(String pauseInstructionsText) {
        this.pauseInstructionsText = pauseInstructionsText;
        return this;
    }

    public void setPauseInstructionsText(String pauseInstructionsText) {
        this.pauseInstructionsText = pauseInstructionsText;
    }

    public Integer getPauseDurationCycle() {
        return pauseDurationCycle;
    }

    public CancellationManagement pauseDurationCycle(Integer pauseDurationCycle) {
        this.pauseDurationCycle = pauseDurationCycle;
        return this;
    }

    public void setPauseDurationCycle(Integer pauseDurationCycle) {
        this.pauseDurationCycle = pauseDurationCycle;
    }

    public Boolean isEnableDiscountEmail() {
        return enableDiscountEmail;
    }

    public CancellationManagement enableDiscountEmail(Boolean enableDiscountEmail) {
        this.enableDiscountEmail = enableDiscountEmail;
        return this;
    }

    public void setEnableDiscountEmail(Boolean enableDiscountEmail) {
        this.enableDiscountEmail = enableDiscountEmail;
    }

    public String getDiscountEmailAddress() {
        return discountEmailAddress;
    }

    public CancellationManagement discountEmailAddress(String discountEmailAddress) {
        this.discountEmailAddress = discountEmailAddress;
        return this;
    }

    public void setDiscountEmailAddress(String discountEmailAddress) {
        this.discountEmailAddress = discountEmailAddress;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CancellationManagement)) {
            return false;
        }
        return id != null && id.equals(((CancellationManagement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancellationManagement{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", cancellationType='" + getCancellationType() + "'" +
            ", cancellationInstructionsText='" + getCancellationInstructionsText() + "'" +
            ", cancellationReasonsJSON='" + getCancellationReasonsJSON() + "'" +
            ", pauseInstructionsText='" + getPauseInstructionsText() + "'" +
            ", pauseDurationCycle=" + getPauseDurationCycle() +
            ", enableDiscountEmail='" + isEnableDiscountEmail() + "'" +
            ", discountEmailAddress='" + getDiscountEmailAddress() + "'" +
            "}";
    }
}
