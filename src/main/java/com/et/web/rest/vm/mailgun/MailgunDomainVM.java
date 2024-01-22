
package com.et.web.rest.vm.mailgun;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "domain",
    "receiving_dns_records",
    "sending_dns_records"
})
public class MailgunDomainVM {

    @JsonProperty("domain")
    private Domain domain;
    @JsonProperty("receiving_dns_records")
    private List<ReceivingDnsRecord> receivingDnsRecords = null;
    @JsonProperty("sending_dns_records")
    private List<SendingDnsRecord> sendingDnsRecords = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("domain")
    public Domain getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    @JsonProperty("receiving_dns_records")
    public List<ReceivingDnsRecord> getReceivingDnsRecords() {
        return receivingDnsRecords;
    }

    @JsonProperty("receiving_dns_records")
    public void setReceivingDnsRecords(List<ReceivingDnsRecord> receivingDnsRecords) {
        this.receivingDnsRecords = receivingDnsRecords;
    }

    @JsonProperty("sending_dns_records")
    public List<SendingDnsRecord> getSendingDnsRecords() {
        return sendingDnsRecords;
    }

    @JsonProperty("sending_dns_records")
    public void setSendingDnsRecords(List<SendingDnsRecord> sendingDnsRecords) {
        this.sendingDnsRecords = sendingDnsRecords;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
