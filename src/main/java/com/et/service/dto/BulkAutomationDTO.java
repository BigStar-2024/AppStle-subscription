package com.et.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.BulkAutomationType;

/**
 * A DTO for the {@link com.et.domain.BulkAutomation} entity.
 */
public class BulkAutomationDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private BulkAutomationType automationType;

    private Boolean running;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    @Lob
    private String requestInfo;

    @Lob
    private String errorInfo;

    private String currentExecution;


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

    public BulkAutomationType getAutomationType() {
        return automationType;
    }

    public void setAutomationType(BulkAutomationType automationType) {
        this.automationType = automationType;
    }

    public Boolean isRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getCurrentExecution() {
        return currentExecution;
    }

    public void setCurrentExecution(String currentExecution) {
        this.currentExecution = currentExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BulkAutomationDTO)) {
            return false;
        }

        return id != null && id.equals(((BulkAutomationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulkAutomationDTO{" +
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
