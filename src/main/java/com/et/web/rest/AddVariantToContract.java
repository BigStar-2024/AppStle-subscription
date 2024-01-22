package com.et.web.rest;

import org.apache.commons.csv.CSVRecord;

import java.util.Map;

@FunctionalInterface
public interface AddVariantToContract {
    void addVariantToContract(CSVRecord csvRecord, Map<String, String> subscriptionHeaderMap) throws Exception;
}
