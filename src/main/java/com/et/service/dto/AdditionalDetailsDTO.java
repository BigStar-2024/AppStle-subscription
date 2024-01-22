package com.et.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalDetailsDTO {

    private Long subscriptionCount;
    private Double subscriptionOrderAmount;
    private Boolean analytics;
    private Boolean enableSubscriptionManagement;
    private Boolean enableDunningManagement;
    private Boolean enableCustomerPortalSettings;
    private Boolean enableShippingProfiles;
    private Boolean enableProductSwapAutomation;
    private Boolean enableAdvancedSellingPlans;
    private Boolean enableSummaryReports;
    private Boolean enableCustomEmailDomain;
    private Boolean enableWidgetPlacement;
    private Boolean enableIntegrations;
    private Boolean enableSmsAlert;
    private Boolean enableCustomEmailHtml;
    private Boolean enableCancellationManagement;
    private Boolean enableBundling;
    private Boolean enableAutomation;
    private Boolean enableQuickActions;
    private Boolean enableExternalApi;

    private Boolean enableCartWidget;

    private Boolean enableAutoSync;

    private Boolean webhookAccess;

    public Long getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(Long subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }

    public Double getSubscriptionOrderAmount() {
        return subscriptionOrderAmount;
    }

    public void setSubscriptionOrderAmount(Double subscriptionOrderAmount) {
        this.subscriptionOrderAmount = subscriptionOrderAmount;
    }

    public Boolean getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Boolean analytics) {
        this.analytics = analytics;
    }

    public Boolean getEnableSubscriptionManagement() {
        return enableSubscriptionManagement;
    }

    public void setEnableSubscriptionManagement(Boolean enableSubscriptionManagement) {
        this.enableSubscriptionManagement = enableSubscriptionManagement;
    }

    public Boolean getEnableDunningManagement() {
        return enableDunningManagement;
    }

    public void setEnableDunningManagement(Boolean enableDunningManagement) {
        this.enableDunningManagement = enableDunningManagement;
    }

    public Boolean getEnableCustomerPortalSettings() {
        return enableCustomerPortalSettings;
    }

    public void setEnableCustomerPortalSettings(Boolean enableCustomerPortalSettings) {
        this.enableCustomerPortalSettings = enableCustomerPortalSettings;
    }

    public Boolean getEnableShippingProfiles() {
        return enableShippingProfiles;
    }

    public void setEnableShippingProfiles(Boolean enableShippingProfiles) {
        this.enableShippingProfiles = enableShippingProfiles;
    }

    public Boolean getEnableProductSwapAutomation() {
        return enableProductSwapAutomation;
    }

    public void setEnableProductSwapAutomation(Boolean enableProductSwapAutomation) {
        this.enableProductSwapAutomation = enableProductSwapAutomation;
    }

    public Boolean getEnableAdvancedSellingPlans() {
        return enableAdvancedSellingPlans;
    }

    public void setEnableAdvancedSellingPlans(Boolean enableAdvancedSellingPlans) {
        this.enableAdvancedSellingPlans = enableAdvancedSellingPlans;
    }

    public Boolean getEnableSummaryReports() {
        return enableSummaryReports;
    }

    public void setEnableSummaryReports(Boolean enableSummaryReports) {
        this.enableSummaryReports = enableSummaryReports;
    }

    public Boolean getEnableCustomEmailDomain() {
        return enableCustomEmailDomain;
    }

    public void setEnableCustomEmailDomain(Boolean enableCustomEmailDomain) {
        this.enableCustomEmailDomain = enableCustomEmailDomain;
    }

    public Boolean getEnableWidgetPlacement() {
        return enableWidgetPlacement;
    }

    public void setEnableWidgetPlacement(Boolean enableWidgetPlacement) {
        this.enableWidgetPlacement = enableWidgetPlacement;
    }

    public Boolean getEnableIntegrations() {
        return enableIntegrations;
    }

    public void setEnableIntegrations(Boolean enableIntegrations) {
        this.enableIntegrations = enableIntegrations;
    }

    public Boolean getEnableSmsAlert() {
        return enableSmsAlert;
    }

    public void setEnableSmsAlert(Boolean enableSmsAlert) {
        this.enableSmsAlert = enableSmsAlert;
    }

    public Boolean getEnableCustomEmailHtml() {
        return enableCustomEmailHtml;
    }

    public void setEnableCustomEmailHtml(Boolean enableCustomEmailHtml) {
        this.enableCustomEmailHtml = enableCustomEmailHtml;
    }

    public Boolean getEnableCancellationManagement() {
        return enableCancellationManagement;
    }

    public void setEnableCancellationManagement(Boolean enableCancellationManagement) {
        this.enableCancellationManagement = enableCancellationManagement;
    }

    public Boolean getEnableBundling() {
        return enableBundling;
    }

    public void setEnableBundling(Boolean enableBundling) {
        this.enableBundling = enableBundling;
    }

    public Boolean getEnableAutomation() {
        return enableAutomation;
    }

    public void setEnableAutomation(Boolean enableAutomation) {
        this.enableAutomation = enableAutomation;
    }

    public Boolean getEnableQuickActions() {
        return enableQuickActions;
    }

    public void setEnableQuickActions(Boolean enableQuickActions) {
        this.enableQuickActions = enableQuickActions;
    }

    public Boolean getEnableExternalApi() {
        return enableExternalApi;
    }

    public void setEnableExternalApi(Boolean enableExternalApi) {
        this.enableExternalApi = enableExternalApi;
    }

    public Boolean getEnableCartWidget() {
        return enableCartWidget;
    }

    public void setEnableCartWidget(Boolean enableCartWidget) {
        this.enableCartWidget = enableCartWidget;
    }

    public Boolean getEnableAutoSync() {
        return enableAutoSync;
    }

    public void setEnableAutoSync(Boolean enableAutoSync) {
        this.enableAutoSync = enableAutoSync;
    }

    public Boolean getWebhookAccess() {
        return webhookAccess;
    }

    public void setWebhookAccess(Boolean webhookAccess) {
        this.webhookAccess = webhookAccess;
    }
}
