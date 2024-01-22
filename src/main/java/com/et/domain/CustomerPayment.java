package com.et.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.et.domain.enumeration.cardExpireEmailSentStatus;

import com.et.domain.enumeration.CardExpireSmsSentStatus;

/**
 * A CustomerPayment.
 */
@Entity
@Table(name = "customer_payment")
public class CustomerPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop")
    private String shop;

    @Column(name = "admin_graphql_api_id")
    private String adminGraphqlApiId;

    @Column(name = "token")
    private String token;

    @Column(name = "token_created_time")
    private ZonedDateTime tokenCreatedTime;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "admin_graphql_api_customer_id")
    private String adminGraphqlApiCustomerId;

    @Column(name = "instrument_type")
    private String instrumentType;

    @Column(name = "payment_instrument_last_digits")
    private Long paymentInstrumentLastDigits;

    @Column(name = "payment_instrument_month")
    private Long paymentInstrumentMonth;

    @Column(name = "payment_instrument_year")
    private Long paymentInstrumentYear;

    @Column(name = "payment_instrument_name")
    private String paymentInstrumentName;

    @Column(name = "payment_instruments_brand")
    private String paymentInstrumentsBrand;

    @Column(name = "customer_uid")
    private String customerUid;

    @NotNull
    @Column(name = "card_expiry_notification_counter", nullable = false)
    private Long cardExpiryNotificationCounter;

    @Column(name = "card_expiry_notification_first_sent")
    private ZonedDateTime cardExpiryNotificationFirstSent;

    @Column(name = "card_expiry_notification_last_sent")
    private ZonedDateTime cardExpiryNotificationLastSent;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_expire_email_sent_status")
    private cardExpireEmailSentStatus cardExpireEmailSentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_expire_sms_sent_status")
    private CardExpireSmsSentStatus cardExpireSmsSentStatus;

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

    public CustomerPayment shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getAdminGraphqlApiId() {
        return adminGraphqlApiId;
    }

    public CustomerPayment adminGraphqlApiId(String adminGraphqlApiId) {
        this.adminGraphqlApiId = adminGraphqlApiId;
        return this;
    }

    public void setAdminGraphqlApiId(String adminGraphqlApiId) {
        this.adminGraphqlApiId = adminGraphqlApiId;
    }

    public String getToken() {
        return token;
    }

    public CustomerPayment token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getTokenCreatedTime() {
        return tokenCreatedTime;
    }

    public void setTokenCreatedTime(ZonedDateTime tokenCreatedTime) {
        this.tokenCreatedTime = tokenCreatedTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public CustomerPayment customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAdminGraphqlApiCustomerId() {
        return adminGraphqlApiCustomerId;
    }

    public CustomerPayment adminGraphqlApiCustomerId(String adminGraphqlApiCustomerId) {
        this.adminGraphqlApiCustomerId = adminGraphqlApiCustomerId;
        return this;
    }

    public void setAdminGraphqlApiCustomerId(String adminGraphqlApiCustomerId) {
        this.adminGraphqlApiCustomerId = adminGraphqlApiCustomerId;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public CustomerPayment instrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
        return this;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public Long getPaymentInstrumentLastDigits() {
        return paymentInstrumentLastDigits;
    }

    public CustomerPayment paymentInstrumentLastDigits(Long paymentInstrumentLastDigits) {
        this.paymentInstrumentLastDigits = paymentInstrumentLastDigits;
        return this;
    }

    public void setPaymentInstrumentLastDigits(Long paymentInstrumentLastDigits) {
        this.paymentInstrumentLastDigits = paymentInstrumentLastDigits;
    }

    public Long getPaymentInstrumentMonth() {
        return paymentInstrumentMonth;
    }

    public CustomerPayment paymentInstrumentMonth(Long paymentInstrumentMonth) {
        this.paymentInstrumentMonth = paymentInstrumentMonth;
        return this;
    }

    public void setPaymentInstrumentMonth(Long paymentInstrumentMonth) {
        this.paymentInstrumentMonth = paymentInstrumentMonth;
    }

    public Long getPaymentInstrumentYear() {
        return paymentInstrumentYear;
    }

    public CustomerPayment paymentInstrumentYear(Long paymentInstrumentYear) {
        this.paymentInstrumentYear = paymentInstrumentYear;
        return this;
    }

    public void setPaymentInstrumentYear(Long paymentInstrumentYear) {
        this.paymentInstrumentYear = paymentInstrumentYear;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public CustomerPayment paymentInstrumentName(String paymentInstrumentName) {
        this.paymentInstrumentName = paymentInstrumentName;
        return this;
    }

    public void setPaymentInstrumentName(String paymentInstrumentName) {
        this.paymentInstrumentName = paymentInstrumentName;
    }

    public String getPaymentInstrumentsBrand() {
        return paymentInstrumentsBrand;
    }

    public CustomerPayment paymentInstrumentsBrand(String paymentInstrumentsBrand) {
        this.paymentInstrumentsBrand = paymentInstrumentsBrand;
        return this;
    }

    public void setPaymentInstrumentsBrand(String paymentInstrumentsBrand) {
        this.paymentInstrumentsBrand = paymentInstrumentsBrand;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public CustomerPayment customerUid(String customerUid) {
        this.customerUid = customerUid;
        return this;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public Long getCardExpiryNotificationCounter() {
        return cardExpiryNotificationCounter;
    }

    public CustomerPayment cardExpiryNotificationCounter(Long cardExpiryNotificationCounter) {
        this.cardExpiryNotificationCounter = cardExpiryNotificationCounter;
        return this;
    }

    public void setCardExpiryNotificationCounter(Long cardExpiryNotificationCounter) {
        this.cardExpiryNotificationCounter = cardExpiryNotificationCounter;
    }

    public ZonedDateTime getCardExpiryNotificationFirstSent() {
        return cardExpiryNotificationFirstSent;
    }

    public CustomerPayment cardExpiryNotificationFirstSent(ZonedDateTime cardExpiryNotificationFirstSent) {
        this.cardExpiryNotificationFirstSent = cardExpiryNotificationFirstSent;
        return this;
    }

    public void setCardExpiryNotificationFirstSent(ZonedDateTime cardExpiryNotificationFirstSent) {
        this.cardExpiryNotificationFirstSent = cardExpiryNotificationFirstSent;
    }

    public ZonedDateTime getCardExpiryNotificationLastSent() {
        return cardExpiryNotificationLastSent;
    }

    public CustomerPayment cardExpiryNotificationLastSent(ZonedDateTime cardExpiryNotificationLastSent) {
        this.cardExpiryNotificationLastSent = cardExpiryNotificationLastSent;
        return this;
    }

    public void setCardExpiryNotificationLastSent(ZonedDateTime cardExpiryNotificationLastSent) {
        this.cardExpiryNotificationLastSent = cardExpiryNotificationLastSent;
    }

    public cardExpireEmailSentStatus getCardExpireEmailSentStatus() {
        return cardExpireEmailSentStatus;
    }

    public CustomerPayment cardExpireEmailSentStatus(cardExpireEmailSentStatus cardExpireEmailSentStatus) {
        this.cardExpireEmailSentStatus = cardExpireEmailSentStatus;
        return this;
    }

    public void setCardExpireEmailSentStatus(cardExpireEmailSentStatus cardExpireEmailSentStatus) {
        this.cardExpireEmailSentStatus = cardExpireEmailSentStatus;
    }

    public CardExpireSmsSentStatus getCardExpireSmsSentStatus() {
        return cardExpireSmsSentStatus;
    }

    public CustomerPayment cardExpireSmsSentStatus(CardExpireSmsSentStatus cardExpireSmsSentStatus) {
        this.cardExpireSmsSentStatus = cardExpireSmsSentStatus;
        return this;
    }

    public void setCardExpireSmsSentStatus(CardExpireSmsSentStatus cardExpireSmsSentStatus) {
        this.cardExpireSmsSentStatus = cardExpireSmsSentStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerPayment)) {
            return false;
        }
        return id != null && id.equals(((CustomerPayment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerPayment{" +
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
