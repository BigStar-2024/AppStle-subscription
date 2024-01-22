package com.et.service.impl;

import com.et.pojo.ExportActivityLogsRequest;
import com.et.service.ActivityLogQueryService;
import com.et.service.ActivityLogService;
import com.et.domain.ActivityLog;
import com.et.repository.ActivityLogRepository;
import com.et.service.MailgunService;
import com.et.service.dto.ActivityLogCriteria;
import com.et.service.dto.ActivityLogDTO;
import com.et.service.mapper.ActivityLogMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ActivityLog}.
 */
@Service
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {

    private final Logger log = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private final ActivityLogRepository activityLogRepository;

    private final ActivityLogMapper activityLogMapper;

    private final MailgunService mailgunService;

    private final ActivityLogQueryService activityLogQueryService;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository, ActivityLogMapper activityLogMapper,MailgunService mailgunService, ActivityLogQueryService activityLogQueryService)  {
        this.activityLogRepository = activityLogRepository;
        this.activityLogMapper = activityLogMapper;
        this.mailgunService = mailgunService;
        this.activityLogQueryService = activityLogQueryService;
    }

    /**
     * Save a activityLog.
     *
     * @param activityLogDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ActivityLogDTO save(ActivityLogDTO activityLogDTO) {
        log.debug("Request to save ActivityLog : {}", activityLogDTO);
        ActivityLog activityLog = activityLogMapper.toEntity(activityLogDTO);
        activityLog = activityLogRepository.save(activityLog);
        return activityLogMapper.toDto(activityLog);
    }

    /**
     * Get all the activityLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityLogs");
        return activityLogRepository.findAll(pageable)
            .map(activityLogMapper::toDto);
    }


    /**
     * Get one activityLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityLogDTO> findOne(Long id) {
        log.debug("Request to get ActivityLog : {}", id);
        return activityLogRepository.findById(id)
            .map(activityLogMapper::toDto);
    }

    /**
     * Delete the activityLog by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ActivityLog : {}", id);
        activityLogRepository.deleteById(id);
    }

    @Override
    public int exportActivityLogs(ExportActivityLogsRequest activityLogsRequest) {
        File tempFile = null;
        int totalPages = 0;
        Pageable pageable = PageRequest.of(activityLogsRequest.getPageNumber(), activityLogsRequest.getPageSize());
        try {
            String[] headers = {
                "Event ID",
                "Event Type",
                "Activity",
                "Source",
                "Status",
                "Activity On"
            };
            tempFile = File.createTempFile("Activity-Logs-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                Page<ActivityLogDTO> activityLogDTOPage = activityLogQueryService.findByCriteria(activityLogsRequest.getActivityLogCriteria(), pageable);
                if (activityLogDTOPage.hasContent()) {
                    totalPages = activityLogDTOPage.getTotalPages();
                    List<ActivityLogDTO> activityLogDTOS = activityLogDTOPage.getContent();
                    log.info("activityLogDTOPage.size.=" + activityLogDTOS.size());
                    for (int j = 0; j < activityLogDTOS.size(); j++) {
                        ActivityLogDTO activityLogDTO = activityLogDTOS.get(j);
                        log.info("Came to Record. No=" + j + " shop=" + activityLogsRequest.getShop());
                        csvPrinter.printRecord(activityLogDTO.getEntityId(),
                            activityLogDTO.getEventType(),
                            activityLogDTO.getAdditionalInfo(),
                            activityLogDTO.getEventSource(),
                            activityLogDTO.getStatus(),
                            activityLogDTO.getCreateAt());
                    }
                }
            } catch (Exception e) {
                log.error("ex=" + ExceptionUtils.getStackTrace(e));
            }

        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, "Activity Logs Exported (" + (pageable.getPageNumber() + 1) + " of " + totalPages + ")", "Check attached csv file for your all activity list details.", "subscription-support@appstle.com", activityLogsRequest.getShop(), activityLogsRequest.getEmail());
        return totalPages;
    }
}
