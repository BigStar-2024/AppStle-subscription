package com.et.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.SmsSettingType;

/**
 * A DTO for the {@link com.et.domain.SmsTemplateSetting} entity.
 */
public class SmsTemplateSettingDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String shop;

    private SmsSettingType smsSettingType;

    @NotNull
    private Boolean sendSmsDisabled;

    
    @Lob
    private String smsContent;

    private Boolean stopReplySMS;

    
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

    public SmsSettingType getSmsSettingType() {
        return smsSettingType;
    }

    public void setSmsSettingType(SmsSettingType smsSettingType) {
        this.smsSettingType = smsSettingType;
    }

    public Boolean isSendSmsDisabled() {
        return sendSmsDisabled;
    }

    public void setSendSmsDisabled(Boolean sendSmsDisabled) {
        this.sendSmsDisabled = sendSmsDisabled;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Boolean isStopReplySMS() {
        return stopReplySMS;
    }

    public void setStopReplySMS(Boolean stopReplySMS) {
        this.stopReplySMS = stopReplySMS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsTemplateSettingDTO)) {
            return false;
        }

        return id != null && id.equals(((SmsTemplateSettingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplateSettingDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", smsSettingType='" + getSmsSettingType() + "'" +
            ", sendSmsDisabled='" + isSendSmsDisabled() + "'" +
            ", smsContent='" + getSmsContent() + "'" +
            ", stopReplySMS='" + isStopReplySMS() + "'" +
            "}";
    }
}
