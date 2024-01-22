package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.WidgetTemplateType;

/**
 * A WidgetTemplate.
 */
@Entity
@Table(name = "widget_template")
public class WidgetTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true)
    private WidgetTemplateType type;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "html")
    private String html;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WidgetTemplateType getType() {
        return type;
    }

    public WidgetTemplate type(WidgetTemplateType type) {
        this.type = type;
        return this;
    }

    public void setType(WidgetTemplateType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public WidgetTemplate title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public WidgetTemplate html(String html) {
        this.html = html;
        return this;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WidgetTemplate)) {
            return false;
        }
        return id != null && id.equals(((WidgetTemplate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WidgetTemplate{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", title='" + getTitle() + "'" +
            ", html='" + getHtml() + "'" +
            "}";
    }
}
