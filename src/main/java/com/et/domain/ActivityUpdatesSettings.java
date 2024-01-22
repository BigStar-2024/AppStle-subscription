package com.et.domain;


import com.et.domain.enumeration.SummaryReportFrequencyUnit;
import com.et.domain.enumeration.SummaryReportTimePeriodUnit;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A ActivityUpdatesSettings.
 */
@Entity
@Table(name = "activity_updates_settings")
public class ActivityUpdatesSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "shop", nullable = false)
    private String shop;

    @NotNull
    @Column(name = "summary_report_enabled", nullable = false)
    private Boolean summaryReportEnabled;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "summary_report_frequency", nullable = false)
    private SummaryReportFrequencyUnit summaryReportFrequency;

    @Column(name = "summary_report_deliver_to_email")
    private String summaryReportDeliverToEmail;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "summary_report_time_period", nullable = false)
    private SummaryReportTimePeriodUnit summaryReportTimePeriod;

    @Column(name = "summary_report_last_sent")
    private ZonedDateTime summaryReportLastSent;

    @Column(name = "summary_report_processing")
    private Boolean summaryReportProcessing;

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

    public ActivityUpdatesSettings shop(String shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Boolean isSummaryReportEnabled() {
        return summaryReportEnabled;
    }

    public ActivityUpdatesSettings summaryReportEnabled(Boolean summaryReportEnabled) {
        this.summaryReportEnabled = summaryReportEnabled;
        return this;
    }

    public void setSummaryReportEnabled(Boolean summaryReportEnabled) {
        this.summaryReportEnabled = summaryReportEnabled;
    }

    public SummaryReportFrequencyUnit getSummaryReportFrequency() {
        return summaryReportFrequency;
    }

    public ActivityUpdatesSettings summaryReportFrequency(SummaryReportFrequencyUnit summaryReportFrequency) {
        this.summaryReportFrequency = summaryReportFrequency;
        return this;
    }

    public void setSummaryReportFrequency(SummaryReportFrequencyUnit summaryReportFrequency) {
        this.summaryReportFrequency = summaryReportFrequency;
    }

    public String getSummaryReportDeliverToEmail() {
        return summaryReportDeliverToEmail;
    }

    public ActivityUpdatesSettings summaryReportDeliverToEmail(String summaryReportDeliverToEmail) {
        this.summaryReportDeliverToEmail = summaryReportDeliverToEmail;
        return this;
    }

    public void setSummaryReportDeliverToEmail(String summaryReportDeliverToEmail) {
        this.summaryReportDeliverToEmail = summaryReportDeliverToEmail;
    }

    public SummaryReportTimePeriodUnit getSummaryReportTimePeriod() {
        return summaryReportTimePeriod;
    }

    public ActivityUpdatesSettings summaryReportTimePeriod(SummaryReportTimePeriodUnit summaryReportTimePeriod) {
        this.summaryReportTimePeriod = summaryReportTimePeriod;
        return this;
    }

    public void setSummaryReportTimePeriod(SummaryReportTimePeriodUnit summaryReportTimePeriod) {
        this.summaryReportTimePeriod = summaryReportTimePeriod;
    }

    public ZonedDateTime getSummaryReportLastSent() {
        return summaryReportLastSent;
    }

    public ActivityUpdatesSettings summaryReportLastSent(ZonedDateTime summaryReportLastSent) {
        this.summaryReportLastSent = summaryReportLastSent;
        return this;
    }

    public void setSummaryReportLastSent(ZonedDateTime summaryReportLastSent) {
        this.summaryReportLastSent = summaryReportLastSent;
    }

    public Boolean isSummaryReportProcessing() {
        return summaryReportProcessing;
    }

    public ActivityUpdatesSettings summaryReportProcessing(Boolean summaryReportProcessing) {
        this.summaryReportProcessing = summaryReportProcessing;
        return this;
    }

    public void setSummaryReportProcessing(Boolean summaryReportProcessing) {
        this.summaryReportProcessing = summaryReportProcessing;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityUpdatesSettings)) {
            return false;
        }
        return id != null && id.equals(((ActivityUpdatesSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityUpdatesSettings{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", summaryReportEnabled='" + isSummaryReportEnabled() + "'" +
            ", summaryReportFrequency='" + getSummaryReportFrequency() + "'" +
            ", summaryReportDeliverToEmail='" + getSummaryReportDeliverToEmail() + "'" +
            ", summaryReportTimePeriod='" + getSummaryReportTimePeriod() + "'" +
            ", summaryReportLastSent='" + getSummaryReportLastSent() + "'" +
            ", summaryReportProcessing='" + isSummaryReportProcessing() + "'" +
            "}";
    }
}
