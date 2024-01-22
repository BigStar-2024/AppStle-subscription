package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.BulkAutomationType;

/**
 * A BulkAutomation.
 */
@Entity
@Table(name = "bulk_automation")
public class BulkAutomation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "automation_type", nullable = false)
    private BulkAutomationType automationType;

    @Column(name = "running")
    private Boolean running;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Lob
    @Column(name = "request_info")
    private String requestInfo;

    @Lob
    @Column(name = "error_info")
    private String errorInfo;

    @Column(name = "current_execution")
    private String currentExecution;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public BulkAutomation shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public BulkAutomationType getAutomationType() {
        return automationType;
    }

    public BulkAutomation automationType(BulkAutomationType automationType) {
        this.automationType = automationType;
        return this;
    }

    public void setAutomationType(BulkAutomationType automationType) {
        this.automationType = automationType;
    }

    public Boolean isRunning() {
        return running;
    }

    public BulkAutomation running(Boolean running) {
        this.running = running;
        return this;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public BulkAutomation startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public BulkAutomation endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public BulkAutomation requestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public BulkAutomation errorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
        return this;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getCurrentExecution() {
        return currentExecution;
    }

    public BulkAutomation currentExecution(String currentExecution) {
        this.currentExecution = currentExecution;
        return this;
    }

    public void setCurrentExecution(String currentExecution) {
        this.currentExecution = currentExecution;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BulkAutomation)) {
            return false;
        }
        return id != null && id.equals(((BulkAutomation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BulkAutomation{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", automationType='" + getAutomationType() + "'" +
            ", running='" + isRunning() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", requestInfo='" + getRequestInfo() + "'" +
            ", errorInfo='" + getErrorInfo() + "'" +
            ", currentExecution='" + getCurrentExecution() + "'" +
            "}";
    }
}
