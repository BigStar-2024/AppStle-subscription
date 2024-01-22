
package com.et.web.rest.vm.mailgun;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cached",
    "priority",
    "record_type",
    "valid",
    "value"
})
public class ReceivingDnsRecord {

    @JsonProperty("cached")
    private List<Object> cached = null;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("record_type")
    private String recordType;
    @JsonProperty("valid")
    private String valid;
    @JsonProperty("value")
    private String value;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("cached")
    public List<Object> getCached() {
        return cached;
    }

    @JsonProperty("cached")
    public void setCached(List<Object> cached) {
        this.cached = cached;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("record_type")
    public String getRecordType() {
        return recordType;
    }

    @JsonProperty("record_type")
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @JsonProperty("valid")
    public String getValid() {
        return valid;
    }

    @JsonProperty("valid")
    public void setValid(String valid) {
        this.valid = valid;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
