package com.et.pojo;

import java.util.ArrayList;
import java.util.List;

public class LabelValueInfo {

    private String value;
    private List<String> groups = new ArrayList<>();

    public LabelValueInfo() {

    }

    public LabelValueInfo(String value, List<String> groups) {
        this.value = value;
        this.groups = groups;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "LabelValueInfo{" +
            "value='" + value + '\'' +
            ", groups=" + groups +
            '}';
    }
}
