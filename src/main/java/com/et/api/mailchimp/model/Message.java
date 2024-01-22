package com.et.api.mailchimp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String html;
    private String text;
    private String subject;
    private String from_email;
    private String from_name;
    private List<RecipientInformation> to;
    private Map<String, Object> headers;
    private boolean important;
    private boolean track_opens;
    private boolean track_clicks;
    private boolean auto_text;
    private boolean auto_html;
    private boolean inline_css;
    private boolean url_strip_qs;
    private boolean preserve_recipients;
    private boolean view_content_link;
    private String bcc_address;
    private String tracking_domain;
    private String signing_domain;
    private String return_path_domain;
    private boolean merge;
    private String merge_language;
    private List<String> tags;
    private String subaccount;
    private List<String> google_analytics_domains;
    private String google_analytics_campaign;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public List<RecipientInformation> getTo() {
        return to;
    }

    public void setTo(List<RecipientInformation> to) {
        this.to = to;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isTrack_opens() {
        return track_opens;
    }

    public void setTrack_opens(boolean track_opens) {
        this.track_opens = track_opens;
    }

    public boolean isTrack_clicks() {
        return track_clicks;
    }

    public void setTrack_clicks(boolean track_clicks) {
        this.track_clicks = track_clicks;
    }

    public boolean isAuto_text() {
        return auto_text;
    }

    public void setAuto_text(boolean auto_text) {
        this.auto_text = auto_text;
    }

    public boolean isAuto_html() {
        return auto_html;
    }

    public void setAuto_html(boolean auto_html) {
        this.auto_html = auto_html;
    }

    public boolean isInline_css() {
        return inline_css;
    }

    public void setInline_css(boolean inline_css) {
        this.inline_css = inline_css;
    }

    public boolean isUrl_strip_qs() {
        return url_strip_qs;
    }

    public void setUrl_strip_qs(boolean url_strip_qs) {
        this.url_strip_qs = url_strip_qs;
    }

    public boolean isPreserve_recipients() {
        return preserve_recipients;
    }

    public void setPreserve_recipients(boolean preserve_recipients) {
        this.preserve_recipients = preserve_recipients;
    }

    public boolean isView_content_link() {
        return view_content_link;
    }

    public void setView_content_link(boolean view_content_link) {
        this.view_content_link = view_content_link;
    }

    public String getBcc_address() {
        return bcc_address;
    }

    public void setBcc_address(String bcc_address) {
        this.bcc_address = bcc_address;
    }

    public String getTracking_domain() {
        return tracking_domain;
    }

    public void setTracking_domain(String tracking_domain) {
        this.tracking_domain = tracking_domain;
    }

    public String getSigning_domain() {
        return signing_domain;
    }

    public void setSigning_domain(String signing_domain) {
        this.signing_domain = signing_domain;
    }

    public String getReturn_path_domain() {
        return return_path_domain;
    }

    public void setReturn_path_domain(String return_path_domain) {
        this.return_path_domain = return_path_domain;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public String getMerge_language() {
        return merge_language;
    }

    public void setMerge_language(String merge_language) {
        this.merge_language = merge_language;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSubaccount() {
        return subaccount;
    }

    public void setSubaccount(String subaccount) {
        this.subaccount = subaccount;
    }

    public List<String> getGoogle_analytics_domains() {
        return google_analytics_domains;
    }

    public void setGoogle_analytics_domains(List<String> google_analytics_domains) {
        this.google_analytics_domains = google_analytics_domains;
    }

    public String getGoogle_analytics_campaign() {
        return google_analytics_campaign;
    }

    public void setGoogle_analytics_campaign(String google_analytics_campaign) {
        this.google_analytics_campaign = google_analytics_campaign;
    }
}
