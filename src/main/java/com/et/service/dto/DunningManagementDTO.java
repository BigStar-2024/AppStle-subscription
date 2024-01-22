package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import com.et.domain.enumeration.RetryAttempts;
import com.et.domain.enumeration.DaysBeforeRetrying;
import com.et.domain.enumeration.MaxNumberOfFailures;

/**
 * A DTO for the {@link com.et.domain.DunningManagement} entity.
 */
public class DunningManagementDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private RetryAttempts retryAttempts;

    private DaysBeforeRetrying daysBeforeRetrying;

    private MaxNumberOfFailures maxNumberOfFailures;

    
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

    public RetryAttempts getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(RetryAttempts retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public DaysBeforeRetrying getDaysBeforeRetrying() {
        return daysBeforeRetrying;
    }

    public void setDaysBeforeRetrying(DaysBeforeRetrying daysBeforeRetrying) {
        this.daysBeforeRetrying = daysBeforeRetrying;
    }

    public MaxNumberOfFailures getMaxNumberOfFailures() {
        return maxNumberOfFailures;
    }

    public void setMaxNumberOfFailures(MaxNumberOfFailures maxNumberOfFailures) {
        this.maxNumberOfFailures = maxNumberOfFailures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DunningManagementDTO)) {
            return false;
        }

        return id != null && id.equals(((DunningManagementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DunningManagementDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", retryAttempts='" + getRetryAttempts() + "'" +
            ", daysBeforeRetrying='" + getDaysBeforeRetrying() + "'" +
            ", maxNumberOfFailures='" + getMaxNumberOfFailures() + "'" +
            "}";
    }
}
