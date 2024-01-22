package com.et.pojo;

import java.util.HashSet;
import java.util.Set;

public class RemoveTagsAfterInfo {

    private Set<String> tags = new HashSet<>();
    private Long timeStamp;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
