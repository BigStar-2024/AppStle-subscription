package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.SmsSettingType;

/**
 * A SmsTemplateSetting.
 */
@Entity
@Table(name = "sms_template_setting")
public class SmsTemplateSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Enumerated(EnumType.STRING)
    @Column(name = "sms_setting_type")
    private SmsSettingType smsSettingType;

    @NotNull
    @Column(name = "send_sms_disabled", nullable = false)
    private Boolean sendSmsDisabled;

    
    @Lob
    @Column(name = "sms_content", nullable = false)
    private String smsContent;

    @Column(name = "stop_reply_sms")
    private Boolean stopReplySMS;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public SmsTemplateSetting shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public SmsSettingType getSmsSettingType() {
        return smsSettingType;
    }

    public SmsTemplateSetting smsSettingType(SmsSettingType smsSettingType) {
        this.smsSettingType = smsSettingType;
        return this;
    }

    public void setSmsSettingType(SmsSettingType smsSettingType) {
        this.smsSettingType = smsSettingType;
    }

    public Boolean isSendSmsDisabled() {
        return sendSmsDisabled;
    }

    public SmsTemplateSetting sendSmsDisabled(Boolean sendSmsDisabled) {
        this.sendSmsDisabled = sendSmsDisabled;
        return this;
    }

    public void setSendSmsDisabled(Boolean sendSmsDisabled) {
        this.sendSmsDisabled = sendSmsDisabled;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public SmsTemplateSetting smsContent(String smsContent) {
        this.smsContent = smsContent;
        return this;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Boolean isStopReplySMS() {
        return stopReplySMS;
    }

    public SmsTemplateSetting stopReplySMS(Boolean stopReplySMS) {
        this.stopReplySMS = stopReplySMS;
        return this;
    }

    public void setStopReplySMS(Boolean stopReplySMS) {
        this.stopReplySMS = stopReplySMS;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsTemplateSetting)) {
            return false;
        }
        return id != null && id.equals(((SmsTemplateSetting) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplateSetting{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", smsSettingType='" + getSmsSettingType() + "'" +
            ", sendSmsDisabled='" + isSendSmsDisabled() + "'" +
            ", smsContent='" + getSmsContent() + "'" +
            ", stopReplySMS='" + isStopReplySMS() + "'" +
            "}";
    }
}
