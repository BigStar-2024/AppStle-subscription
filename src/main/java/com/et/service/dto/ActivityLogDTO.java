package com.et.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.ActivityLogEntityType;
import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.domain.enumeration.ActivityLogEventType;
import com.et.domain.enumeration.ActivityLogStatus;

/**
 * A DTO for the {@link com.et.domain.ActivityLog} entity.
 */
public class ActivityLogDTO implements Serializable {

    private Long id;

    private String shop;

    private Long entityId;

    private ActivityLogEntityType entityType;

    private ActivityLogEventSource eventSource;

    private ActivityLogEventType eventType;

    private ActivityLogStatus status;

    private ZonedDateTime createAt;

    @Lob
    private String additionalInfo;


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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public ActivityLogEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(ActivityLogEntityType entityType) {
        this.entityType = entityType;
    }

    public ActivityLogEventSource getEventSource() {
        return eventSource;
    }

    public void setEventSource(ActivityLogEventSource eventSource) {
        this.eventSource = eventSource;
    }

    public ActivityLogEventType getEventType() {
        return eventType;
    }

    public void setEventType(ActivityLogEventType eventType) {
        this.eventType = eventType;
    }

    public ActivityLogStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityLogStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityLogDTO activityLogDTO = (ActivityLogDTO) o;
        if (activityLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityLogDTO{" +
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
