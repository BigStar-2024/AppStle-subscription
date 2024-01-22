
package com.et.web.rest.vm;

import com.et.domain.enumeration.ProcessedOrderInfoStatus;
import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "orderNumber",
    "name",
    "orderId",
    "topic",
    "appendedTags",
    "removeTags",
    "remove_date",
    "complete_date",
    "tagRemovalInfo",
    "status"
})
public class ProcessedOrderInfoVM implements Serializable
{

    @JsonProperty("orderNumber")
    private Long orderNumber;
    @JsonProperty("name")
    private String name;
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("appendedTags")
    private List<String> appendedTags = new ArrayList<String>();
    @JsonProperty("removeTags")
    private List<String> removeTags = new ArrayList<String>();
    @JsonProperty("remove_date")
    private String removeDate;
    @JsonProperty("complete_date")
    private String completeDate;
    @JsonProperty("tagRemovalInfo")
    private List<TagRemovalInfo> tagRemovalInfo = new ArrayList<TagRemovalInfo>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4890732107021256730L;

    @JsonProperty("status")
    private ProcessedOrderInfoStatus status;
    private String delayedUntil;
    private String appendedToNote;

    @JsonProperty("orderNumber")
    public Long getOrderNumber() {
        return orderNumber;
    }

    @JsonProperty("orderNumber")
    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty("appendedTags")
    public List<String> getAppendedTags() {
        return appendedTags;
    }

    @JsonProperty("appendedTags")
    public void setAppendedTags(List<String> appendedTags) {
        this.appendedTags = appendedTags;
    }

    @JsonProperty("removeTags")
    public List<String> getRemoveTags() {
        return removeTags;
    }

    @JsonProperty("removeTags")
    public void setRemoveTags(List<String> removeTags) {
        this.removeTags = removeTags;
    }

    @JsonProperty("remove_date")
    public String getRemoveDate() {
        return removeDate;
    }

    @JsonProperty("remove_date")
    public void setRemoveDate(String removeDate) {
        this.removeDate = removeDate;
    }

    @JsonProperty("complete_date")
    public String getCompleteDate() {
        return completeDate;
    }

    @JsonProperty("complete_date")
    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    @JsonProperty("tagRemovalInfo")
    public List<TagRemovalInfo> getTagRemovalInfo() {
        return tagRemovalInfo;
    }

    @JsonProperty("tagRemovalInfo")
    public void setTagRemovalInfo(List<TagRemovalInfo> tagRemovalInfo) {
        this.tagRemovalInfo = tagRemovalInfo;
    }

    @JsonProperty("orderId")
    public Long getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("status")
    public void setStatus(ProcessedOrderInfoStatus status) {
        this.status = status;
    }

    @JsonProperty("status")
    public ProcessedOrderInfoStatus getStatus() {
        return status;
    }

    public void setDelayedUntil(String delayedUntil) {
        this.delayedUntil = delayedUntil;
    }

    public String getDelayedUntil() {
        return delayedUntil;
    }

    public void setAppendedToNote(String appendedToNote) {
        this.appendedToNote = appendedToNote;
    }

    public String getAppendedToNote() {
        return appendedToNote;
    }
}
