package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A AsyncUpdateEventProcessing.
 */
@Entity
@Table(name = "async_update_event_processing")
public class AsyncUpdateEventProcessing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "subscription_contract_id", nullable = false)
    private Long subscriptionContractId;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    @Lob
    @Column(name = "tag_model_json")
    private String tagModelJson;

    @Column(name = "first_time_order_tags")
    private String firstTimeOrderTags;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public AsyncUpdateEventProcessing subscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
        return this;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public AsyncUpdateEventProcessing lastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTagModelJson() {
        return tagModelJson;
    }

    public AsyncUpdateEventProcessing tagModelJson(String tagModelJson) {
        this.tagModelJson = tagModelJson;
        return this;
    }

    public void setTagModelJson(String tagModelJson) {
        this.tagModelJson = tagModelJson;
    }

    public String getFirstTimeOrderTags() {
        return firstTimeOrderTags;
    }

    public AsyncUpdateEventProcessing firstTimeOrderTags(String firstTimeOrderTags) {
        this.firstTimeOrderTags = firstTimeOrderTags;
        return this;
    }

    public void setFirstTimeOrderTags(String firstTimeOrderTags) {
        this.firstTimeOrderTags = firstTimeOrderTags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsyncUpdateEventProcessing)) {
            return false;
        }
        return id != null && id.equals(((AsyncUpdateEventProcessing) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsyncUpdateEventProcessing{" +
            "id=" + getId() +
            ", subscriptionContractId=" + getSubscriptionContractId() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", tagModelJson='" + getTagModelJson() + "'" +
            ", firstTimeOrderTags='" + getFirstTimeOrderTags() + "'" +
            "}";
    }
}
