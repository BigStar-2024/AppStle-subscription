package com.et.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.et.domain.CurrencyConversionInfo} entity.
 */
public class CurrencyConversionInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String from;

    @NotNull
    private String to;

    private LocalDate storedOn;

    private Double currencyRate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getStoredOn() {
        return storedOn;
    }

    public void setStoredOn(LocalDate storedOn) {
        this.storedOn = storedOn;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyConversionInfoDTO currencyConversionInfoDTO = (CurrencyConversionInfoDTO) o;
        if (currencyConversionInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyConversionInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyConversionInfoDTO{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", storedOn='" + getStoredOn() + "'" +
            ", currencyRate=" + getCurrencyRate() +
            "}";
    }
}
