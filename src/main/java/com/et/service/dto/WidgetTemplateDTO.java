package com.et.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.et.domain.enumeration.WidgetTemplateType;

/**
 * A DTO for the {@link com.et.domain.WidgetTemplate} entity.
 */
public class WidgetTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    private WidgetTemplateType type;

    private String title;

    @Lob
    private String html;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WidgetTemplateType getType() {
        return type;
    }

    public void setType(WidgetTemplateType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WidgetTemplateDTO widgetTemplateDTO = (WidgetTemplateDTO) o;
        if (widgetTemplateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), widgetTemplateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WidgetTemplateDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", title='" + getTitle() + "'" +
            ", html='" + getHtml() + "'" +
            "}";
    }
}
