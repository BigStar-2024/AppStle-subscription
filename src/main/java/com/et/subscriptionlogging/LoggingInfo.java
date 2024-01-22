package com.et.subscriptionlogging;

import com.et.constant.LoggingConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

public class LoggingInfo {
    private long entityId;
    private LoggingConstants.EntityType entityType;
    private LoggingConstants.EventLog eventLog;

    private LoggingConstants.EventSource eventSource;
    private String status;
    private String shop;
    private ZonedDateTime createdAt;
    private List<String> additionalInfo;
    private String tags; //TODO : For logging additional information related to shop,user etc. Field will contain json

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public LoggingConstants.EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(LoggingConstants.EntityType entityType) {
        this.entityType = entityType;
    }

    public LoggingConstants.EventLog getEventLog() {
        return eventLog;
    }

    public void setEventLog(LoggingConstants.EventLog eventLog) {
        this.eventLog = eventLog;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public LoggingConstants.EventSource getEventSource() {
        return eventSource;
    }

    public void setEventSource(LoggingConstants.EventSource eventSource) {
        this.eventSource = eventSource;
    }
}
