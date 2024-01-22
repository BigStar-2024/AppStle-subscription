package com.et.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import com.et.domain.enumeration.ActivityLogEntityType;
import com.et.domain.enumeration.ActivityLogEventSource;
import com.et.domain.enumeration.ActivityLogEventType;
import com.et.domain.enumeration.ActivityLogStatus;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.et.domain.ActivityLog} entity. This class is used
 * in {@link com.et.web.rest.ActivityLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /activity-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActivityLogCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ActivityLogEntityType
     */
    public static class ActivityLogEntityTypeFilter extends Filter<ActivityLogEntityType> {

        public ActivityLogEntityTypeFilter() {
        }

        public ActivityLogEntityTypeFilter(ActivityLogEntityTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActivityLogEntityTypeFilter copy() {
            return new ActivityLogEntityTypeFilter(this);
        }

    }
    /**
     * Class for filtering ActivityLogEventSource
     */
    public static class ActivityLogEventSourceFilter extends Filter<ActivityLogEventSource> {

        public ActivityLogEventSourceFilter() {
        }

        public ActivityLogEventSourceFilter(ActivityLogEventSourceFilter filter) {
            super(filter);
        }

        @Override
        public ActivityLogEventSourceFilter copy() {
            return new ActivityLogEventSourceFilter(this);
        }

    }
    /**
     * Class for filtering ActivityLogEventType
     */
    public static class ActivityLogEventTypeFilter extends Filter<ActivityLogEventType> {

        public ActivityLogEventTypeFilter() {
        }

        public ActivityLogEventTypeFilter(ActivityLogEventTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActivityLogEventTypeFilter copy() {
            return new ActivityLogEventTypeFilter(this);
        }

    }
    /**
     * Class for filtering ActivityLogStatus
     */
    public static class ActivityLogStatusFilter extends Filter<ActivityLogStatus> {

        public ActivityLogStatusFilter() {
        }

        public ActivityLogStatusFilter(ActivityLogStatusFilter filter) {
            super(filter);
        }

        @Override
        public ActivityLogStatusFilter copy() {
            return new ActivityLogStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shop;

    private LongFilter entityId;

    private ActivityLogEntityTypeFilter entityType;

    private ActivityLogEventSourceFilter eventSource;

    private ActivityLogEventTypeFilter eventType;

    private ActivityLogStatusFilter status;

    private ZonedDateTimeFilter createAt;

    public ActivityLogCriteria(){
    }

    public ActivityLogCriteria(ActivityLogCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.shop = other.shop == null ? null : other.shop.copy();
        this.entityId = other.entityId == null ? null : other.entityId.copy();
        this.entityType = other.entityType == null ? null : other.entityType.copy();
        this.eventSource = other.eventSource == null ? null : other.eventSource.copy();
        this.eventType = other.eventType == null ? null : other.eventType.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createAt = other.createAt == null ? null : other.createAt.copy();
    }

    @Override
    public ActivityLogCriteria copy() {
        return new ActivityLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getShop() {
        return shop;
    }

    public void setShop(StringFilter shop) {
        this.shop = shop;
    }

    public LongFilter getEntityId() {
        return entityId;
    }

    public void setEntityId(LongFilter entityId) {
        this.entityId = entityId;
    }

    public ActivityLogEntityTypeFilter getEntityType() {
        return entityType;
    }

    public void setEntityType(ActivityLogEntityTypeFilter entityType) {
        this.entityType = entityType;
    }

    public ActivityLogEventSourceFilter getEventSource() {
        return eventSource;
    }

    public void setEventSource(ActivityLogEventSourceFilter eventSource) {
        this.eventSource = eventSource;
    }

    public ActivityLogEventTypeFilter getEventType() {
        return eventType;
    }

    public void setEventType(ActivityLogEventTypeFilter eventType) {
        this.eventType = eventType;
    }

    public ActivityLogStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ActivityLogStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActivityLogCriteria that = (ActivityLogCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(shop, that.shop) &&
            Objects.equals(entityId, that.entityId) &&
            Objects.equals(entityType, that.entityType) &&
            Objects.equals(eventSource, that.eventSource) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createAt, that.createAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        shop,
        entityId,
        entityType,
        eventSource,
        eventType,
        status,
        createAt
        );
    }

    @Override
    public String toString() {
        return "ActivityLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (shop != null ? "shop=" + shop + ", " : "") +
                (entityId != null ? "entityId=" + entityId + ", " : "") +
                (entityType != null ? "entityType=" + entityType + ", " : "") +
                (eventSource != null ? "eventSource=" + eventSource + ", " : "") +
                (eventType != null ? "eventType=" + eventType + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
            "}";
    }

}
