package com.et.api.eber.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPoints {
    private Long id;
    private Long user_id;
    private Long business_id;
    private Long points;
    private Long num_visits;
    private Long num_claimed_rewards;
    private String friendly_last_visit_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getNum_visits() {
        return num_visits;
    }

    public void setNum_visits(Long num_visits) {
        this.num_visits = num_visits;
    }

    public Long getNum_claimed_rewards() {
        return num_claimed_rewards;
    }

    public void setNum_claimed_rewards(Long num_claimed_rewards) {
        this.num_claimed_rewards = num_claimed_rewards;
    }

    public String getFriendly_last_visit_at() {
        return friendly_last_visit_at;
    }

    public void setFriendly_last_visit_at(String friendly_last_visit_at) {
        this.friendly_last_visit_at = friendly_last_visit_at;
    }
}
