package com.et.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A SubscriptionContractProcessing.
 */
@Entity
@Table(name = "subscription_contract_processing")
public class SubscriptionContractProcessing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "attempt_count")
    private Integer attemptCount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public SubscriptionContractProcessing contractId(Long contractId) {
        this.contractId = contractId;
        return this;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public SubscriptionContractProcessing attemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
        return this;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionContractProcessing)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionContractProcessing) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionContractProcessing{" +
            "id=" + getId() +
            ", contractId=" + getContractId() +
            ", attemptCount=" + getAttemptCount() +
            "}";
    }
}
