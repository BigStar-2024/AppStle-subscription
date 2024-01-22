package com.et.service.dto;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.et.domain.CurrencyConversionInfo} entity. This class is used
 * in {@link com.et.web.rest.CurrencyConversionInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /currency-conversion-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyConversionInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter from;

    private StringFilter to;

    private LocalDateFilter storedOn;

    private DoubleFilter currencyRate;

    public CurrencyConversionInfoCriteria(){
    }

    public CurrencyConversionInfoCriteria(CurrencyConversionInfoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.from = other.from == null ? null : other.from.copy();
        this.to = other.to == null ? null : other.to.copy();
        this.storedOn = other.storedOn == null ? null : other.storedOn.copy();
        this.currencyRate = other.currencyRate == null ? null : other.currencyRate.copy();
    }

    @Override
    public CurrencyConversionInfoCriteria copy() {
        return new CurrencyConversionInfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFrom() {
        return from;
    }

    public void setFrom(StringFilter from) {
        this.from = from;
    }

    public StringFilter getTo() {
        return to;
    }

    public void setTo(StringFilter to) {
        this.to = to;
    }

    public LocalDateFilter getStoredOn() {
        return storedOn;
    }

    public void setStoredOn(LocalDateFilter storedOn) {
        this.storedOn = storedOn;
    }

    public DoubleFilter getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(DoubleFilter currencyRate) {
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
        final CurrencyConversionInfoCriteria that = (CurrencyConversionInfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(from, that.from) &&
            Objects.equals(to, that.to) &&
            Objects.equals(storedOn, that.storedOn) &&
            Objects.equals(currencyRate, that.currencyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        from,
        to,
        storedOn,
        currencyRate
        );
    }

    @Override
    public String toString() {
        return "CurrencyConversionInfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (from != null ? "from=" + from + ", " : "") +
                (to != null ? "to=" + to + ", " : "") +
                (storedOn != null ? "storedOn=" + storedOn + ", " : "") +
                (currencyRate != null ? "currencyRate=" + currencyRate + ", " : "") +
            "}";
    }

}
