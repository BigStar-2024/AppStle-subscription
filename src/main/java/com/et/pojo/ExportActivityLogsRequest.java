package com.et.pojo;


import com.et.service.dto.ActivityLogCriteria;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;


public class ExportActivityLogsRequest {

//    private int id;

    private String shop;

    private String email;

//    private LongFilter entityId;
//
//    private ActivityLogCriteria.ActivityLogEntityTypeFilter entityType;

//    private ActivityLogCriteria.ActivityLogEventSourceFilter eventSource;
//
//    private ActivityLogCriteria.ActivityLogEventTypeFilter eventType;
//
//    private ActivityLogCriteria.ActivityLogStatusFilter status;
//
//    private ZonedDateTimeFilter createAt;

    private ActivityLogCriteria activityLogCriteria;

    private int pageSize;

    private int pageNumber;

    public ExportActivityLogsRequest() {
    }

    public ExportActivityLogsRequest(String shop, String email, ActivityLogCriteria activityLogCriteria, int pageSize, int pageNumber) {
//        this.id = id;
        this.shop = shop;
        this.email = email;
//        this.entityId = entityId;
//        this.entityType = entityType;
//        this.eventSource = eventSource;
//        this.eventType = eventType;
//        this.status = status;
//        this.createAt = createAt;
        this.activityLogCriteria = activityLogCriteria;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public LongFilter getEntityId() {
//        return entityId;
//    }
//
//    public void setEntityId(LongFilter entityId) {
//        this.entityId = entityId;
//    }
//
//    public ActivityLogCriteria.ActivityLogEntityTypeFilter getEntityType() {
//        return entityType;
//    }
//
//    public void setEntityType(ActivityLogCriteria.ActivityLogEntityTypeFilter entityType) {
//        this.entityType = entityType;
//    }
//
//    public ActivityLogCriteria.ActivityLogEventSourceFilter getEventSource() {
//        return eventSource;
//    }
//
//    public void setEventSource(ActivityLogCriteria.ActivityLogEventSourceFilter eventSource) {
//        this.eventSource = eventSource;
//    }
//
//    public ActivityLogCriteria.ActivityLogEventTypeFilter getEventType() {
//        return eventType;
//    }
//
//    public void setEventType(ActivityLogCriteria.ActivityLogEventTypeFilter eventType) {
//        this.eventType = eventType;
//    }
//
//    public ActivityLogCriteria.ActivityLogStatusFilter getStatus() {
//        return status;
//    }
//
//    public void setStatus(ActivityLogCriteria.ActivityLogStatusFilter status) {
//        this.status = status;
//    }
//
//    public ZonedDateTimeFilter getCreateAt() {
//        return createAt;
//    }
//
//    public void setCreateAt(ZonedDateTimeFilter createAt) {
//        this.createAt = createAt;
//    }


    public ActivityLogCriteria getActivityLogCriteria() {
        return activityLogCriteria;
    }

    public void setActivityLogCriteria(ActivityLogCriteria activityLogCriteria) {
        this.activityLogCriteria = activityLogCriteria;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
