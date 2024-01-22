package com.et.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import com.et.constant.ThirdPartyAppIntegration;
import com.et.domain.PaymentPlan;
import com.et.security.AuthoritiesConstants;
import com.et.security.ExternalTokenType;
import com.et.security.ExternalUserPasswordAuthenticationToken;
import com.et.service.CustomerPaymentService;
import com.et.service.PaymentPlanService;
import com.et.service.dto.AdditionalDetailsDTO;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.utils.HmacAuthUtils;
import com.et.web.rest.vm.CustomerTokenInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import tech.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;

import static com.et.security.AuthoritiesConstants.*;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;
    private Key shopifyKey;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private long shopifyTokenValidityInMilliseconds;

    private long shopifyTokenValidityInMillisecondsForRememberMe;

    private final JHipsterProperties jHipsterProperties;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setDefaultKey();
        setShopifyKey();
    }

    private void setShopifyKey() {
        byte[] keyBytes;
        String secret = "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";
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        this.shopifyKey = Keys.hmacShaKeyFor(keyBytes);
        this.shopifyTokenValidityInMilliseconds =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.shopifyTokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt()
                .getTokenValidityInSecondsForRememberMe();
    }

    private void setDefaultKey() {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt()
                .getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        Collection<SimpleGrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(ADMIN));
        authorities.add(new SimpleGrantedAuthority(MERCHANT_PORTAL));

        String username = claims.getSubject().trim();

        if (username.equals("admin") || username.equals("user")) {
            throw new RuntimeException("Please use Dynamo DB password and username");
        }
        User principal = new User(username, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

/*    public Authentication getExternalAuthentication(String externalToken, String requestPath) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(externalToken)
            .getBody();

        Collection<SimpleGrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String subject = claims.getSubject();

        log.info("subject=" + subject);

        String[] subjectTokens = org.apache.commons.lang3.StringUtils.split(subject, "|");


        String token = null;
        try {
            token = subjectTokens[2];
        } catch (Exception ex) {
        }

        ExternalTokenType externalTokenType = ExternalTokenType.CUSTOMER_PORTAL;

        if (subjectTokens.length >= 2) {
            if (subjectTokens[1].equals("external")) {
                externalTokenType = ExternalTokenType.CUSTOMER_PORTAL;
                if (requestPath.contains("subscription-contracts-update-order-note/") ||
                    requestPath.contains("subscription-customers/") ||
                    requestPath.contains("subscription-custom-csses/") ||
                    requestPath.contains("customer-portal-settings/") ||
                    requestPath.contains("subscription-customers-detail/valid/") ||
                    requestPath.contains("subscription-groups/all-selling-plans") ||
                    requestPath.contains("subscription-contract-one-offs-by-contractId") ||
                    requestPath.contains("subscription-contracts-freeze-status-detail") ||
                    requestPath.contains("subscription-billing-attempts/top-orders") ||
                    requestPath.contains("subscription-contract-details/customer/") ||
                    requestPath.contains("cancellation-managements") ||
                    requestPath.contains("subscription-contracts/contract-external") ||
                    requestPath.contains("subscription-billing-attempts/past-orders") ||
                    requestPath.contains("api/data/product") ||
                    requestPath.contains("api/data/products") ||
                    requestPath.contains("product-swaps-by-variant-groups") ||
                    requestPath.contains("subscription-contracts-add-line-item") ||
                    requestPath.contains("subscription-contracts-remove-line-item") ||
                    requestPath.contains("subscription-contracts-update-line-item-quantity") ||
                    requestPath.contains("subscription-contracts-update-line-item-attributes") ||
                    requestPath.contains("subscription-contracts-update-billing-interval") ||
                    requestPath.contains("subscription-contracts-remove-discount") ||
                    requestPath.contains("subscription-contracts-apply-discount") ||
                    requestPath.contains("subscription-contracts-update-shipping-address") ||
                    requestPath.contains("subscription-billing-attempts/attempt-billing") ||
                    requestPath.contains("subscription-contracts-update-payment-method") ||
                    requestPath.contains("subscription-billing-attempts/skip-order") ||
                    requestPath.contains("subscription-contracts-update-status") ||
                    requestPath.contains("subscription-contracts/") ||
                    requestPath.contains("subscription-contracts-update-billing-date") ||
                    requestPath.contains("subscription-bundlings/external/get-bundle") ||
                    requestPath.contains("subscription-bundle-settings"))
                {
                    log.info("{} customer portal role assigned.", requestPath);
                    authorities.add(new SimpleGrantedAuthority(CUSTOMER_PORTAL));
                } else {
                    authorities.add(new SimpleGrantedAuthority(MERCHANT_PORTAL));
                }
            } else if (subjectTokens[1].equals("external_bundle")) {
                externalTokenType = ExternalTokenType.BUILD_A_BOX;
                if(requestPath.contains("subscription-custom-csses") ||
                    requestPath.contains("subscription-bundlings/external/get-bundle") ||
                    requestPath.contains("subscription-bundle-settings"))
                {
                    authorities.add(new SimpleGrantedAuthority(BUILD_A_BOX));
                } else {
                    authorities.add(new SimpleGrantedAuthority(MERCHANT_PORTAL));
                }
            }
        }

        User principal = new User(subjectTokens[0], "", authorities);
        log.info("came here -> getExternalAuthentication. principal=" + principal);
        return new ExternalUserPasswordAuthenticationToken(principal, externalToken, authorities, token, externalTokenType);
    }*/

    public Authentication getExternalAuthentication(String externalToken) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(externalToken)
            .getBody();

        Collection<SimpleGrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String subject = claims.getSubject();

        log.info("subject=" + subject);

        String[] subjectTokens = org.apache.commons.lang3.StringUtils.split(subject, "|");


        String token = null;
        try {
            token = subjectTokens[2];
        } catch (Exception ex) {
        }

        ExternalTokenType externalTokenType = ExternalTokenType.CUSTOMER_PORTAL;

        if (subjectTokens.length >= 2) {
            if (subjectTokens[1].equals("external")) {
                externalTokenType = ExternalTokenType.CUSTOMER_PORTAL;
                authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.CUSTOMER_PORTAL));
            } else if (subjectTokens[1].equals("external_bundle")) {
                externalTokenType = ExternalTokenType.BUILD_A_BOX;
                authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.BUILD_A_BOX));
            }
        }

        User principal = new User(subjectTokens[0], "", authorities);
        log.info("came here -> getExternalAuthentication. principal=" + principal);
        return new ExternalUserPasswordAuthenticationToken(principal, externalToken, authorities, token, externalTokenType);
    }

    public Authentication getShopifyProxyAuthentication(HttpServletRequest httpServletRequest) throws Exception {

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        log.info("Getting authentication after HMAC validation");

        String shop = Optional.ofNullable(httpServletRequest.getParameter("shop")).orElse("").trim();

        String token = null;

        String loggedInCustomerIdString = httpServletRequest.getParameter("logged_in_customer_id");

        if (org.apache.commons.lang3.StringUtils.isNotBlank(loggedInCustomerIdString)) {
            Long customerId = Long.parseLong(loggedInCustomerIdString);
            log.info("Customer id found: {}", customerId);
            CustomerTokenInfo customerTokenInfo = customerPaymentService.getCustomerTokenInfo(customerId, shop);
            token = customerTokenInfo.getToken();
        } else if (httpServletRequest.getParameterMap().containsKey("token")) {
            token = httpServletRequest.getParameterMap().get("token")[0];
            log.info("token found: {}", token);
        } else {

        }

        ExternalTokenType externalTokenType = ExternalTokenType.CUSTOMER_PORTAL;
        if (httpServletRequest.getServletPath().startsWith("/subscriptions/cp/api")) {
            externalTokenType = ExternalTokenType.CUSTOMER_PORTAL;
            authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.CUSTOMER_PORTAL));
            log.info("Authority Added CUSTOMER_PORTAL");
        } else if (httpServletRequest.getServletPath().startsWith("/subscriptions/bb/api")) {
            externalTokenType = ExternalTokenType.BUILD_A_BOX;
            authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.BUILD_A_BOX));
            log.info("Authority Added BUILD_A_BOX");
        }

        User principal = new User(shop, "", authorities);

        return new ExternalUserPasswordAuthenticationToken(principal, "", authorities, token, externalTokenType);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public boolean validateExternalToken(String externalAuthToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(externalAuthToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public boolean validateShopifyToken(String shopifyJwt) {
        try {
            Jwts.parser().setSigningKey(shopifyKey).parseClaimsJws(shopifyJwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public boolean validateHmac(Map<String, String[]> parameterMap) {
        try {
            Map<String, OAuth2ClientProperties.Registration> registrationMap = oAuth2ClientProperties.getRegistration();
            OAuth2ClientProperties.Registration registration = registrationMap.get("subscription");
            String clientSecret = registration.getClientSecret();

            String signature = Optional.ofNullable(parameterMap.get("signature")).map(s -> org.apache.commons.lang3.StringUtils.join(s, ",")).orElse("");

            Optional<String> signature1 = parameterMap.entrySet().stream().filter(s -> !s.getKey().equals("signature"))
                .sorted(Map.Entry.comparingByKey())
                .map(o1 -> o1.getKey() + "=" + org.apache.commons.lang3.StringUtils.join(o1.getValue(), ","))
                .reduce((o1, o2) -> o1 + o2);

            if (signature1.isPresent()) {
                String message = signature1.get();
                log.info("messageBody=" + message + " signature=" + signature + " clientSecret=" + clientSecret);
                return HmacAuthUtils.checkHmac(message, signature, clientSecret);
            }
        } catch (Exception ex) {
            log.info("Hmac is not valid");
        }
        return false;
    }


    public Authentication getAuthenticationForShop(String shop) {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(MERCHANT_PORTAL));

        User principal = new User(shop, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Authentication getExternalApiKeyAuthentication(String apiKey, String appKey) {
        if (!org.apache.commons.lang3.StringUtils.isNotBlank(apiKey)) {
            return null;
        }

        Optional<ShopInfoDTO> shopInfoDTOOptional = commonUtils.getShopInfoByAPIKey(apiKey);

        if (shopInfoDTOOptional.isEmpty()) {
            return null;
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOptional.get();

        String shop = shopInfoDTO.getShop();
        Optional<PaymentPlan> paymentPlan = paymentPlanService.findByShop(shop);

        if (paymentPlan.isEmpty()) {
            return null;
        }

        AdditionalDetailsDTO additionalDetailsDTO = CommonUtils.fromJSONIgnoreUnknownProperty(
            new TypeReference<>() {
            },
            paymentPlan.get().getAdditionalDetails()
        );

        if (org.apache.commons.lang3.StringUtils.isNotBlank(appKey)) {
            Optional<ThirdPartyAppIntegration> thirdPartyAppIntegrationOptional = ThirdPartyAppIntegration.findByAppKey(appKey);
            if(thirdPartyAppIntegrationOptional.isEmpty()) {
                return null;
            }

            boolean isThirdPartAppEnabled = false;
            switch (thirdPartyAppIntegrationOptional.get()) {
                case SHIP_INSURE:
                    isThirdPartAppEnabled = BooleanUtils.isTrue(shopInfoDTO.isShipInsureEnabled());
                    break;
                default:
            }

            if(!isThirdPartAppEnabled) {
                return null;
            }

        } else if (!Optional.ofNullable(additionalDetailsDTO.getEnableExternalApi()).orElse(false)) {
            return null;
        }

        return getAuthenticationForShop(shop);

    }


    public Authentication getShopifyAuthentication(String shopifyJwt) {
        Claims claims = Jwts.parser()
            .setSigningKey(shopifyKey)
            .parseClaimsJws(shopifyJwt)
            .getBody();

        String shop = claims.getIssuer();
        shop = shop.replace("https://", "");
        shop = shop.replace("/admin", "");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(MERCHANT_PORTAL));
        User principal = new User(shop.trim(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, shopifyJwt, authorities);
    }
}
