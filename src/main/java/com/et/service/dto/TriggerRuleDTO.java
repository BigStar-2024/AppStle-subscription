package com.et.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;
import com.et.domain.enumeration.OrderWebhookType;
import com.et.domain.enumeration.RemoveTagsExpiresUnitType;
import com.et.domain.enumeration.TriggerRuleStatus;

/**
 * A DTO for the {@link com.et.domain.TriggerRule} entity.
 */
public class TriggerRuleDTO implements Serializable {

    private Long id;

    @NotNull
    private String shop;

    private String name;

    @NotNull
    private Boolean appendToNote;

    @NotNull
    private OrderWebhookType webhook;

    private LocalDate deactivateAfterDate;

    private String deactivateAfterTime;

    @Lob
    private String fixedTags;

    @Lob
    private String dynamicTags;

    @Lob
    private String removeTags;

    @Lob
    private String notMatchTags;

    private Long removeTagsExpiresIn;

    private RemoveTagsExpiresUnitType removeTagsExpiresInUnit;

    @Lob
    private String handlerData;

    private TriggerRuleStatus status;

    private Instant deactivatedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAppendToNote() {
        return appendToNote;
    }

    public void setAppendToNote(Boolean appendToNote) {
        this.appendToNote = appendToNote;
    }

    public OrderWebhookType getWebhook() {
        return webhook;
    }

    public void setWebhook(OrderWebhookType webhook) {
        this.webhook = webhook;
    }

    public LocalDate getDeactivateAfterDate() {
        return deactivateAfterDate;
    }

    public void setDeactivateAfterDate(LocalDate deactivateAfterDate) {
        this.deactivateAfterDate = deactivateAfterDate;
    }

    public String getDeactivateAfterTime() {
        return deactivateAfterTime;
    }

    public void setDeactivateAfterTime(String deactivateAfterTime) {
        this.deactivateAfterTime = deactivateAfterTime;
    }

    public String getFixedTags() {
        return fixedTags;
    }

    public void setFixedTags(String fixedTags) {
        this.fixedTags = fixedTags;
    }

    public String getDynamicTags() {
        return dynamicTags;
    }

    public void setDynamicTags(String dynamicTags) {
        this.dynamicTags = dynamicTags;
    }

    public String getRemoveTags() {
        return removeTags;
    }

    public void setRemoveTags(String removeTags) {
        this.removeTags = removeTags;
    }

    public String getNotMatchTags() {
        return notMatchTags;
    }

    public void setNotMatchTags(String notMatchTags) {
        this.notMatchTags = notMatchTags;
    }

    public Long getRemoveTagsExpiresIn() {
        return removeTagsExpiresIn;
    }

    public void setRemoveTagsExpiresIn(Long removeTagsExpiresIn) {
        this.removeTagsExpiresIn = removeTagsExpiresIn;
    }

    public RemoveTagsExpiresUnitType getRemoveTagsExpiresInUnit() {
        return removeTagsExpiresInUnit;
    }

    public void setRemoveTagsExpiresInUnit(RemoveTagsExpiresUnitType removeTagsExpiresInUnit) {
        this.removeTagsExpiresInUnit = removeTagsExpiresInUnit;
    }

    public String getHandlerData() {
        return handlerData;
    }

    public void setHandlerData(String handlerData) {
        this.handlerData = handlerData;
    }

    public TriggerRuleStatus getStatus() {
        return status;
    }

    public void setStatus(TriggerRuleStatus status) {
        this.status = status;
    }

    public Instant getDeactivatedAt() {
        return deactivatedAt;
    }

    public void setDeactivatedAt(Instant deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TriggerRuleDTO)) {
            return false;
        }

        return id != null && id.equals(((TriggerRuleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TriggerRuleDTO{" +
            "id=" + getId() +
            ", shop='" + getShop() + "'" +
            ", name='" + getName() + "'" +
            ", appendToNote='" + isAppendToNote() + "'" +
            ", webhook='" + getWebhook() + "'" +
            ", deactivateAfterDate='" + getDeactivateAfterDate() + "'" +
            ", deactivateAfterTime='" + getDeactivateAfterTime() + "'" +
            ", fixedTags='" + getFixedTags() + "'" +
            ", dynamicTags='" + getDynamicTags() + "'" +
            ", removeTags='" + getRemoveTags() + "'" +
            ", notMatchTags='" + getNotMatchTags() + "'" +
            ", removeTagsExpiresIn=" + getRemoveTagsExpiresIn() +
            ", removeTagsExpiresInUnit='" + getRemoveTagsExpiresInUnit() + "'" +
            ", handlerData='" + getHandlerData() + "'" +
            ", status='" + getStatus() + "'" +
            ", deactivatedAt='" + getDeactivatedAt() + "'" +
            "}";
    }
}
