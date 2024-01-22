package com.et.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.CustomizationType;
import com.et.domain.enumeration.CustomizationCategory;

/**
 * A DTO for the {@link com.et.domain.Customization} entity.
 */
public class CustomizationDTO implements Serializable {

    private Long id;

    private String label;

    private CustomizationType type;

    @Lob
    private String css;

    private CustomizationCategory category;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CustomizationType getType() {
        return type;
    }

    public void setType(CustomizationType type) {
        this.type = type;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public CustomizationCategory getCategory() {
        return category;
    }

    public void setCategory(CustomizationCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomizationDTO customizationDTO = (CustomizationDTO) o;
        if (customizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomizationDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", type='" + getType() + "'" +
            ", css='" + getCss() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
