package com.et.domain;


import javax.persistence.*;

import java.io.Serializable;

import com.et.domain.enumeration.EmailSettingType;

/**
 * A EmailTemplate.
 */
@Entity
@Table(name = "email_template")
public class EmailTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_type")
    private EmailSettingType emailType;

    @Lob
    @Column(name = "html")
    private String html;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailSettingType getEmailType() {
        return emailType;
    }

    public EmailTemplate emailType(EmailSettingType emailType) {
        this.emailType = emailType;
        return this;
    }

    public void setEmailType(EmailSettingType emailType) {
        this.emailType = emailType;
    }

    public String getHtml() {
        return html;
    }

    public EmailTemplate html(String html) {
        this.html = html;
        return this;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailTemplate)) {
            return false;
        }
        return id != null && id.equals(((EmailTemplate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailTemplate{" +
            "id=" + getId() +
            ", emailType='" + getEmailType() + "'" +
            ", html='" + getHtml() + "'" +
            "}";
    }
}
