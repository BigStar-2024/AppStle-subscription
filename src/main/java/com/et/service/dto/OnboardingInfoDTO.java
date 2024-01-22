package com.et.service.dto;

import com.et.domain.enumeration.OnboardingChecklistStep;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.et.domain.OnboardingInfo} entity.
 */
public class OnboardingInfoDTO implements Serializable {

    private Long id;

    private String shop;

    private List<OnboardingChecklistStep> uncompletedChecklistSteps;

    private List<OnboardingChecklistStep> completedChecklistSteps;

    private Boolean checklistCompleted;


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

    public List<OnboardingChecklistStep> getUncompletedChecklistSteps() {
        return uncompletedChecklistSteps;
    }

    public void setUncompletedChecklistSteps(List<OnboardingChecklistStep> uncompletedChecklistSteps) {
        this.uncompletedChecklistSteps = uncompletedChecklistSteps;
    }

    public List<OnboardingChecklistStep> getCompletedChecklistSteps() {
        return completedChecklistSteps;
    }

    public void setCompletedChecklistSteps(List<OnboardingChecklistStep> completedChecklistSteps) {
        this.completedChecklistSteps = completedChecklistSteps;
    }

    public Boolean isChecklistCompleted() {
        return checklistCompleted;
    }

    public void setChecklistCompleted(Boolean checklistCompleted) {
        this.checklistCompleted = checklistCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OnboardingInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((OnboardingInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OnboardingInfoDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", uncompletedChecklistSteps='" + getUncompletedChecklistSteps() + "'" +
            ", completedChecklistSteps='" + getCompletedChecklistSteps() + "'" +
            ", checklistCompleted='" + isChecklistCompleted() + "'" +
            "}";
    }
}
