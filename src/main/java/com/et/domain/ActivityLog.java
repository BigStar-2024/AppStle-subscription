package com.et.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.ActivityLogEntityType;

import com.et.domain.enumeration.ActivityLogEventSource;

import com.et.domain.enumeration.ActivityLogEventType;

import com.et.domain.enumeration.ActivityLogStatus;

/**
 * A ActivityLog.
 */
@Entity
@Table(name = "activity_log")
public class ActivityLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop")
    private String shop;

    @Column(name = "entity_id")
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private ActivityLogEntityType entityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_source")
    private ActivityLogEventSource eventSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private ActivityLogEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActivityLogStatus status;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @Lob
    @Column(name = "additional_info")
    private String additionalInfo;

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

    public ActivityLog shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Long getEntityId() {
        return entityId;
    }

    public ActivityLog entityId(Long entityId) {
        this.entityId = entityId;
        return this;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public ActivityLogEntityType getEntityType() {
        return entityType;
    }

    public ActivityLog entityType(ActivityLogEntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public void setEntityType(ActivityLogEntityType entityType) {
        this.entityType = entityType;
    }

    public ActivityLogEventSource getEventSource() {
        return eventSource;
    }

    public ActivityLog eventSource(ActivityLogEventSource eventSource) {
        this.eventSource = eventSource;
        return this;
    }

    public void setEventSource(ActivityLogEventSource eventSource) {
        this.eventSource = eventSource;
    }

    public ActivityLogEventType getEventType() {
        return eventType;
    }

    public ActivityLog eventType(ActivityLogEventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(ActivityLogEventType eventType) {
        this.eventType = eventType;
    }

    public ActivityLogStatus getStatus() {
        return status;
    }

    public ActivityLog status(ActivityLogStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ActivityLogStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public ActivityLog createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public ActivityLog additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityLog)) {
            return false;
        }
        return id != null && id.equals(((ActivityLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", entityId=" + getEntityId() +
            ", entityType='" + getEntityType() + "'" +
            ", eventSource='" + getEventSource() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", status='" + getStatus() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            "}";
    }
}
