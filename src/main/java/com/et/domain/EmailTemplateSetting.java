package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.et.domain.enumeration.EmailSettingType;

/**
 * A EmailTemplateSetting.
 */
@Entity
@Table(name = "email_template_setting")
public class EmailTemplateSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_setting_type")
    private EmailSettingType emailSettingType;

    @NotNull
    @Column(name = "send_email_disabled", nullable = false)
    private Boolean sendEmailDisabled;

    
    @Lob
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @Column(name = "from_email", nullable = false)
    private String fromEmail;

    @NotNull
    @Column(name = "logo", nullable = false)
    private String logo;

    
    @Lob
    @Column(name = "heading", nullable = false)
    private String heading;

    @NotNull
    @Column(name = "heading_text_color", nullable = false)
    private String headingTextColor;

    @NotNull
    @Column(name = "content_text_color", nullable = false)
    private String contentTextColor;

    @NotNull
    @Column(name = "content_link_color", nullable = false)
    private String contentLinkColor;

    
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "footer_text_color", nullable = false)
    private String footerTextColor;

    @NotNull
    @Column(name = "footer_link_color", nullable = false)
    private String footerLinkColor;

    
    @Lob
    @Column(name = "footer_text", nullable = false)
    private String footerText;

    @NotNull
    @Column(name = "template_background_color", nullable = false)
    private String templateBackgroundColor;

    @Lob
    @Column(name = "html")
    private String html;

    @Column(name = "text_image_url")
    private String textImageUrl;

    @Column(name = "manage_subscription_button_color")
    private String manageSubscriptionButtonColor;

    @Column(name = "manage_subscription_button_text")
    private String manageSubscriptionButtonText;

    @Column(name = "manage_subscription_button_text_color")
    private String manageSubscriptionButtonTextColor;

    @Column(name = "shipping_address_text")
    private String shippingAddressText;

    @Column(name = "billing_address_text")
    private String billingAddressText;

    @Column(name = "next_orderdate_text")
    private String nextOrderdateText;

    @Column(name = "payment_method_text")
    private String paymentMethodText;

    @Column(name = "ending_in_text")
    private String endingInText;

    @Column(name = "bcc_email")
    private String bccEmail;

    @Column(name = "upcoming_order_email_buffer")
    private Integer upcomingOrderEmailBuffer;

    @Column(name = "heading_image_url")
    private String headingImageUrl;

    @Column(name = "quantity_text")
    private String quantityText;

    @Column(name = "manage_subscription_button_url")
    private String manageSubscriptionButtonUrl;

    @Column(name = "logo_height")
    private String logoHeight;

    @Column(name = "logo_width")
    private String logoWidth;

    @Column(name = "thanks_image_height")
    private String thanksImageHeight;

    @Column(name = "thanks_image_width")
    private String thanksImageWidth;

    @Column(name = "logo_alignment")
    private String logoAlignment;

    @Column(name = "thanks_image_alignment")
    private String thanksImageAlignment;

    
    @Lob
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    
    @Lob
    @Column(name = "billing_address", nullable = false)
    private String billingAddress;

    @Column(name = "reply_to")
    private String replyTo;

    @Column(name = "send_bcc_email_flag")
    private Boolean sendBCCEmailFlag;

    @Column(name = "selling_plan_name_text")
    private String sellingPlanNameText;

    @Column(name = "variant_sku_text")
    private String variantSkuText;

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

    public EmailTemplateSetting shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public EmailSettingType getEmailSettingType() {
        return emailSettingType;
    }

    public EmailTemplateSetting emailSettingType(EmailSettingType emailSettingType) {
        this.emailSettingType = emailSettingType;
        return this;
    }

    public void setEmailSettingType(EmailSettingType emailSettingType) {
        this.emailSettingType = emailSettingType;
    }

    public Boolean isSendEmailDisabled() {
        return sendEmailDisabled;
    }

    public EmailTemplateSetting sendEmailDisabled(Boolean sendEmailDisabled) {
        this.sendEmailDisabled = sendEmailDisabled;
        return this;
    }

    public void setSendEmailDisabled(Boolean sendEmailDisabled) {
        this.sendEmailDisabled = sendEmailDisabled;
    }

    public String getSubject() {
        return subject;
    }

    public EmailTemplateSetting subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public EmailTemplateSetting fromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
        return this;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getLogo() {
        return logo;
    }

    public EmailTemplateSetting logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getHeading() {
        return heading;
    }

    public EmailTemplateSetting heading(String heading) {
        this.heading = heading;
        return this;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getHeadingTextColor() {
        return headingTextColor;
    }

    public EmailTemplateSetting headingTextColor(String headingTextColor) {
        this.headingTextColor = headingTextColor;
        return this;
    }

    public void setHeadingTextColor(String headingTextColor) {
        this.headingTextColor = headingTextColor;
    }

    public String getContentTextColor() {
        return contentTextColor;
    }

    public EmailTemplateSetting contentTextColor(String contentTextColor) {
        this.contentTextColor = contentTextColor;
        return this;
    }

    public void setContentTextColor(String contentTextColor) {
        this.contentTextColor = contentTextColor;
    }

    public String getContentLinkColor() {
        return contentLinkColor;
    }

    public EmailTemplateSetting contentLinkColor(String contentLinkColor) {
        this.contentLinkColor = contentLinkColor;
        return this;
    }

    public void setContentLinkColor(String contentLinkColor) {
        this.contentLinkColor = contentLinkColor;
    }

    public String getContent() {
        return content;
    }

    public EmailTemplateSetting content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFooterTextColor() {
        return footerTextColor;
    }

    public EmailTemplateSetting footerTextColor(String footerTextColor) {
        this.footerTextColor = footerTextColor;
        return this;
    }

    public void setFooterTextColor(String footerTextColor) {
        this.footerTextColor = footerTextColor;
    }

    public String getFooterLinkColor() {
        return footerLinkColor;
    }

    public EmailTemplateSetting footerLinkColor(String footerLinkColor) {
        this.footerLinkColor = footerLinkColor;
        return this;
    }

    public void setFooterLinkColor(String footerLinkColor) {
        this.footerLinkColor = footerLinkColor;
    }

    public String getFooterText() {
        return footerText;
    }

    public EmailTemplateSetting footerText(String footerText) {
        this.footerText = footerText;
        return this;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public String getTemplateBackgroundColor() {
        return templateBackgroundColor;
    }

    public EmailTemplateSetting templateBackgroundColor(String templateBackgroundColor) {
        this.templateBackgroundColor = templateBackgroundColor;
        return this;
    }

    public void setTemplateBackgroundColor(String templateBackgroundColor) {
        this.templateBackgroundColor = templateBackgroundColor;
    }

    public String getHtml() {
        return html;
    }

    public EmailTemplateSetting html(String html) {
        this.html = html;
        return this;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTextImageUrl() {
        return textImageUrl;
    }

    public EmailTemplateSetting textImageUrl(String textImageUrl) {
        this.textImageUrl = textImageUrl;
        return this;
    }

    public void setTextImageUrl(String textImageUrl) {
        this.textImageUrl = textImageUrl;
    }

    public String getManageSubscriptionButtonColor() {
        return manageSubscriptionButtonColor;
    }

    public EmailTemplateSetting manageSubscriptionButtonColor(String manageSubscriptionButtonColor) {
        this.manageSubscriptionButtonColor = manageSubscriptionButtonColor;
        return this;
    }

    public void setManageSubscriptionButtonColor(String manageSubscriptionButtonColor) {
        this.manageSubscriptionButtonColor = manageSubscriptionButtonColor;
    }

    public String getManageSubscriptionButtonText() {
        return manageSubscriptionButtonText;
    }

    public EmailTemplateSetting manageSubscriptionButtonText(String manageSubscriptionButtonText) {
        this.manageSubscriptionButtonText = manageSubscriptionButtonText;
        return this;
    }

    public void setManageSubscriptionButtonText(String manageSubscriptionButtonText) {
        this.manageSubscriptionButtonText = manageSubscriptionButtonText;
    }

    public String getManageSubscriptionButtonTextColor() {
        return manageSubscriptionButtonTextColor;
    }

    public EmailTemplateSetting manageSubscriptionButtonTextColor(String manageSubscriptionButtonTextColor) {
        this.manageSubscriptionButtonTextColor = manageSubscriptionButtonTextColor;
        return this;
    }

    public void setManageSubscriptionButtonTextColor(String manageSubscriptionButtonTextColor) {
        this.manageSubscriptionButtonTextColor = manageSubscriptionButtonTextColor;
    }

    public String getShippingAddressText() {
        return shippingAddressText;
    }

    public EmailTemplateSetting shippingAddressText(String shippingAddressText) {
        this.shippingAddressText = shippingAddressText;
        return this;
    }

    public void setShippingAddressText(String shippingAddressText) {
        this.shippingAddressText = shippingAddressText;
    }

    public String getBillingAddressText() {
        return billingAddressText;
    }

    public EmailTemplateSetting billingAddressText(String billingAddressText) {
        this.billingAddressText = billingAddressText;
        return this;
    }

    public void setBillingAddressText(String billingAddressText) {
        this.billingAddressText = billingAddressText;
    }

    public String getNextOrderdateText() {
        return nextOrderdateText;
    }

    public EmailTemplateSetting nextOrderdateText(String nextOrderdateText) {
        this.nextOrderdateText = nextOrderdateText;
        return this;
    }

    public void setNextOrderdateText(String nextOrderdateText) {
        this.nextOrderdateText = nextOrderdateText;
    }

    public String getPaymentMethodText() {
        return paymentMethodText;
    }

    public EmailTemplateSetting paymentMethodText(String paymentMethodText) {
        this.paymentMethodText = paymentMethodText;
        return this;
    }

    public void setPaymentMethodText(String paymentMethodText) {
        this.paymentMethodText = paymentMethodText;
    }

    public String getEndingInText() {
        return endingInText;
    }

    public EmailTemplateSetting endingInText(String endingInText) {
        this.endingInText = endingInText;
        return this;
    }

    public void setEndingInText(String endingInText) {
        this.endingInText = endingInText;
    }

    public String getBccEmail() {
        return bccEmail;
    }

    public EmailTemplateSetting bccEmail(String bccEmail) {
        this.bccEmail = bccEmail;
        return this;
    }

    public void setBccEmail(String bccEmail) {
        this.bccEmail = bccEmail;
    }

    public Integer getUpcomingOrderEmailBuffer() {
        return upcomingOrderEmailBuffer;
    }

    public EmailTemplateSetting upcomingOrderEmailBuffer(Integer upcomingOrderEmailBuffer) {
        this.upcomingOrderEmailBuffer = upcomingOrderEmailBuffer;
        return this;
    }

    public void setUpcomingOrderEmailBuffer(Integer upcomingOrderEmailBuffer) {
        this.upcomingOrderEmailBuffer = upcomingOrderEmailBuffer;
    }

    public String getHeadingImageUrl() {
        return headingImageUrl;
    }

    public EmailTemplateSetting headingImageUrl(String headingImageUrl) {
        this.headingImageUrl = headingImageUrl;
        return this;
    }

    public void setHeadingImageUrl(String headingImageUrl) {
        this.headingImageUrl = headingImageUrl;
    }

    public String getQuantityText() {
        return quantityText;
    }

    public EmailTemplateSetting quantityText(String quantityText) {
        this.quantityText = quantityText;
        return this;
    }

    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    public String getManageSubscriptionButtonUrl() {
        return manageSubscriptionButtonUrl;
    }

    public EmailTemplateSetting manageSubscriptionButtonUrl(String manageSubscriptionButtonUrl) {
        this.manageSubscriptionButtonUrl = manageSubscriptionButtonUrl;
        return this;
    }

    public void setManageSubscriptionButtonUrl(String manageSubscriptionButtonUrl) {
        this.manageSubscriptionButtonUrl = manageSubscriptionButtonUrl;
    }

    public String getLogoHeight() {
        return logoHeight;
    }

    public EmailTemplateSetting logoHeight(String logoHeight) {
        this.logoHeight = logoHeight;
        return this;
    }

    public void setLogoHeight(String logoHeight) {
        this.logoHeight = logoHeight;
    }

    public String getLogoWidth() {
        return logoWidth;
    }

    public EmailTemplateSetting logoWidth(String logoWidth) {
        this.logoWidth = logoWidth;
        return this;
    }

    public void setLogoWidth(String logoWidth) {
        this.logoWidth = logoWidth;
    }

    public String getThanksImageHeight() {
        return thanksImageHeight;
    }

    public EmailTemplateSetting thanksImageHeight(String thanksImageHeight) {
        this.thanksImageHeight = thanksImageHeight;
        return this;
    }

    public void setThanksImageHeight(String thanksImageHeight) {
        this.thanksImageHeight = thanksImageHeight;
    }

    public String getThanksImageWidth() {
        return thanksImageWidth;
    }

    public EmailTemplateSetting thanksImageWidth(String thanksImageWidth) {
        this.thanksImageWidth = thanksImageWidth;
        return this;
    }

    public void setThanksImageWidth(String thanksImageWidth) {
        this.thanksImageWidth = thanksImageWidth;
    }

    public String getLogoAlignment() {
        return logoAlignment;
    }

    public EmailTemplateSetting logoAlignment(String logoAlignment) {
        this.logoAlignment = logoAlignment;
        return this;
    }

    public void setLogoAlignment(String logoAlignment) {
        this.logoAlignment = logoAlignment;
    }

    public String getThanksImageAlignment() {
        return thanksImageAlignment;
    }

    public EmailTemplateSetting thanksImageAlignment(String thanksImageAlignment) {
        this.thanksImageAlignment = thanksImageAlignment;
        return this;
    }

    public void setThanksImageAlignment(String thanksImageAlignment) {
        this.thanksImageAlignment = thanksImageAlignment;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public EmailTemplateSetting shippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public EmailTemplateSetting billingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public EmailTemplateSetting replyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Boolean isSendBCCEmailFlag() {
        return sendBCCEmailFlag;
    }

    public EmailTemplateSetting sendBCCEmailFlag(Boolean sendBCCEmailFlag) {
        this.sendBCCEmailFlag = sendBCCEmailFlag;
        return this;
    }

    public void setSendBCCEmailFlag(Boolean sendBCCEmailFlag) {
        this.sendBCCEmailFlag = sendBCCEmailFlag;
    }

    public String getSellingPlanNameText() {
        return sellingPlanNameText;
    }

    public EmailTemplateSetting sellingPlanNameText(String sellingPlanNameText) {
        this.sellingPlanNameText = sellingPlanNameText;
        return this;
    }

    public void setSellingPlanNameText(String sellingPlanNameText) {
        this.sellingPlanNameText = sellingPlanNameText;
    }

    public String getVariantSkuText() {
        return variantSkuText;
    }

    public EmailTemplateSetting variantSkuText(String variantSkuText) {
        this.variantSkuText = variantSkuText;
        return this;
    }

    public void setVariantSkuText(String variantSkuText) {
        this.variantSkuText = variantSkuText;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailTemplateSetting)) {
            return false;
        }
        return id != null && id.equals(((EmailTemplateSetting) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailTemplateSetting{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", emailSettingType='" + getEmailSettingType() + "'" +
            ", sendEmailDisabled='" + isSendEmailDisabled() + "'" +
            ", subject='" + getSubject() + "'" +
            ", fromEmail='" + getFromEmail() + "'" +
            ", logo='" + getLogo() + "'" +
            ", heading='" + getHeading() + "'" +
            ", headingTextColor='" + getHeadingTextColor() + "'" +
            ", contentTextColor='" + getContentTextColor() + "'" +
            ", contentLinkColor='" + getContentLinkColor() + "'" +
            ", content='" + getContent() + "'" +
            ", footerTextColor='" + getFooterTextColor() + "'" +
            ", footerLinkColor='" + getFooterLinkColor() + "'" +
            ", footerText='" + getFooterText() + "'" +
            ", templateBackgroundColor='" + getTemplateBackgroundColor() + "'" +
            ", html='" + getHtml() + "'" +
            ", textImageUrl='" + getTextImageUrl() + "'" +
            ", manageSubscriptionButtonColor='" + getManageSubscriptionButtonColor() + "'" +
            ", manageSubscriptionButtonText='" + getManageSubscriptionButtonText() + "'" +
            ", manageSubscriptionButtonTextColor='" + getManageSubscriptionButtonTextColor() + "'" +
            ", shippingAddressText='" + getShippingAddressText() + "'" +
            ", billingAddressText='" + getBillingAddressText() + "'" +
            ", nextOrderdateText='" + getNextOrderdateText() + "'" +
            ", paymentMethodText='" + getPaymentMethodText() + "'" +
            ", endingInText='" + getEndingInText() + "'" +
            ", bccEmail='" + getBccEmail() + "'" +
            ", upcomingOrderEmailBuffer=" + getUpcomingOrderEmailBuffer() +
            ", headingImageUrl='" + getHeadingImageUrl() + "'" +
            ", quantityText='" + getQuantityText() + "'" +
            ", manageSubscriptionButtonUrl='" + getManageSubscriptionButtonUrl() + "'" +
            ", logoHeight='" + getLogoHeight() + "'" +
            ", logoWidth='" + getLogoWidth() + "'" +
            ", thanksImageHeight='" + getThanksImageHeight() + "'" +
            ", thanksImageWidth='" + getThanksImageWidth() + "'" +
            ", logoAlignment='" + getLogoAlignment() + "'" +
            ", thanksImageAlignment='" + getThanksImageAlignment() + "'" +
            ", shippingAddress='" + getShippingAddress() + "'" +
            ", billingAddress='" + getBillingAddress() + "'" +
            ", replyTo='" + getReplyTo() + "'" +
            ", sendBCCEmailFlag='" + isSendBCCEmailFlag() + "'" +
            ", sellingPlanNameText='" + getSellingPlanNameText() + "'" +
            ", variantSkuText='" + getVariantSkuText() + "'" +
            "}";
    }
}
