package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CurrencyConversionInfo.
 */
@Entity
@Table(name = "currency_conversion_info")
public class CurrencyConversionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_from", nullable = false)
    private String from;

    @NotNull
    @Column(name = "jhi_to", nullable = false)
    private String to;

    @Column(name = "stored_on")
    private LocalDate storedOn;

    @Column(name = "currency_rate")
    private Double currencyRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public CurrencyConversionInfo from(String from) {
        this.from = from;
        return this;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public CurrencyConversionInfo to(String to) {
        this.to = to;
        return this;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getStoredOn() {
        return storedOn;
    }

    public CurrencyConversionInfo storedOn(LocalDate storedOn) {
        this.storedOn = storedOn;
        return this;
    }

    public void setStoredOn(LocalDate storedOn) {
        this.storedOn = storedOn;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public CurrencyConversionInfo currencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
        return this;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyConversionInfo)) {
            return false;
        }
        return id != null && id.equals(((CurrencyConversionInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CurrencyConversionInfo{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", storedOn='" + getStoredOn() + "'" +
            ", currencyRate=" + getCurrencyRate() +
            "}";
    }
}
