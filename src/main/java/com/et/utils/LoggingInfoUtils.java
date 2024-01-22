package com.et.utils;

import com.et.constant.LoggingConstants;
import com.et.subscriptionlogging.LoggingInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class LoggingInfoUtils {
    public LoggingInfo getLoggingInfo(String shop, LoggingConstants.EntityType entityType, LoggingConstants.EventLog eventLog, Long entityId, LoggingConstants.EventSource eventSource) {
        LoggingInfo loggingInfo = new LoggingInfo();
        loggingInfo.setEntityId(entityId);
        loggingInfo.setShop(shop);
        loggingInfo.setEntityType(entityType);
        loggingInfo.setEventLog(eventLog);
        loggingInfo.setEventSource(eventSource);
        loggingInfo.setStatus(LoggingConstants.Statuses.SUCCESS.getStatus());
        loggingInfo.setCreatedAt(ZonedDateTime.now());
        loggingInfo.setAdditionalInfo(new ArrayList<>());
        return loggingInfo;
    }

    public String buildResponseJson(LoggingInfo loggingInfo, ArrayList<String> additionalInfo, String status) throws JsonProcessingException {
//        if(!CollectionUtils.isEmpty(errors)){
            loggingInfo.setAdditionalInfo(additionalInfo);
            loggingInfo.setStatus(status);
//        }
        return new ObjectMapper().writeValueAsString(loggingInfo);
    }
}
