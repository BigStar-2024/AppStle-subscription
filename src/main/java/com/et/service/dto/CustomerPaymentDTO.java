package com.et.service.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.et.domain.enumeration.cardExpireEmailSentStatus;
import com.et.domain.enumeration.CardExpireSmsSentStatus;

/**
 * A DTO for the {@link com.et.domain.CustomerPayment} entity.
 */
public class CustomerPaymentDTO implements Serializable {

    private Long id;

    private String shop;

    private String adminGraphqlApiId;

    private String token;

    private ZonedDateTime tokenCreatedTime;

    private Long customerId;

    private String adminGraphqlApiCustomerId;

    private String instrumentType;

    private Long paymentInstrumentLastDigits;

    private Long paymentInstrumentMonth;

    private Long paymentInstrumentYear;

    private String paymentInstrumentName;

    private String paymentInstrumentsBrand;

    private String customerUid;

    @NotNull
    private Long cardExpiryNotificationCounter;

    private ZonedDateTime cardExpiryNotificationFirstSent;

    private ZonedDateTime cardExpiryNotificationLastSent;

    private cardExpireEmailSentStatus cardExpireEmailSentStatus;

    private CardExpireSmsSentStatus cardExpireSmsSentStatus;


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

    public String getAdminGraphqlApiId() {
        return adminGraphqlApiId;
    }

    public void setAdminGraphqlApiId(String adminGraphqlApiId) {
        this.adminGraphqlApiId = adminGraphqlApiId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAdminGraphqlApiCustomerId() {
        return adminGraphqlApiCustomerId;
    }

    public void setAdminGraphqlApiCustomerId(String adminGraphqlApiCustomerId) {
        this.adminGraphqlApiCustomerId = adminGraphqlApiCustomerId;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Long getPaymentInstrumentLastDigits() {
        return paymentInstrumentLastDigits;
    }

    public void setPaymentInstrumentLastDigits(Long paymentInstrumentLastDigits) {
        this.paymentInstrumentLastDigits = paymentInstrumentLastDigits;
    }

    public Long getPaymentInstrumentMonth() {
        return paymentInstrumentMonth;
    }

    public void setPaymentInstrumentMonth(Long paymentInstrumentMonth) {
        this.paymentInstrumentMonth = paymentInstrumentMonth;
    }

    public Long getPaymentInstrumentYear() {
        return paymentInstrumentYear;
    }

    public void setPaymentInstrumentYear(Long paymentInstrumentYear) {
        this.paymentInstrumentYear = paymentInstrumentYear;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public void setPaymentInstrumentName(String paymentInstrumentName) {
        this.paymentInstrumentName = paymentInstrumentName;
    }

    public String getPaymentInstrumentsBrand() {
        return paymentInstrumentsBrand;
    }

    public void setPaymentInstrumentsBrand(String paymentInstrumentsBrand) {
        this.paymentInstrumentsBrand = paymentInstrumentsBrand;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public Long getCardExpiryNotificationCounter() {
        return cardExpiryNotificationCounter;
    }

    public void setCardExpiryNotificationCounter(Long cardExpiryNotificationCounter) {
        this.cardExpiryNotificationCounter = cardExpiryNotificationCounter;
    }

    public ZonedDateTime getCardExpiryNotificationFirstSent() {
        return cardExpiryNotificationFirstSent;
    }

    public void setCardExpiryNotificationFirstSent(ZonedDateTime cardExpiryNotificationFirstSent) {
        this.cardExpiryNotificationFirstSent = cardExpiryNotificationFirstSent;
    }

    public ZonedDateTime getCardExpiryNotificationLastSent() {
        return cardExpiryNotificationLastSent;
    }

    public void setCardExpiryNotificationLastSent(ZonedDateTime cardExpiryNotificationLastSent) {
        this.cardExpiryNotificationLastSent = cardExpiryNotificationLastSent;
    }

    public cardExpireEmailSentStatus getCardExpireEmailSentStatus() {
        return cardExpireEmailSentStatus;
    }

    public void setCardExpireEmailSentStatus(cardExpireEmailSentStatus cardExpireEmailSentStatus) {
        this.cardExpireEmailSentStatus = cardExpireEmailSentStatus;
    }

    public CardExpireSmsSentStatus getCardExpireSmsSentStatus() {
        return cardExpireSmsSentStatus;
    }

    public void setCardExpireSmsSentStatus(CardExpireSmsSentStatus cardExpireSmsSentStatus) {
        this.cardExpireSmsSentStatus = cardExpireSmsSentStatus;
    }

    public ZonedDateTime getTokenCreatedTime() {
        return tokenCreatedTime;
    }

    public void setTokenCreatedTime(ZonedDateTime tokenCreatedTime) {
        this.tokenCreatedTime = tokenCreatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerPaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((CustomerPaymentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerPaymentDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", adminGraphqlApiId='" + getAdminGraphqlApiId() + "'" +
            ", token='" + getToken() + "'" +
            ", customerId=" + getCustomerId() +
            ", adminGraphqlApiCustomerId='" + getAdminGraphqlApiCustomerId() + "'" +
            ", instrumentType='" + getInstrumentType() + "'" +
            ", paymentInstrumentLastDigits=" + getPaymentInstrumentLastDigits() +
            ", paymentInstrumentMonth=" + getPaymentInstrumentMonth() +
            ", paymentInstrumentYear=" + getPaymentInstrumentYear() +
            ", paymentInstrumentName='" + getPaymentInstrumentName() + "'" +
            ", paymentInstrumentsBrand='" + getPaymentInstrumentsBrand() + "'" +
            ", customerUid='" + getCustomerUid() + "'" +
            ", cardExpiryNotificationCounter=" + getCardExpiryNotificationCounter() +
            ", cardExpiryNotificationFirstSent='" + getCardExpiryNotificationFirstSent() + "'" +
            ", cardExpiryNotificationLastSent='" + getCardExpiryNotificationLastSent() + "'" +
            ", cardExpireEmailSentStatus='" + getCardExpireEmailSentStatus() + "'" +
            ", cardExpireSmsSentStatus='" + getCardExpireSmsSentStatus() + "'" +
            "}";
    }
}
