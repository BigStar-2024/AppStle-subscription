
package com.et.web.rest.vm;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tag",
    "tense",
    "remove_date"
})
public class TagRemovalInfo implements Serializable
{

    @JsonProperty("tag")
    private String tag;
    @JsonProperty("tense")
    private String tense;
    @JsonProperty("remove_date")
    private String removeDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5274407519751827226L;

    public TagRemovalInfo() {
    }

    public TagRemovalInfo(String tag, String tense, String removeDate) {
        this.tag = tag;
        this.tense = tense;
        this.removeDate = removeDate;
    }

    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    @JsonProperty("tag")
    public void setTag(String tag) {
        this.tag = tag;
    }

    @JsonProperty("tense")
    public String getTense() {
        return tense;
    }

    @JsonProperty("tense")
    public void setTense(String tense) {
        this.tense = tense;
    }

    @JsonProperty("remove_date")
    public String getRemoveDate() {
        return removeDate;
    }

    @JsonProperty("remove_date")
    public void setRemoveDate(String removeDate) {
        this.removeDate = removeDate;
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
