
package com.et.pojo;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "interval",
    "interval_count"
})
public class DeliveryPolicy implements Serializable
{

    @JsonProperty("interval")
    private String interval;
    @JsonProperty("interval_count")
    private Long intervalCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2261760258751964533L;

    @JsonProperty("interval")
    public String getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(String interval) {
        this.interval = interval;
    }

    @JsonProperty("interval_count")
    public Long getIntervalCount() {
        return intervalCount;
    }

    @JsonProperty("interval_count")
    public void setIntervalCount(Long intervalCount) {
        this.intervalCount = intervalCount;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("interval", interval).append("intervalCount", intervalCount).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(interval).append(additionalProperties).append(intervalCount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeliveryPolicy) == false) {
            return false;
        }
        DeliveryPolicy rhs = ((DeliveryPolicy) other);
        return new EqualsBuilder().append(interval, rhs.interval).append(additionalProperties, rhs.additionalProperties).append(intervalCount, rhs.intervalCount).isEquals();
    }

}
