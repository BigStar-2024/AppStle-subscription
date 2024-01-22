package com.et.api.klaviyo;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public abstract class ApiBinding {

    protected RestTemplate restTemplate;

    protected String privateKey;
    protected String publicKey;

    public ApiBinding(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add(getBearerTokenInterceptor(privateKey, publicKey));
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String privateKey, final String publicKey) {
        return (request, bytes, execution) -> {
            request.getHeaders().add("api-key", privateKey);
            return execution.execute(request, bytes);
        };
    }
}
