
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
    "interval_count",
    "min_cycles",
    "max_cycles"
})
public class BillingPolicy implements Serializable
{

    @JsonProperty("interval")
    private String interval;
    @JsonProperty("interval_count")
    private Long intervalCount;
    @JsonProperty("min_cycles")
    private Integer minCycles;
    @JsonProperty("max_cycles")
    private Integer maxCycles;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5619237924478068992L;

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

    @JsonProperty("min_cycles")
    public Integer getMinCycles() {
        return minCycles;
    }

    @JsonProperty("min_cycles")
    public void setMinCycles(Integer minCycles) {
        this.minCycles = minCycles;
    }

    @JsonProperty("max_cycles")
    public Integer getMaxCycles() {
        return maxCycles;
    }

    @JsonProperty("max_cycles")
    public void setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
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
        return new ToStringBuilder(this).append("interval", interval).append("intervalCount", intervalCount).append("minCycles", minCycles).append("maxCycles", maxCycles).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(interval).append(additionalProperties).append(maxCycles).append(intervalCount).append(minCycles).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BillingPolicy) == false) {
            return false;
        }
        BillingPolicy rhs = ((BillingPolicy) other);
        return new EqualsBuilder().append(interval, rhs.interval).append(additionalProperties, rhs.additionalProperties).append(maxCycles, rhs.maxCycles).append(intervalCount, rhs.intervalCount).append(minCycles, rhs.minCycles).isEquals();
    }

}
