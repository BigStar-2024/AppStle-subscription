package com.et.utils;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Attachment.AttachmentBuilder;
import com.github.seratch.jslack.api.model.Field;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.Payload.PayloadBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SlackService {
    public enum SlackChannel {
        NotificationSC,
        NotificationEnterprisePlan
    }

    private static final Logger log = LoggerFactory.getLogger(SlackService.class);

    public static void sendMessage(String title, SlackChannel slackChannel, SlackField... fields) {
        AttachmentBuilder builder = Attachment.builder();
        builder.color("good");
        builder.pretext("*" + title + "*");
        builder.fallback(title);

        List<Field> newFields = new ArrayList<>();
        for (SlackField field : fields) {
            newFields.add(field.field);
        }
        builder.fields(newFields);

        String url = "";
        if (slackChannel == SlackChannel.NotificationSC) {
            url = "https://hooks.slack.com/services/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";
        } else if(slackChannel == SlackChannel.NotificationEnterprisePlan) {
            url = "https://hooks.slack.com/services/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";
        }

        sendSlackMessage(builder.build(), url);
    }

    private static void sendSlackMessage(Attachment attachment, String url) {
        try {

            PayloadBuilder builder = Payload.builder();

            builder.channel("#general"); //Not used

            builder.username("MerchantBot");
            builder.iconUrl("https://goo.gl/VRAx3Z");

            if (attachment != null) {
                List<Attachment> attachments = new ArrayList<>();
                attachments.add(attachment);
                builder.attachments(attachments);
            }

            builder.text("");
            Payload payload = builder.build();

            Slack slack = Slack.getInstance();
            slack.send(url, payload);
        } catch (Exception e) {
            log.error("An error occurred while sending message to slack", e);
        }
    }
}
