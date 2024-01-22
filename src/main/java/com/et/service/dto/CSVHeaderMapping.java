package com.et.service.dto;

import java.io.Serializable;

public class CSVHeaderMapping implements Serializable {
    private String systemColumnHeader;
    private String sheetColumnOption;

    public String getSystemColumnHeader() {
        return systemColumnHeader;
    }

    public void setSystemColumnHeader(String systemColumnHeader) {
        this.systemColumnHeader = systemColumnHeader;
    }

    public String getSheetColumnOption() {
        return sheetColumnOption;
    }

    public void setSheetColumnOption(String sheetColumnOption) {
        this.sheetColumnOption = sheetColumnOption;
    }
}
