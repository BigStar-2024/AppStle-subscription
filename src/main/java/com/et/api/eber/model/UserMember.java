package com.et.api.eber.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMember {
    private Long id;
    private Long user_id;
    private Long points_will_expire_in_7d;
    private Long points_will_expire_in_14d;
    private Long points_will_expire_in_30d;
    private Long points_will_expire_in_60d;
    private Long points_will_expire_in_90d;
    private Long points_will_expire_in_1m;
    private Long points_will_expire_in_2m;
    private Long points_will_expire_in_3m;
    private Long next_member_tier_id;
    private String next_member_tier_required_value;
    private String next_member_tier_required_unit;
    private String next_member_tier_value;
    private String next_member_tier_required_amount;
    private String next_member_tier_amount;
    private Long next_member_tier_progress;
    private String downgrade_member_tier_required_value;
    private String downgrade_member_tier_required_unit;
    private String downgrade_member_tier_value;
    private String downgrade_member_tier_required_amount;
    private String downgrade_member_tier_amount;
    private Long downgrade_member_tier_progress;
    private String average_spending;
    private String total_spending;
    private Long total_expired_points;
    private Long expiring_coming_points;
    private String expiring_coming_date;
    private Long expiring_coming_points_circle_2;
    private String expiring_coming_date_2;
    private Long expiring_coming_points_circle_3;
    private String expiring_coming_date_3;

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

    public Long getPoints_will_expire_in_7d() {
        return points_will_expire_in_7d;
    }

    public void setPoints_will_expire_in_7d(Long points_will_expire_in_7d) {
        this.points_will_expire_in_7d = points_will_expire_in_7d;
    }

    public Long getPoints_will_expire_in_14d() {
        return points_will_expire_in_14d;
    }

    public void setPoints_will_expire_in_14d(Long points_will_expire_in_14d) {
        this.points_will_expire_in_14d = points_will_expire_in_14d;
    }

    public Long getPoints_will_expire_in_30d() {
        return points_will_expire_in_30d;
    }

    public void setPoints_will_expire_in_30d(Long points_will_expire_in_30d) {
        this.points_will_expire_in_30d = points_will_expire_in_30d;
    }

    public Long getPoints_will_expire_in_60d() {
        return points_will_expire_in_60d;
    }

    public void setPoints_will_expire_in_60d(Long points_will_expire_in_60d) {
        this.points_will_expire_in_60d = points_will_expire_in_60d;
    }

    public Long getPoints_will_expire_in_90d() {
        return points_will_expire_in_90d;
    }

    public void setPoints_will_expire_in_90d(Long points_will_expire_in_90d) {
        this.points_will_expire_in_90d = points_will_expire_in_90d;
    }

    public Long getPoints_will_expire_in_1m() {
        return points_will_expire_in_1m;
    }

    public void setPoints_will_expire_in_1m(Long points_will_expire_in_1m) {
        this.points_will_expire_in_1m = points_will_expire_in_1m;
    }

    public Long getPoints_will_expire_in_2m() {
        return points_will_expire_in_2m;
    }

    public void setPoints_will_expire_in_2m(Long points_will_expire_in_2m) {
        this.points_will_expire_in_2m = points_will_expire_in_2m;
    }

    public Long getPoints_will_expire_in_3m() {
        return points_will_expire_in_3m;
    }

    public void setPoints_will_expire_in_3m(Long points_will_expire_in_3m) {
        this.points_will_expire_in_3m = points_will_expire_in_3m;
    }

    public Long getNext_member_tier_id() {
        return next_member_tier_id;
    }

    public void setNext_member_tier_id(Long next_member_tier_id) {
        this.next_member_tier_id = next_member_tier_id;
    }

    public String getNext_member_tier_required_value() {
        return next_member_tier_required_value;
    }

    public void setNext_member_tier_required_value(String next_member_tier_required_value) {
        this.next_member_tier_required_value = next_member_tier_required_value;
    }

    public String getNext_member_tier_required_unit() {
        return next_member_tier_required_unit;
    }

    public void setNext_member_tier_required_unit(String next_member_tier_required_unit) {
        this.next_member_tier_required_unit = next_member_tier_required_unit;
    }

    public String getNext_member_tier_value() {
        return next_member_tier_value;
    }

    public void setNext_member_tier_value(String next_member_tier_value) {
        this.next_member_tier_value = next_member_tier_value;
    }

    public String getNext_member_tier_required_amount() {
        return next_member_tier_required_amount;
    }

    public void setNext_member_tier_required_amount(String next_member_tier_required_amount) {
        this.next_member_tier_required_amount = next_member_tier_required_amount;
    }

    public String getNext_member_tier_amount() {
        return next_member_tier_amount;
    }

    public void setNext_member_tier_amount(String next_member_tier_amount) {
        this.next_member_tier_amount = next_member_tier_amount;
    }

    public Long getNext_member_tier_progress() {
        return next_member_tier_progress;
    }

    public void setNext_member_tier_progress(Long next_member_tier_progress) {
        this.next_member_tier_progress = next_member_tier_progress;
    }

    public String getDowngrade_member_tier_required_value() {
        return downgrade_member_tier_required_value;
    }

    public void setDowngrade_member_tier_required_value(String downgrade_member_tier_required_value) {
        this.downgrade_member_tier_required_value = downgrade_member_tier_required_value;
    }

    public String getDowngrade_member_tier_required_unit() {
        return downgrade_member_tier_required_unit;
    }

    public void setDowngrade_member_tier_required_unit(String downgrade_member_tier_required_unit) {
        this.downgrade_member_tier_required_unit = downgrade_member_tier_required_unit;
    }

    public String getDowngrade_member_tier_value() {
        return downgrade_member_tier_value;
    }

    public void setDowngrade_member_tier_value(String downgrade_member_tier_value) {
        this.downgrade_member_tier_value = downgrade_member_tier_value;
    }

    public String getDowngrade_member_tier_required_amount() {
        return downgrade_member_tier_required_amount;
    }

    public void setDowngrade_member_tier_required_amount(String downgrade_member_tier_required_amount) {
        this.downgrade_member_tier_required_amount = downgrade_member_tier_required_amount;
    }

    public String getDowngrade_member_tier_amount() {
        return downgrade_member_tier_amount;
    }

    public void setDowngrade_member_tier_amount(String downgrade_member_tier_amount) {
        this.downgrade_member_tier_amount = downgrade_member_tier_amount;
    }

    public Long getDowngrade_member_tier_progress() {
        return downgrade_member_tier_progress;
    }

    public void setDowngrade_member_tier_progress(Long downgrade_member_tier_progress) {
        this.downgrade_member_tier_progress = downgrade_member_tier_progress;
    }

    public String getAverage_spending() {
        return average_spending;
    }

    public void setAverage_spending(String average_spending) {
        this.average_spending = average_spending;
    }

    public String getTotal_spending() {
        return total_spending;
    }

    public void setTotal_spending(String total_spending) {
        this.total_spending = total_spending;
    }

    public Long getTotal_expired_points() {
        return total_expired_points;
    }

    public void setTotal_expired_points(Long total_expired_points) {
        this.total_expired_points = total_expired_points;
    }

    public Long getExpiring_coming_points() {
        return expiring_coming_points;
    }

    public void setExpiring_coming_points(Long expiring_coming_points) {
        this.expiring_coming_points = expiring_coming_points;
    }

    public String getExpiring_coming_date() {
        return expiring_coming_date;
    }

    public void setExpiring_coming_date(String expiring_coming_date) {
        this.expiring_coming_date = expiring_coming_date;
    }

    public Long getExpiring_coming_points_circle_2() {
        return expiring_coming_points_circle_2;
    }

    public void setExpiring_coming_points_circle_2(Long expiring_coming_points_circle_2) {
        this.expiring_coming_points_circle_2 = expiring_coming_points_circle_2;
    }

    public String getExpiring_coming_date_2() {
        return expiring_coming_date_2;
    }

    public void setExpiring_coming_date_2(String expiring_coming_date_2) {
        this.expiring_coming_date_2 = expiring_coming_date_2;
    }

    public Long getExpiring_coming_points_circle_3() {
        return expiring_coming_points_circle_3;
    }

    public void setExpiring_coming_points_circle_3(Long expiring_coming_points_circle_3) {
        this.expiring_coming_points_circle_3 = expiring_coming_points_circle_3;
    }

    public String getExpiring_coming_date_3() {
        return expiring_coming_date_3;
    }

    public void setExpiring_coming_date_3(String expiring_coming_date_3) {
        this.expiring_coming_date_3 = expiring_coming_date_3;
    }
}
