package com.et.api.yotpo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Campaign {

    private Long id;
    private String title;
    private String details;
    private String type;
    private String cta_text;
    private String share_text;
    private String url;
    private String username;
    private String entity_id;
    private String icon;
    private String reward_text;
    private ZonedDateTime created_at;
    private ZonedDateTime updated_at;
    private ZonedDateTime expires_at;
    private int max_completions_per_user;
    private int min_actions_required;
    private ZonedDateTime ends_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCta_text() {
        return cta_text;
    }

    public void setCta_text(String cta_text) {
        this.cta_text = cta_text;
    }

    public String getShare_text() {
        return share_text;
    }

    public void setShare_text(String share_text) {
        this.share_text = share_text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReward_text() {
        return reward_text;
    }

    public void setReward_text(String reward_text) {
        this.reward_text = reward_text;
    }

    public ZonedDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(ZonedDateTime created_at) {
        this.created_at = created_at;
    }

    public ZonedDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(ZonedDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public ZonedDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(ZonedDateTime expires_at) {
        this.expires_at = expires_at;
    }

    public int getMax_completions_per_user() {
        return max_completions_per_user;
    }

    public void setMax_completions_per_user(int max_completions_per_user) {
        this.max_completions_per_user = max_completions_per_user;
    }

    public int getMin_actions_required() {
        return min_actions_required;
    }

    public void setMin_actions_required(int min_actions_required) {
        this.min_actions_required = min_actions_required;
    }

    public ZonedDateTime getEnds_at() {
        return ends_at;
    }

    public void setEnds_at(ZonedDateTime ends_at) {
        this.ends_at = ends_at;
    }
}
