package com.et.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.AsyncUpdateEventProcessing} entity.
 */
public class AsyncUpdateEventProcessingDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Long subscriptionContractId;

    private ZonedDateTime lastUpdated;

    @Lob
    private String tagModelJson;

    private String firstTimeOrderTags;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTagModelJson() {
        return tagModelJson;
    }

    public void setTagModelJson(String tagModelJson) {
        this.tagModelJson = tagModelJson;
    }

    public String getFirstTimeOrderTags() {
        return firstTimeOrderTags;
    }

    public void setFirstTimeOrderTags(String firstTimeOrderTags) {
        this.firstTimeOrderTags = firstTimeOrderTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsyncUpdateEventProcessingDTO)) {
            return false;
        }

        return id != null && id.equals(((AsyncUpdateEventProcessingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsyncUpdateEventProcessingDTO{" +
            "id=" + getId() +
            ", subscriptionContractId=" + getSubscriptionContractId() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", tagModelJson='" + getTagModelJson() + "'" +
            ", firstTimeOrderTags='" + getFirstTimeOrderTags() + "'" +
            "}";
    }
}
