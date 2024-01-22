package com.et.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.SubscriptionContractProcessing} entity.
 */
public class SubscriptionContractProcessingDTO implements Serializable {
    
    private Long id;

    private Long contractId;

    private Integer attemptCount;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractProcessingDTO)) {
            return false;
        }

        return id != null && id.equals(((SubscriptionContractProcessingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractProcessingDTO{" +
            "id=" + getId() +
            ", contractId=" + getContractId() +
            ", attemptCount=" + getAttemptCount() +
            "}";
    }
}
