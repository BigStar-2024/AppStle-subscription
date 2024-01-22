package com.et.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.ProcessedOrderInfoStatus;
import com.et.domain.enumeration.ProcessedOrderInfoDelayedTaggingUnit;

/**
 * A DTO for the {@link com.et.domain.ProcessedOrderInfo} entity.
 */
public class ProcessedOrderInfoDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @NotNull
    private Long orderId;

    @NotNull
    private Long orderNumber;

    private String topicName;

    private ZonedDateTime processedTime;

    @Lob
    private String triggerRuleInfoJson;

    @Lob
    private String attachedTags;

    @Lob
    private String attachedTagsToNote;

    @Lob
    private String removedTags;

    @Lob
    private String removedTagsAfter;

    @Lob
    private String removedTagsBefore;

    private String firstName;

    private String lastName;

    private ProcessedOrderInfoStatus status;

    private Long delayedTaggingValue;

    private ProcessedOrderInfoDelayedTaggingUnit delayedTaggingUnit;

    
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public ZonedDateTime getProcessedTime() {
        return processedTime;
    }

    public void setProcessedTime(ZonedDateTime processedTime) {
        this.processedTime = processedTime;
    }

    public String getTriggerRuleInfoJson() {
        return triggerRuleInfoJson;
    }

    public void setTriggerRuleInfoJson(String triggerRuleInfoJson) {
        this.triggerRuleInfoJson = triggerRuleInfoJson;
    }

    public String getAttachedTags() {
        return attachedTags;
    }

    public void setAttachedTags(String attachedTags) {
        this.attachedTags = attachedTags;
    }

    public String getAttachedTagsToNote() {
        return attachedTagsToNote;
    }

    public void setAttachedTagsToNote(String attachedTagsToNote) {
        this.attachedTagsToNote = attachedTagsToNote;
    }

    public String getRemovedTags() {
        return removedTags;
    }

    public void setRemovedTags(String removedTags) {
        this.removedTags = removedTags;
    }

    public String getRemovedTagsAfter() {
        return removedTagsAfter;
    }

    public void setRemovedTagsAfter(String removedTagsAfter) {
        this.removedTagsAfter = removedTagsAfter;
    }

    public String getRemovedTagsBefore() {
        return removedTagsBefore;
    }

    public void setRemovedTagsBefore(String removedTagsBefore) {
        this.removedTagsBefore = removedTagsBefore;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ProcessedOrderInfoStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessedOrderInfoStatus status) {
        this.status = status;
    }

    public Long getDelayedTaggingValue() {
        return delayedTaggingValue;
    }

    public void setDelayedTaggingValue(Long delayedTaggingValue) {
        this.delayedTaggingValue = delayedTaggingValue;
    }

    public ProcessedOrderInfoDelayedTaggingUnit getDelayedTaggingUnit() {
        return delayedTaggingUnit;
    }

    public void setDelayedTaggingUnit(ProcessedOrderInfoDelayedTaggingUnit delayedTaggingUnit) {
        this.delayedTaggingUnit = delayedTaggingUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessedOrderInfoDTO)) {
            return false;
        }

        return id != null && id.equals(((ProcessedOrderInfoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessedOrderInfoDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", orderId=" + getOrderId() +
            ", orderNumber=" + getOrderNumber() +
            ", topicName='" + getTopicName() + "'" +
            ", processedTime='" + getProcessedTime() + "'" +
            ", triggerRuleInfoJson='" + getTriggerRuleInfoJson() + "'" +
            ", attachedTags='" + getAttachedTags() + "'" +
            ", attachedTagsToNote='" + getAttachedTagsToNote() + "'" +
            ", removedTags='" + getRemovedTags() + "'" +
            ", removedTagsAfter='" + getRemovedTagsAfter() + "'" +
            ", removedTagsBefore='" + getRemovedTagsBefore() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", status='" + getStatus() + "'" +
            ", delayedTaggingValue=" + getDelayedTaggingValue() +
            ", delayedTaggingUnit='" + getDelayedTaggingUnit() + "'" +
            "}";
    }
}
