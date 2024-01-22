package com.et.web.rest.vm;

import java.time.ZonedDateTime;

public class FilterProperties {
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "FilterProperties{" +
            "fromDate=" + fromDate +
            ", toDate=" + toDate +
            '}';
    }
}
