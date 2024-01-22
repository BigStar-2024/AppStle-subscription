package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.et.domain.CustomerPortalDynamicScript} entity.
 */
public class CustomerPortalDynamicScriptDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    @Lob
    private String dynamicScript;

    
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

    public String getDynamicScript() {
        return dynamicScript;
    }

    public void setDynamicScript(String dynamicScript) {
        this.dynamicScript = dynamicScript;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerPortalDynamicScriptDTO)) {
            return false;
        }

        return id != null && id.equals(((CustomerPortalDynamicScriptDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerPortalDynamicScriptDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", dynamicScript='" + getDynamicScript() + "'" +
            "}";
    }
}
