package com.et.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacAuthUtils {

    private static final Logger log = LoggerFactory.getLogger(HmacAuthUtils.class);

    public static final String HMAC_ALGORITHM = "HmacSHA256";


    public static String calculateHmac(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        if (StringUtils.isBlank(message)) {
            return null;
        }

        Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
        hmac.init(key);

        return  Hex.encodeHexString(hmac.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }

    public static boolean checkHmac(String message, String hmac, String secret) {
        boolean isHmacValid = false;
        try {
            if (StringUtils.isNotBlank(hmac)) {
                isHmacValid = hmac.equals(calculateHmac(message, secret));
            }
        } catch (Exception e) {
            log.error("Error while verifying HMAC.", e);
            return false;
        }

        return isHmacValid;
    }
}
