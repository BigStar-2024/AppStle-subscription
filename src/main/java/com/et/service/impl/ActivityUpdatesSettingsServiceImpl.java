package com.et.service.impl;

import com.et.domain.enumeration.SummaryReportFrequencyUnit;
import com.et.domain.enumeration.SummaryReportTimePeriodUnit;
import com.et.service.ActivityUpdatesSettingsService;
import com.et.domain.ActivityUpdatesSettings;
import com.et.repository.ActivityUpdatesSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ActivityUpdatesSettings}.
 */
@Service
@Transactional
public class ActivityUpdatesSettingsServiceImpl implements ActivityUpdatesSettingsService {

    private final Logger log = LoggerFactory.getLogger(ActivityUpdatesSettingsServiceImpl.class);

    private final ActivityUpdatesSettingsRepository activityUpdatesSettingsRepository;

    public ActivityUpdatesSettingsServiceImpl(ActivityUpdatesSettingsRepository activityUpdatesSettingsRepository) {
        this.activityUpdatesSettingsRepository = activityUpdatesSettingsRepository;
    }

    @Override
    public ActivityUpdatesSettings save(ActivityUpdatesSettings activityUpdatesSettings) {
        log.debug("Request to save ActivityUpdatesSettings : {}", activityUpdatesSettings);
        setSummaryReportTimePeriod(activityUpdatesSettings);
        return activityUpdatesSettingsRepository.save(activityUpdatesSettings);
    }

    private void setSummaryReportTimePeriod(ActivityUpdatesSettings activityUpdatesSettings) {
        if(activityUpdatesSettings.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.DAILY)){
            activityUpdatesSettings.setSummaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_1_DAY);
        } else if(activityUpdatesSettings.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.WEEKLY)){
            activityUpdatesSettings.setSummaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_7_DAYS);
        } else if(activityUpdatesSettings.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.MONTHLY)){
            activityUpdatesSettings.setSummaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_30_DAYS);
        } else if(activityUpdatesSettings.getSummaryReportFrequency().equals(SummaryReportFrequencyUnit.QUARTERLY)){
            activityUpdatesSettings.setSummaryReportTimePeriod(SummaryReportTimePeriodUnit.LAST_90_DAYS);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityUpdatesSettings> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityUpdatesSettings");
        return activityUpdatesSettingsRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityUpdatesSettings> findOne(Long id) {
        log.debug("Request to get ActivityUpdatesSettings : {}", id);
        return activityUpdatesSettingsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ActivityUpdatesSettings : {}", id);
        activityUpdatesSettingsRepository.deleteById(id);
    }

    @Override
    public Optional<ActivityUpdatesSettings> findByShop(String shop) {
        return activityUpdatesSettingsRepository.findByShop(shop);
    }
}
