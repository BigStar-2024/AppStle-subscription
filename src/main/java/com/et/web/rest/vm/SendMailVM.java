package com.et.web.rest.vm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class SendMailVM {

    @Email
    private String customerEmailAddress;

    @NotNull
    private String Subject;

    @NotNull
    private String htmlBody;

    @NotNull
    private String shopName;

    @NotNull
    private long emailTemplateSettingId;

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public void setCustomerEmailAddress(String customerEmailAddress) {
        this.customerEmailAddress = customerEmailAddress;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getEmailTemplateSettingId() {
        return emailTemplateSettingId;
    }

    public void setEmailTemplateSettingId(long emailTemplateSettingId) {
        this.emailTemplateSettingId = emailTemplateSettingId;
    }

    @Override
    public String toString() {
        return "SendMailVM{" +
            "customerEmailAddress='" + customerEmailAddress + '\'' +
            ", Subject='" + Subject + '\'' +
            ", htmlBody='" + htmlBody + '\'' +
            ", shopName='" + shopName + '\'' +
            ", emailTemplateSettingId=" + emailTemplateSettingId +
            '}';
    }
}
