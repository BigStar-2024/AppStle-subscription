package com.et.domain;


import com.et.domain.enumeration.OnboardingChecklistStep;
import com.et.service.converter.OnboardingChecklistConverter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * A OnboardingInfo.
 */
@Entity
@Table(name = "onboarding_info")
public class OnboardingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop")
    private String shop;

    @Column(name = "uncompleted_checklist_steps")
    @Convert(converter =  OnboardingChecklistConverter.class)
    private List<OnboardingChecklistStep> uncompletedChecklistSteps;

    @Column(name = "completed_checklist_steps")
    @Convert(converter =  OnboardingChecklistConverter.class)
    private List<OnboardingChecklistStep> completedChecklistSteps;

    @Column(name = "checklist_completed")
    private Boolean checklistCompleted;

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

    public OnboardingInfo shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public List<OnboardingChecklistStep> getUncompletedChecklistSteps() {
        return uncompletedChecklistSteps;
    }

    public OnboardingInfo uncompletedChecklistSteps(List<OnboardingChecklistStep> uncompletedChecklistSteps) {
        this.uncompletedChecklistSteps = uncompletedChecklistSteps;
        return this;
    }

    public void setUncompletedChecklistSteps(List<OnboardingChecklistStep> uncompletedChecklistSteps) {
        this.uncompletedChecklistSteps = uncompletedChecklistSteps;
    }

    public List<OnboardingChecklistStep> getCompletedChecklistSteps() {
        return completedChecklistSteps;
    }

    public OnboardingInfo completedChecklistSteps(List<OnboardingChecklistStep> completedChecklistSteps) {
        this.completedChecklistSteps = completedChecklistSteps;
        return this;
    }

    public void setCompletedChecklistSteps(List<OnboardingChecklistStep> completedChecklistSteps) {
        this.completedChecklistSteps = completedChecklistSteps;
    }

    public Boolean isChecklistCompleted() {
        return checklistCompleted;
    }

    public OnboardingInfo checklistCompleted(Boolean checklistCompleted) {
        this.checklistCompleted = checklistCompleted;
        return this;
    }

    public void setChecklistCompleted(Boolean checklistCompleted) {
        this.checklistCompleted = checklistCompleted;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OnboardingInfo)) {
            return false;
        }
        return id != null && id.equals(((OnboardingInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OnboardingInfo{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", uncompletedChecklistSteps='" + getUncompletedChecklistSteps() + "'" +
            ", completedChecklistSteps='" + getCompletedChecklistSteps() + "'" +
            ", checklistCompleted='" + isChecklistCompleted() + "'" +
            "}";
    }
}
