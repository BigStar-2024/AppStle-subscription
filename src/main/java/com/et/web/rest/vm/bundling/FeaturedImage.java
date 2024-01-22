
package com.et.web.rest.vm.bundling;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "aspect_ratio",
    "alt",
    "height",
    "url",
    "width"
})
@Generated("jsonschema2pojo")
public class FeaturedImage implements Serializable
{

    @JsonProperty("aspect_ratio")
    private Long aspectRatio;
    @JsonProperty("alt")
    private String alt;
    @JsonProperty("height")
    private Long height;
    @JsonProperty("url")
    private String url;
    @JsonProperty("width")
    private Long width;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 7460579309157148880L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FeaturedImage() {
    }

    /**
     * 
     * @param alt
     * @param width
     * @param aspectRatio
     * @param url
     * @param height
     */
    public FeaturedImage(Long aspectRatio, String alt, Long height, String url, Long width) {
        super();
        this.aspectRatio = aspectRatio;
        this.alt = alt;
        this.height = height;
        this.url = url;
        this.width = width;
    }

    @JsonProperty("aspect_ratio")
    public Long getAspectRatio() {
        return aspectRatio;
    }

    @JsonProperty("aspect_ratio")
    public void setAspectRatio(Long aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @JsonProperty("alt")
    public String getAlt() {
        return alt;
    }

    @JsonProperty("alt")
    public void setAlt(String alt) {
        this.alt = alt;
    }

    @JsonProperty("height")
    public Long getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Long height) {
        this.height = height;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("width")
    public Long getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Long width) {
        this.width = width;
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
        StringBuilder sb = new StringBuilder();
        sb.append(FeaturedImage.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("aspectRatio");
        sb.append('=');
        sb.append(((this.aspectRatio == null)?"<null>":this.aspectRatio));
        sb.append(',');
        sb.append("alt");
        sb.append('=');
        sb.append(((this.alt == null)?"<null>":this.alt));
        sb.append(',');
        sb.append("height");
        sb.append('=');
        sb.append(((this.height == null)?"<null>":this.height));
        sb.append(',');
        sb.append("url");
        sb.append('=');
        sb.append(((this.url == null)?"<null>":this.url));
        sb.append(',');
        sb.append("width");
        sb.append('=');
        sb.append(((this.width == null)?"<null>":this.width));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.alt == null)? 0 :this.alt.hashCode()));
        result = ((result* 31)+((this.width == null)? 0 :this.width.hashCode()));
        result = ((result* 31)+((this.aspectRatio == null)? 0 :this.aspectRatio.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.url == null)? 0 :this.url.hashCode()));
        result = ((result* 31)+((this.height == null)? 0 :this.height.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FeaturedImage) == false) {
            return false;
        }
        FeaturedImage rhs = ((FeaturedImage) other);
        return (((((((this.alt == rhs.alt)||((this.alt!= null)&&this.alt.equals(rhs.alt)))&&((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width))))&&((this.aspectRatio == rhs.aspectRatio)||((this.aspectRatio!= null)&&this.aspectRatio.equals(rhs.aspectRatio))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.url == rhs.url)||((this.url!= null)&&this.url.equals(rhs.url))))&&((this.height == rhs.height)||((this.height!= null)&&this.height.equals(rhs.height))));
    }

}
