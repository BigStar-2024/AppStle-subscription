package com.et.service.dto;

import java.util.ArrayList;
import java.util.List;

public class CsvImportResult {

    private List<String> errorList = new ArrayList<>();
    private List<String> alreadyProcessedId = new ArrayList<>();
    private List<String> processedId = new ArrayList<>();
    private List<String> missingHeaders = new ArrayList<>();
    private List<String> customerDataMissingHeaders = new ArrayList<>();

    public List<String> getMissingHeaders() {
        return missingHeaders;
    }

    public void setMissingHeaders(List<String> missingHeaders) {
        this.missingHeaders = missingHeaders;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public List<String> getAlreadyProcessedId() {
        return alreadyProcessedId;
    }

    public void setAlreadyProcessedId(List<String> alreadyProcessedId) {
        this.alreadyProcessedId = alreadyProcessedId;
    }

    public List<String> getProcessedId() {
        return processedId;
    }

    public void setProcessedId(List<String> processedId) {
        this.processedId = processedId;
    }


    public void setCustomerDataMissingHeaders(List<String> customerDataMissingHeaders) {
        this.customerDataMissingHeaders = customerDataMissingHeaders;
    }

    public List<String> getCustomerDataMissingHeaders() {
        return customerDataMissingHeaders;
    }

    @Override
    public String toString() {
        return "CsvImportResult{" +
            "errorList=" + errorList +
            ", alreadyProcessedId=" + alreadyProcessedId +
            ", processedId=" + processedId +
            ", missingHeaders=" + missingHeaders +
            ", customerDataMissingHeaders=" + customerDataMissingHeaders +
            '}';
    }
}
