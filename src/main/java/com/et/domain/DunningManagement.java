package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.RetryAttempts;

import com.et.domain.enumeration.DaysBeforeRetrying;

import com.et.domain.enumeration.MaxNumberOfFailures;

/**
 * A DunningManagement.
 */
@Entity
@Table(name = "dunning_management")
public class DunningManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Enumerated(EnumType.STRING)
    @Column(name = "retry_attempts")
    private RetryAttempts retryAttempts;

    @Enumerated(EnumType.STRING)
    @Column(name = "days_before_retrying")
    private DaysBeforeRetrying daysBeforeRetrying;

    @Enumerated(EnumType.STRING)
    @Column(name = "max_number_of_failures")
    private MaxNumberOfFailures maxNumberOfFailures;

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

    public DunningManagement shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public RetryAttempts getRetryAttempts() {
        return retryAttempts;
    }

    public DunningManagement retryAttempts(RetryAttempts retryAttempts) {
        this.retryAttempts = retryAttempts;
        return this;
    }

    public void setRetryAttempts(RetryAttempts retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public DaysBeforeRetrying getDaysBeforeRetrying() {
        return daysBeforeRetrying;
    }

    public DunningManagement daysBeforeRetrying(DaysBeforeRetrying daysBeforeRetrying) {
        this.daysBeforeRetrying = daysBeforeRetrying;
        return this;
    }

    public void setDaysBeforeRetrying(DaysBeforeRetrying daysBeforeRetrying) {
        this.daysBeforeRetrying = daysBeforeRetrying;
    }

    public MaxNumberOfFailures getMaxNumberOfFailures() {
        return maxNumberOfFailures;
    }

    public DunningManagement maxNumberOfFailures(MaxNumberOfFailures maxNumberOfFailures) {
        this.maxNumberOfFailures = maxNumberOfFailures;
        return this;
    }

    public void setMaxNumberOfFailures(MaxNumberOfFailures maxNumberOfFailures) {
        this.maxNumberOfFailures = maxNumberOfFailures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DunningManagement)) {
            return false;
        }
        return id != null && id.equals(((DunningManagement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DunningManagement{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", retryAttempts='" + getRetryAttempts() + "'" +
            ", daysBeforeRetrying='" + getDaysBeforeRetrying() + "'" +
            ", maxNumberOfFailures='" + getMaxNumberOfFailures() + "'" +
            "}";
    }
}
