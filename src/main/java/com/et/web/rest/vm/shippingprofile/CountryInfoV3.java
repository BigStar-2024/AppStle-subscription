package com.et.web.rest.vm.shippingprofile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "code",
    "value",
    "label",
    "includeAllProvinces",
    "provinces"
})
@Generated("jsonschema2pojo")
public class CountryInfoV3 implements Serializable {
    @JsonProperty("value")
    private String value;
    @JsonProperty("label")
    private String label;
    @JsonProperty("includeAllProvinces")
    private Boolean includeAllProvinces;
    @JsonProperty("provinces")
    private List<Province> provinces;

    /**
     * No args constructor for use in serialization
     */
    public CountryInfoV3() {
    }

    /**
     * @param code
     * @param value
     * @param label
     * @param shouldIncludeAllProvince
     * @param provinces
     */
    public CountryInfoV3(String value, String label, Boolean includeAllProvinces, List<Province> provinces) {
        this.value = value;
        this.label = label;
        this.includeAllProvinces = includeAllProvinces;
        this.provinces = provinces;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("provinces")
    public List<Province> getProvinces() {
        return provinces;
    }

    @JsonProperty("provinces")
    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    @JsonProperty("includeAllProvinces")
    public Boolean getIncludeAllProvinces() {
        return includeAllProvinces;
    }

    @JsonProperty("includeAllProvinces")
    public void setIncludeAllProvinces(Boolean includeAllProvinces) {
        this.includeAllProvinces = includeAllProvinces;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CountryInfoV3.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null) ? "<null>" : this.value));
        sb.append(',');
        sb.append("label");
        sb.append('=');
        sb.append(((this.label == null) ? "<null>" : this.label));
        sb.append(',');
        sb.append("includeAllProvinces");
        sb.append('=');
        sb.append(((this.includeAllProvinces == null) ? "<null>" : this.includeAllProvinces));
        sb.append(',');
        sb.append("provinces");
        sb.append("=");
        sb.append(this.provinces == null ? "<null>" : "[" + this.provinces.toString() + "]");
        sb.append(",");
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryInfoV3 that = (CountryInfoV3) o;
        return Objects.equals(value, that.value) && Objects.equals(includeAllProvinces, that.includeAllProvinces) && Objects.equals(provinces, that.provinces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, includeAllProvinces, provinces);
    }
}
