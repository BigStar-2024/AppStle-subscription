package com.et.repository;

import com.et.domain.ActivityLog;
import com.et.domain.enumeration.ActivityLogEntityType;
import com.et.domain.enumeration.ActivityLogEventType;
import com.et.domain.enumeration.ActivityLogStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ActivityLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>, JpaSpecificationExecutor<ActivityLog> {
    @Query("select al from ActivityLog al where al.shop = :shop and al.entityType = :entityType and al.entityId = :entityId" +
        " and al.eventType in :eventTypes and al.status = :status")
    List<ActivityLog> findActivityLogsForContract(
        @Param("shop") String shop,
        @Param("entityType") ActivityLogEntityType entityType,
        @Param("entityId") Long entityId,
        @Param("eventTypes")  List<ActivityLogEventType> eventTypes,
        @Param("status") ActivityLogStatus status);


    List<ActivityLog> findActivityLogsByShopAndEventTypeAndStatus(@Param("shop") String shop, @Param("eventType") ActivityLogEventType eventType, @Param("status") ActivityLogStatus status);

}
