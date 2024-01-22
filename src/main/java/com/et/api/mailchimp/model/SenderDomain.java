package com.et.api.mailchimp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SenderDomain {

    private String domain;
    private String created_at;
    private String last_tested_at;
    private SenderAuth spf;
    private SenderAuth dkim;
    private String verified_at;
    private Boolean valid_signing;
    private String verify_txt_key;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLast_tested_at() {
        return last_tested_at;
    }

    public void setLast_tested_at(String last_tested_at) {
        this.last_tested_at = last_tested_at;
    }

    public SenderAuth getSpf() {
        return spf;
    }

    public void setSpf(SenderAuth spf) {
        this.spf = spf;
    }

    public SenderAuth getDkim() {
        return dkim;
    }

    public void setDkim(SenderAuth dkim) {
        this.dkim = dkim;
    }

    public String getVerified_at() {
        return verified_at;
    }

    public void setVerified_at(String verified_at) {
        this.verified_at = verified_at;
    }

    public Boolean getValid_signing() {
        return valid_signing;
    }

    public void setValid_signing(Boolean valid_signing) {
        this.valid_signing = valid_signing;
    }

    public String getVerify_txt_key() {
        return verify_txt_key;
    }

    public void setVerify_txt_key(String verify_txt_key) {
        this.verify_txt_key = verify_txt_key;
    }
}
