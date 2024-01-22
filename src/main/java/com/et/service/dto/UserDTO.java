package com.et.service.dto;

import com.et.constant.Constants;

import com.et.domain.Authority;
import com.et.domain.User;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private boolean showRevertThemeButton;
    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 700)
    private String login;

    @Size(max = 1000)
    private String firstName;

    @Size(max = 1000)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    private String dynamodbPassword;

    private boolean paymentPlanAvailable = true;
    private long remainingDaysOnTrial;
    private Long productCount;
    private Long allowedProductCount;
    private String planName;
    private String totalSalesGenerated;
    private Long planId;
    private double totalSalesGeneratedNumeric;
    private boolean installCollectionFilters;

    private boolean customPlanApply;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
    }

    public boolean isShowRevertThemeButton() {
        return showRevertThemeButton;
    }

    public void setShowRevertThemeButton(boolean showRevertThemeButton) {
        this.showRevertThemeButton = showRevertThemeButton;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public boolean isPaymentPlanAvailable() {
        return paymentPlanAvailable;
    }

    public void setPaymentPlanAvailable(boolean paymentPlanAvailable) {
        this.paymentPlanAvailable = paymentPlanAvailable;
    }

    public boolean isCustomPlanApply() {
        return customPlanApply;
    }

    public void setCustomPlanApply(boolean customPlanApply) {
        this.customPlanApply = customPlanApply;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            ", customPlanApply=" + customPlanApply +
            "}";
    }

    public void setRemainingDaysOnTrial(long remainingDaysOnTrial) {
        this.remainingDaysOnTrial = remainingDaysOnTrial;
    }

    public long getRemainingDaysOnTrial() {
        return remainingDaysOnTrial;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }

    public Long getProductCount() {
        return productCount;
    }

    public void setAllowedProductCount(Long allowedProductCount) {
        this.allowedProductCount = allowedProductCount;
    }

    public Long getAllowedProductCount() {
        return allowedProductCount;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setTotalSalesGenerated(String totalSalesGenerated) {
        this.totalSalesGenerated = totalSalesGenerated;
    }

    public String getTotalSalesGenerated() {
        return totalSalesGenerated;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setTotalSalesGeneratedNumeric(double totalSalesGeneratedNumeric) {
        this.totalSalesGeneratedNumeric = totalSalesGeneratedNumeric;
    }

    public double getTotalSalesGeneratedNumeric() {
        return totalSalesGeneratedNumeric;
    }

    public boolean isInstallCollectionFilters() {
        return installCollectionFilters;
    }

    public void setInstallCollectionFilters(boolean installCollectionFilters) {
        this.installCollectionFilters = installCollectionFilters;
    }

    public String getDynamodbPassword() {
        return dynamodbPassword;
    }

    public void setDynamodbPassword(String dynamodbPassword) {
        this.dynamodbPassword = dynamodbPassword;
    }
}
