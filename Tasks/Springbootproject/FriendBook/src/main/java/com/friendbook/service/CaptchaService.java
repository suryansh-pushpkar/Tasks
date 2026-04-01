package com.friendbook.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaService {

    private final RestTemplate restTemplate;
    private final boolean enabled;
    private final String secret;
    private final String verifyUrl;

    public CaptchaService(RestTemplate restTemplate,
                          @Value("${app.security.captcha.enabled}") boolean enabled,
                          @Value("${app.security.captcha.secret:}") String secret,
                          @Value("${app.security.captcha.verify-url:https://www.google.com/recaptcha/api/siteverify}") String verifyUrl) {
        this.restTemplate = restTemplate;
        this.enabled = enabled;
        this.secret = secret;
        this.verifyUrl = verifyUrl;
    }

    public void validate(String token) {
        if (!enabled) {
            return;
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Captcha verification is required");
        }

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("secret", secret);
        request.add("response", token);

        ResponseEntity<Map> response = restTemplate.postForEntity(verifyUrl, request, Map.class);
        Object success = response.getBody() != null ? response.getBody().get("success") : null;
        if (!(success instanceof Boolean valid) || !valid) {
            throw new IllegalArgumentException("Captcha verification failed");
        }
    }
}
