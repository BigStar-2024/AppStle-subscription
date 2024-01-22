package com.et.liquid;

import com.et.api.shopify.shop.Shop;
import com.et.pojo.OrderItem;
import com.et.web.rest.vm.AttributeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailModel {

    private String subject;
    private String header_text_color;
    private String text_color;
    private String footer_text_color;
    private String footer_link_color;
    private Integer button_border_radius;
    private String heading;
    private String logo_url;
    private Shop shop;
    private String body_content;
    private String footer;
    private Customer customer;
    private List<OrderItem> orderItems = new ArrayList<>();
    private String maskedCardNumber;
    private String nextOrderDate;
    private String planType;
    private String cardLogo;
    private String text_image_url;
    private String manage_subscription_button_color;
    private String manage_subscription_button_text;
    private String manage_subscription_button_text_color;
    private String shipping_address_text;
    private String billing_address_text;
    private String next_orderdate_text;
    private String payment_method_text;
    private String ending_in_text;
    private String manageSubscriptionLink;

    private String heading_image_url;
    private String quantity_text;
    private String logoHeight;
    private String logoWidth;
    private String thanksImageHeight;
    private String thanksImageWidth;
    private String logoAlignment;
    private String thanksImageAlignment;
    private String shippingAddress;
    private String billingAddress;
    private List<String> bodyLines = new ArrayList<>();
    private List<String> shippingLines = new ArrayList<>();
    private List<String> billingLines = new ArrayList<>();
    private String shipping_address2;
    private String shipping_address1;
    private String shipping_city;
    private String shipping_province_code;
    private String shipping_zip;
    private String shipping_first_name;
    private String shipping_last_name;
    private String shipping_country;
    private String shipping_country_code;
    private String shipping_province;
    private String shipping_company;
    private String billing_address1;
    private String billing_city;
    private String billing_province_code;
    private String billing_zip;
    private String billing_full_name;
    private String billing_country;
    private String billing_country_code;
    private String billing_province;
    private String customer_token;
    private Long subscriptionContractId;
    private String cancellationReason;
    private String cancellationNote;
    private String note;
    private int deliveryIntervalCount;
    private String deliveryInterval;
    private int billingIntervalCount;
    private String billingInterval;
    private String selling_plan_name_text;
    private String variant_sku_text;
    private List<AttributeInfo> allAttributes = new ArrayList<>();
    private Map<String, String> customAttributes = new HashMap<>();
    private Double firstOrderAmount;
    private Double firstOrderAmountUSD;
    private String currencyCode;
    private Double contractAmount;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHeader_text_color() {
        return header_text_color;
    }

    public void setHeader_text_color(String header_text_color) {
        this.header_text_color = header_text_color;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public String getFooter_text_color() {
        return footer_text_color;
    }

    public void setFooter_text_color(String footer_text_color) {
        this.footer_text_color = footer_text_color;
    }

    public String getFooter_link_color() {
        return footer_link_color;
    }

    public void setFooter_link_color(String footer_link_color) {
        this.footer_link_color = footer_link_color;
    }

    public Integer getButton_border_radius() {
        return button_border_radius;
    }

    public void setButton_border_radius(Integer button_border_radius) {
        this.button_border_radius = button_border_radius;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getBody_content() {
        return body_content;
    }

    public void setBody_content(String body_content) {
        this.body_content = body_content;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getNextOrderDate() {
        return nextOrderDate;
    }

    public void setNextOrderDate(String nextOrderDate) {
        this.nextOrderDate = nextOrderDate;
    }

    public String getCardLogo() {
        return cardLogo;
    }

    public void setCardLogo(String cardLogo) {
        this.cardLogo = cardLogo;
    }

    public String getText_image_url() {
        return text_image_url;
    }

    public void setText_image_url(String text_image_url) {
        this.text_image_url = text_image_url;
    }

    public String getManage_subscription_button_text() {
        return manage_subscription_button_text;
    }

    public void setManage_subscription_button_text(String manage_subscription_button_text) {
        this.manage_subscription_button_text = manage_subscription_button_text;
    }

    public String getManage_subscription_button_text_color() {
        return manage_subscription_button_text_color;
    }

    public void setManage_subscription_button_text_color(String manage_subscription_button_text_color) {
        this.manage_subscription_button_text_color = manage_subscription_button_text_color;
    }

    public String getManage_subscription_button_color() {
        return manage_subscription_button_color;
    }

    public void setManage_subscription_button_color(String manage_subscription_button_color) {
        this.manage_subscription_button_color = manage_subscription_button_color;
    }

    public String getShipping_address_text() {
        return shipping_address_text;
    }

    public void setShipping_address_text(String shipping_address_text) {
        this.shipping_address_text = shipping_address_text;
    }

    public String getBilling_address_text() {
        return billing_address_text;
    }

    public void setBilling_address_text(String billing_address_text) {
        this.billing_address_text = billing_address_text;
    }

    public String getNext_orderdate_text() {
        return next_orderdate_text;
    }

    public void setNext_orderdate_text(String next_orderdate_text) {
        this.next_orderdate_text = next_orderdate_text;
    }

    public String getPayment_method_text() {
        return payment_method_text;
    }

    public void setPayment_method_text(String payment_method_text) {
        this.payment_method_text = payment_method_text;
    }

    public String getEnding_in_text() {
        return ending_in_text;
    }

    public void setEnding_in_text(String ending_in_text) {
        this.ending_in_text = ending_in_text;
    }

    public void setManageSubscriptionLink(String manageSubscriptionLink) {
        this.manageSubscriptionLink = manageSubscriptionLink;
    }

    public String getManageSubscriptionLink() {
        return manageSubscriptionLink;
    }

    public String getHeading_image_url() {
        return heading_image_url;
    }

    public void setHeading_image_url(String heading_image_url) {
        this.heading_image_url = heading_image_url;
    }

    public String getQuantity_text() {
        return quantity_text;
    }

    public void setQuantity_text(String quantity_text) {
        this.quantity_text = quantity_text;
    }

    public String getLogoHeight() {
        return logoHeight;
    }

    public void setLogoHeight(String logoHeight) {
        this.logoHeight = logoHeight;
    }

    public String getLogoWidth() {
        return logoWidth;
    }

    public void setLogoWidth(String logoWidth) {
        this.logoWidth = logoWidth;
    }

    public String getThanksImageHeight() {
        return thanksImageHeight;
    }

    public void setThanksImageHeight(String thanksImageHeight) {
        this.thanksImageHeight = thanksImageHeight;
    }

    public String getThanksImageWidth() {
        return thanksImageWidth;
    }

    public void setThanksImageWidth(String thanksImageWidth) {
        this.thanksImageWidth = thanksImageWidth;
    }

    public String getLogoAlignment() {
        return logoAlignment;
    }

    public void setLogoAlignment(String logoAlignment) {
        this.logoAlignment = logoAlignment;
    }

    public String getThanksImageAlignment() {
        return thanksImageAlignment;
    }

    public void setThanksImageAlignment(String thanksImageAlignment) {
        this.thanksImageAlignment = thanksImageAlignment;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setBodyLines(List<String> bodyLines) {
        this.bodyLines = bodyLines;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void setShippingLines(List<String> shippingLines) {
        this.shippingLines = shippingLines;
    }

    public List<String> getShippingLines() {
        return shippingLines;
    }

    public void setBillingLines(List<String> billingLines) {
        this.billingLines = billingLines;
    }

    public List<String> getBillingLines() {
        return billingLines;
    }

    public String getShipping_address2() {
        return shipping_address2;
    }

    public void setShipping_address2(String shipping_address2) {
        this.shipping_address2 = shipping_address2;
    }

    public String getShipping_address1() {
        return shipping_address1;
    }

    public void setShipping_address1(String shipping_address1) {
        this.shipping_address1 = shipping_address1;
    }

    public String getShipping_city() {
        return shipping_city;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public String getShipping_province_code() {
        return shipping_province_code;
    }

    public void setShipping_province_code(String shipping_province_code) {
        this.shipping_province_code = shipping_province_code;
    }

    public String getShipping_zip() {
        return shipping_zip;
    }

    public void setShipping_zip(String shipping_zip) {
        this.shipping_zip = shipping_zip;
    }

    public String getShipping_first_name() {
        return shipping_first_name;
    }

    public void setShipping_first_name(String shipping_first_name) {
        this.shipping_first_name = shipping_first_name;
    }

    public String getShipping_last_name() {
        return shipping_last_name;
    }

    public void setShipping_last_name(String shipping_last_name) {
        this.shipping_last_name = shipping_last_name;
    }

    public String getShipping_country() {
        return shipping_country;
    }

    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    public String getShipping_country_code() {
        return shipping_country_code;
    }

    public void setShipping_country_code(String shipping_country_code) {
        this.shipping_country_code = shipping_country_code;
    }

    public String getBilling_address1() {
        return billing_address1;
    }

    public void setBilling_address1(String billing_address1) {
        this.billing_address1 = billing_address1;
    }

    public String getBilling_city() {
        return billing_city;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }

    public String getBilling_province_code() {
        return billing_province_code;
    }

    public void setBilling_province_code(String billing_province_code) {
        this.billing_province_code = billing_province_code;
    }

    public String getBilling_zip() {
        return billing_zip;
    }

    public void setBilling_zip(String billing_zip) {
        this.billing_zip = billing_zip;
    }

    public String getBilling_full_name() {
        return billing_full_name;
    }

    public void setBilling_full_name(String billing_full_name) {
        this.billing_full_name = billing_full_name;
    }

    public String getBilling_country() {
        return billing_country;
    }

    public void setBilling_country(String billing_country) {
        this.billing_country = billing_country;
    }

    public String getBilling_country_code() {
        return billing_country_code;
    }

    public void setBilling_country_code(String billing_country_code) {
        this.billing_country_code = billing_country_code;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Long getSubscriptionContractId() {
        return subscriptionContractId;
    }

    public void setSubscriptionContractId(Long subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }

    public String getCustomer_token() {
        return customer_token;
    }

    public void setCustomer_token(String customer_token) {
        this.customer_token = customer_token;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDeliveryIntervalCount(int deliveryIntervalCount) {
        this.deliveryIntervalCount = deliveryIntervalCount;
    }

    public int getDeliveryIntervalCount() {
        return deliveryIntervalCount;
    }

    public void setDeliveryInterval(String deliveryInterval) {
        this.deliveryInterval = deliveryInterval;
    }

    public String getDeliveryInterval() {
        return deliveryInterval;
    }

    public void setBillingIntervalCount(int billingIntervalCount) {
        this.billingIntervalCount = billingIntervalCount;
    }

    public int getBillingIntervalCount() {
        return billingIntervalCount;
    }

    public void setBillingInterval(String billingInterval) {
        this.billingInterval = billingInterval;
    }

    public String getBillingInterval() {
        return billingInterval;
    }

    public String getShipping_province() {
        return shipping_province;
    }

    public void setShipping_province(String shipping_province) {
        this.shipping_province = shipping_province;
    }

    public String getBilling_province() {
        return billing_province;
    }

    public void setBilling_province(String billing_province) {
        this.billing_province = billing_province;
    }

    public String getShipping_company() {
        return shipping_company;
    }

    public void setShipping_company(String shipping_company) {
        this.shipping_company = shipping_company;
    }

    public String getSelling_plan_name_text() {
        return selling_plan_name_text;
    }

    public void setSelling_plan_name_text(String selling_plan_name_text) {
        this.selling_plan_name_text = selling_plan_name_text;
    }

    public String getVariant_sku_text() {
        return variant_sku_text;
    }

    public void setVariant_sku_text(String variant_sku_text) {
        this.variant_sku_text = variant_sku_text;
    }

    public List<AttributeInfo> getAllAttributes() {
        return allAttributes;
    }

    public void setAllAttributes(List<AttributeInfo> allAttributes) {
        this.allAttributes = allAttributes;
    }

    public Map<String, String> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(Map<String, String> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public Double getFirstOrderAmount() {
        return firstOrderAmount;
    }

    public void setFirstOrderAmount(Double firstOrderAmount) {
        this.firstOrderAmount = firstOrderAmount;
    }

    public Double getFirstOrderAmountUSD() {
        return firstOrderAmountUSD;
    }

    public void setFirstOrderAmountUSD(Double firstOrderAmountUSD) {
        this.firstOrderAmountUSD = firstOrderAmountUSD;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }
}
