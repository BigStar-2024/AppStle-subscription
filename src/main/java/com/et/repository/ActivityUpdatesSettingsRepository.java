package com.et.repository;

import com.et.domain.ActivityUpdatesSettings;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ActivityUpdatesSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityUpdatesSettingsRepository extends JpaRepository<ActivityUpdatesSettings, Long> {
    Optional<ActivityUpdatesSettings> findByShop(String shop);

    @Query(value = "select * from activity_updates_settings aus " +
        "WHERE aus.summary_report_enabled = :summary_report_enabled " +
        "AND aus.summary_report_processing is not true " +
        "AND ( aus.summary_report_last_sent is NULL " +
        "   OR (aus.summary_report_last_sent < :summary_report_last_sent AND aus.summary_report_frequency ='DAILY') " +
        "   OR (aus.summary_report_last_sent < :summary_report_last_week_sent AND aus.summary_report_frequency ='WEEKLY') " +
        "   OR (aus.summary_report_last_sent < :summary_report_last_month_sent AND aus.summary_report_frequency ='MONTHLY') " +
        "   OR (aus.summary_report_last_sent < :summary_report_last_quarter_sent AND aus.summary_report_frequency ='QUARTERLY')) ",
        nativeQuery = true)
    List<ActivityUpdatesSettings> findShopsByReportToBeSent(@Param("summary_report_enabled") boolean summaryReportEnabled,
                                                            @Param("summary_report_last_sent") ZonedDateTime lastSentGreaterThan,
                                                            @Param("summary_report_last_week_sent") ZonedDateTime week,
                                                            @Param("summary_report_last_month_sent") ZonedDateTime month,
                                                            @Param("summary_report_last_quarter_sent") ZonedDateTime quarter);
}
