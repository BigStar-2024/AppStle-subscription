package com.et.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class SyncLabelsInfo {
    List<SyncInfoItem> syncInfoItems = new ArrayList<>();
    private String namespace;
    private List<String> groups = new ArrayList<>();

    public List<SyncInfoItem> getSyncInfoItems() {
        return syncInfoItems;
    }

    public void setSyncInfoItems(List<SyncInfoItem> syncInfoItems) {
        this.syncInfoItems = syncInfoItems;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
