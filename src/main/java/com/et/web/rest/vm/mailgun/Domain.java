
package com.et.web.rest.vm.mailgun;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "created_at",
    "id",
    "is_disabled",
    "name",
    "require_tls",
    "skip_verification",
    "smtp_login",
    "smtp_password",
    "spam_action",
    "state",
    "type",
    "web_prefix",
    "web_scheme",
    "wildcard"
})
public class Domain {

    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("id")
    private String id;
    @JsonProperty("is_disabled")
    private Boolean isDisabled;
    @JsonProperty("name")
    private String name;
    @JsonProperty("require_tls")
    private Boolean requireTls;
    @JsonProperty("skip_verification")
    private Boolean skipVerification;
    @JsonProperty("smtp_login")
    private String smtpLogin;
    @JsonProperty("smtp_password")
    private String smtpPassword;
    @JsonProperty("spam_action")
    private String spamAction;
    @JsonProperty("state")
    private String state;
    @JsonProperty("type")
    private String type;
    @JsonProperty("web_prefix")
    private String webPrefix;
    @JsonProperty("web_scheme")
    private String webScheme;
    @JsonProperty("wildcard")
    private Boolean wildcard;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("is_disabled")
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    @JsonProperty("is_disabled")
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("require_tls")
    public Boolean getRequireTls() {
        return requireTls;
    }

    @JsonProperty("require_tls")
    public void setRequireTls(Boolean requireTls) {
        this.requireTls = requireTls;
    }

    @JsonProperty("skip_verification")
    public Boolean getSkipVerification() {
        return skipVerification;
    }

    @JsonProperty("skip_verification")
    public void setSkipVerification(Boolean skipVerification) {
        this.skipVerification = skipVerification;
    }

    @JsonProperty("smtp_login")
    public String getSmtpLogin() {
        return smtpLogin;
    }

    @JsonProperty("smtp_login")
    public void setSmtpLogin(String smtpLogin) {
        this.smtpLogin = smtpLogin;
    }

    @JsonProperty("smtp_password")
    public String getSmtpPassword() {
        return smtpPassword;
    }

    @JsonProperty("smtp_password")
    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    @JsonProperty("spam_action")
    public String getSpamAction() {
        return spamAction;
    }

    @JsonProperty("spam_action")
    public void setSpamAction(String spamAction) {
        this.spamAction = spamAction;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("web_prefix")
    public String getWebPrefix() {
        return webPrefix;
    }

    @JsonProperty("web_prefix")
    public void setWebPrefix(String webPrefix) {
        this.webPrefix = webPrefix;
    }

    @JsonProperty("web_scheme")
    public String getWebScheme() {
        return webScheme;
    }

    @JsonProperty("web_scheme")
    public void setWebScheme(String webScheme) {
        this.webScheme = webScheme;
    }

    @JsonProperty("wildcard")
    public Boolean getWildcard() {
        return wildcard;
    }

    @JsonProperty("wildcard")
    public void setWildcard(Boolean wildcard) {
        this.wildcard = wildcard;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
