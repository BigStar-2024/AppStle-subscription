package com.et.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jhipster.swagger", ignoreUnknownFields = false)
@Component
public class Swagger {

    private String title = SwaggerDefaults.Swagger.title;

    private String description = SwaggerDefaults.Swagger.description;

    private String version = SwaggerDefaults.Swagger.version;

    private String termsOfServiceUrl = SwaggerDefaults.Swagger.termsOfServiceUrl;

    private String contactName = SwaggerDefaults.Swagger.contactName;

    private String contactUrl = SwaggerDefaults.Swagger.contactUrl;

    private String contactEmail = SwaggerDefaults.Swagger.contactEmail;

    private String license = SwaggerDefaults.Swagger.license;

    private String licenseUrl = SwaggerDefaults.Swagger.licenseUrl;

    private String defaultIncludePattern = SwaggerDefaults.Swagger.defaultIncludePattern;

    private String host = SwaggerDefaults.Swagger.host;

    private String[] protocols = SwaggerDefaults.Swagger.protocols;

    private boolean useDefaultResponseMessages = SwaggerDefaults.Swagger.useDefaultResponseMessages;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getDefaultIncludePattern() {
        return defaultIncludePattern;
    }

    public void setDefaultIncludePattern(String defaultIncludePattern) {
        this.defaultIncludePattern = defaultIncludePattern;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String[] getProtocols() {
        return protocols;
    }

    public void setProtocols(final String[] protocols) {
        this.protocols = protocols;
    }

    public boolean isUseDefaultResponseMessages() {
        return useDefaultResponseMessages;
    }

    public void setUseDefaultResponseMessages(final boolean useDefaultResponseMessages) {
        this.useDefaultResponseMessages = useDefaultResponseMessages;
    }
}
