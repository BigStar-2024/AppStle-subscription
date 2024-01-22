package com.et.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ClientForwardController {

    private final Logger log = LoggerFactory.getLogger(ClientForwardController.class);

    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     *
     * @return forward to client {@code index.html}.
     */
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forward(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("host", "dGVzdC1zdGFnZS0xMS5teXNob3BpZnkuY29tL2FkbWlu");
        response.addHeader("shop", "test-stage-11.myshopify.com");
        log.info("came here ClientForwardController forward. Referrer=" + Optional.ofNullable(request.getHeader("referer")).orElse(""));
        return "forward:/";
    }

    @GetMapping(value = "/")
    public String forwardWelcome(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {

        String referer = Optional.ofNullable(request.getHeader("referer")).orElse("");
        log.info("came here ClientForwardController forward. Referrer=" + referer);

        if (StringUtils.isNotBlank(referer)) {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(referer), StandardCharsets.UTF_8);
            Map<String, String> collect = params.stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue, (s1, s2) -> s1));
            /*List<NameValuePair> params = URLEncodedUtils.parse(new URI(referer), StandardCharsets.UTF_8);
            URIBuilder uriBuilder = new URIBuilder("/");

            for (NameValuePair param : params) {
                log.info(param.getName() + " : " + param.getValue());
                response.addHeader(param.getName(), param.getValue());
                uriBuilder.addParameter(param.getName(), param.getValue()).build();
            }*/

            Map<String, String[]> parameterMap = request.getParameterMap();
            if (!parameterMap.containsKey("shop")
                && !parameterMap.containsKey("host")
                && collect.containsKey("shop")
                && collect.containsKey("host")) {
                return "redirect:" + referer;
            } else {
                return "forward:index.html";

            }
        } else {
            return "forward:index.html";
        }

    }
}
