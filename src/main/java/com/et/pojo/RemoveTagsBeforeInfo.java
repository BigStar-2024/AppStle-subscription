package com.et.pojo;

import com.et.domain.enumeration.RemoveTagsExpiresUnitType;

import java.util.HashSet;
import java.util.Set;

public class RemoveTagsBeforeInfo {

    private Set<String> tags = new HashSet<>();

    private RemoveTagsExpiresUnitType removeTagsExpiresUnitType;

    private Long removeTagsExpiresInValue;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public RemoveTagsExpiresUnitType getRemoveTagsExpiresUnitType() {
        return removeTagsExpiresUnitType;
    }

    public void setRemoveTagsExpiresUnitType(RemoveTagsExpiresUnitType removeTagsExpiresUnitType) {
        this.removeTagsExpiresUnitType = removeTagsExpiresUnitType;
    }

    public Long getRemoveTagsExpiresInValue() {
        return removeTagsExpiresInValue;
    }

    public void setRemoveTagsExpiresInValue(Long removeTagsExpiresInValue) {
        this.removeTagsExpiresInValue = removeTagsExpiresInValue;
    }
}
