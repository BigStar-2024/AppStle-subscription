package com.et.domain;

import javax.persistence.*;

import java.io.Serializable;

import com.et.domain.enumeration.CustomizationType;

import com.et.domain.enumeration.CustomizationCategory;

/**
 * A Customization.
 */
@Entity
@Table(name = "customization")
public class Customization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CustomizationType type;

    @Lob
    @Column(name = "css")
    private String css;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CustomizationCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Customization label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CustomizationType getType() {
        return type;
    }

    public Customization type(CustomizationType type) {
        this.type = type;
        return this;
    }

    public void setType(CustomizationType type) {
        this.type = type;
    }

    public String getCss() {
        return css;
    }

    public Customization css(String css) {
        this.css = css;
        return this;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public CustomizationCategory getCategory() {
        return category;
    }

    public Customization category(CustomizationCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(CustomizationCategory category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customization)) {
            return false;
        }
        return id != null && id.equals(((Customization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customization{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", type='" + getType() + "'" +
            ", css='" + getCss() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
