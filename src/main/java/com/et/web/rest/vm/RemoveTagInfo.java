package com.et.web.rest.vm;

import java.time.ZonedDateTime;

public class RemoveTagInfo {
    private final String tag;
    private final ZonedDateTime tagExpirationTime;

    public RemoveTagInfo(String tag, ZonedDateTime tagExpirationTime) {
        this.tag = tag;
        this.tagExpirationTime = tagExpirationTime;
    }

    public String getTag() {
        return tag;
    }

    public ZonedDateTime getTagExpirationTime() {
        return tagExpirationTime;
    }
}
