package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.CancellationTypeStatus;

/**
 * A DTO for the {@link com.et.domain.CancellationManagement} entity.
 */
public class CancellationManagementDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private CancellationTypeStatus cancellationType;

    @Lob
    private String cancellationInstructionsText;

    @Lob
    private String cancellationReasonsJSON;

    @Lob
    private String pauseInstructionsText;

    private Integer pauseDurationCycle;

    private Boolean enableDiscountEmail;

    private String discountEmailAddress;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public CancellationTypeStatus getCancellationType() {
        return cancellationType;
    }

    public void setCancellationType(CancellationTypeStatus cancellationType) {
        this.cancellationType = cancellationType;
    }

    public String getCancellationInstructionsText() {
        return cancellationInstructionsText;
    }

    public void setCancellationInstructionsText(String cancellationInstructionsText) {
        this.cancellationInstructionsText = cancellationInstructionsText;
    }

    public String getCancellationReasonsJSON() {
        return cancellationReasonsJSON;
    }

    public void setCancellationReasonsJSON(String cancellationReasonsJSON) {
        this.cancellationReasonsJSON = cancellationReasonsJSON;
    }

    public String getPauseInstructionsText() {
        return pauseInstructionsText;
    }

    public void setPauseInstructionsText(String pauseInstructionsText) {
        this.pauseInstructionsText = pauseInstructionsText;
    }

    public Integer getPauseDurationCycle() {
        return pauseDurationCycle;
    }

    public void setPauseDurationCycle(Integer pauseDurationCycle) {
        this.pauseDurationCycle = pauseDurationCycle;
    }

    public Boolean isEnableDiscountEmail() {
        return enableDiscountEmail;
    }

    public void setEnableDiscountEmail(Boolean enableDiscountEmail) {
        this.enableDiscountEmail = enableDiscountEmail;
    }

    public String getDiscountEmailAddress() {
        return discountEmailAddress;
    }

    public void setDiscountEmailAddress(String discountEmailAddress) {
        this.discountEmailAddress = discountEmailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CancellationManagementDTO cancellationManagementDTO = (CancellationManagementDTO) o;
        if (cancellationManagementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cancellationManagementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancellationManagementDTO{" +
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
