package com.et.api.mailchimp;

import com.et.api.mailchimp.model.Message;
import com.et.api.mailchimp.model.SenderDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class MailchimpApi {

    protected RestTemplate restTemplate = new RestTemplate();

    public static final String API_BASE_URl = "https://mandrillapp.com/api/1.0";

    public static final String SEND_MESSAGE = "/messages/send";

    public static final String GET_DOMAINS = "/senders/domains";

    private final Logger logger = LoggerFactory.getLogger(MailchimpApi.class);

    public Object[] sendEmail(String apiKey, Message message){
        String url = API_BASE_URl+SEND_MESSAGE;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", apiKey);
        requestBody.put("message", message);

        Object[] result = restTemplate.postForObject(url, requestBody, Object[].class);

        return result;
    }

    public List<SenderDomain> getDomains(String apiKey){
        String url = API_BASE_URl+GET_DOMAINS;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("key", apiKey);

        List<SenderDomain> senderDomainList = new ArrayList<>();

        SenderDomain[] result = restTemplate.postForObject(url, requestBody, SenderDomain[].class);

        if(Objects.nonNull(result)) {
            senderDomainList = Arrays.asList(result);
        }

        return senderDomainList;
    }
}
