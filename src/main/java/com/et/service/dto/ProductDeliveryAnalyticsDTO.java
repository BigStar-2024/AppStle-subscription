package com.et.service.dto;

public interface ProductDeliveryAnalyticsDTO {

    String getProductId();

    String getTitle();

    String getVariantId();

    String getVariantTitle();

    Long getDeliveryInNext7Days();

    Long getDeliveryInNext30Days();

    Long getDeliveryInNext90Days();
}
