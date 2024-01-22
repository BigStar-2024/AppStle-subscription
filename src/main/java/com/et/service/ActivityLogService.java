package com.et.service;

import com.et.pojo.ExportActivityLogsRequest;
import com.et.service.dto.ActivityLogDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.et.domain.ActivityLog}.
 */
public interface ActivityLogService {

    /**
     * Save a activityLog.
     *
     * @param activityLogDTO the entity to save.
     * @return the persisted entity.
     */
    ActivityLogDTO save(ActivityLogDTO activityLogDTO);

    /**
     * Get all the activityLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityLogDTO> findAll(Pageable pageable);


    /**
     * Get the "id" activityLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivityLogDTO> findOne(Long id);

    /**
     * Delete the "id" activityLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    int exportActivityLogs(ExportActivityLogsRequest activityLogsRequest);
}
