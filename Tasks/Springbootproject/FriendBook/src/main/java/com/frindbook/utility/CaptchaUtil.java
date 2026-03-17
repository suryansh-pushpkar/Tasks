package com.frindbook.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class CaptchaUtil {

    @Value("${google.recaptcha.secret}")
    private String secretKey;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify?secret={secret}&response={response}";

    public boolean verifyCaptcha(String token) {
        RestTemplate restTemplate = new RestTemplate();

        // Google returns a JSON response
        Map<String, Object> response = restTemplate.postForObject(VERIFY_URL, null, Map.class, secretKey, token);

        boolean success = (Boolean) response.get("success");
        double score = (Double) response.get("score");

        // Logic: Must be successful AND score > 0.5 (1.0 is very likely a human)
        return success && score >= 0.5;
    }
}