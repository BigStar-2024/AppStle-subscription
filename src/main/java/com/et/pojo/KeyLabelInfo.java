package com.et.pojo;

public class KeyLabelInfo {
    private String key;
    private LabelValueInfo labelValueInfo;

    public KeyLabelInfo() {

    }

    public KeyLabelInfo(String key, LabelValueInfo labelValueInfo) {
        this.key = key;
        this.labelValueInfo = labelValueInfo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LabelValueInfo getLabelValueInfo() {
        return labelValueInfo;
    }

    public void setLabelValueInfo(LabelValueInfo labelValueInfo) {
        this.labelValueInfo = labelValueInfo;
    }

    @Override
    public String toString() {
        return "KeyLabelInfo{" +
            "key='" + key + '\'' +
            ", labelValueInfo=" + labelValueInfo +
            '}';
    }
}
