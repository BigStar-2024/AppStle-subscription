package com.et.web.rest.vm;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name"
})
public class RemoveTags implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2447464657702866697L;
  @JsonProperty("name")
  private String name;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("name")
  public String getName() {
      return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
      this.name = name;
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
