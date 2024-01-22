package com.et.utils;

import com.et.service.PlanInfoService;
import com.et.service.ShopInfoService;
import com.et.service.SocialConnectionService;
import com.et.web.rest.vm.FilterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


@Component
public class SeedsUtils {

    private final Logger log = LoggerFactory.getLogger(SeedsUtils.class);


    public FilterProperties getFilterProperties(@Nonnull final String filterBy,
                                                @Nonnull final Long days,
                                                @Nonnull final ZonedDateTime fromDate,
                                                @Nonnull final ZonedDateTime toDate,
                                                @Nonnull final ZoneId zoneId) {

        ZonedDateTime finalFromDate = ZonedDateTime.now(zoneId).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime finalToDate = ZonedDateTime.now(zoneId).withHour(23).withMinute(59).withSecond(59);
        if (filterBy.equals("days")) {
            finalFromDate = finalFromDate.minusDays(days);
            if(days.equals(1L) || days.equals(7L)) {
                finalToDate = finalToDate.minusDays(1);
            }
        }
        if (filterBy.equals("range")) {
            finalFromDate = fromDate.withHour(0).withMinute(0).withSecond(0).withZoneSameLocal(zoneId);
            finalToDate = toDate.withHour(23).withMinute(59).withSecond(59).withZoneSameLocal(zoneId);
        }

        FilterProperties filterProperties = new FilterProperties();
        filterProperties.setFromDate(finalFromDate);
        filterProperties.setToDate(finalToDate);

        return filterProperties;
    }

    public FilterProperties getFutureOrPastFilterProperties(@Nonnull final Long days, @Nonnull final String futureOrPast) {

        ZonedDateTime finalFromDate = ZonedDateTime.now(ZoneId.of("UTC")).withHour(0).withMinute(0);
        ZonedDateTime finalToDate = ZonedDateTime.now(ZoneId.of("UTC")).withHour(23).withMinute(59);

        if (futureOrPast.equals("future")) {
            finalToDate = finalToDate.plusDays(days);
        } else {
            finalFromDate = finalFromDate.minusDays(days);
        }

        FilterProperties filterProperties = new FilterProperties();
        filterProperties.setFromDate(finalFromDate);
        filterProperties.setToDate(finalToDate);

        return filterProperties;
    }

    public String getDayFromDateRange(@Nonnull final String filterBy,
                                      @Nonnull final Long days,
                                      @Nonnull final ZonedDateTime fromDate,
                                      @Nonnull final ZonedDateTime toDate) {

        ZonedDateTime finalFromDate;
        ZonedDateTime finalToDate;
        String dayCount = null;
        if (filterBy.equals("days")) {
            dayCount = days + "d";
        }
        if (filterBy.equals("range")) {
            finalFromDate = fromDate.withHour(0).withMinute(0);
            finalToDate = toDate.withHour(23).withMinute(59);
            dayCount = ChronoUnit.DAYS.between(finalFromDate, finalToDate) + "d";
        }
        return dayCount;
    }
}
