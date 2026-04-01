package com.friendbook.service;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    private final int signupMaxAttempts;
    private final int signupWindowMinutes;
    private final int loginMaxAttempts;
    private final int loginWindowMinutes;

    public RateLimiterService(StringRedisTemplate redisTemplate,
                              @Value("${app.security.rate-limit.signup.max-attempts:5}") int signupMaxAttempts,
                              @Value("${app.security.rate-limit.signup.window-minutes:10}") int signupWindowMinutes,
                              @Value("${app.security.rate-limit.login.max-attempts:10}") int loginMaxAttempts,
                              @Value("${app.security.rate-limit.login.window-minutes:10}") int loginWindowMinutes) {
        this.redisTemplate = redisTemplate;
        this.signupMaxAttempts = signupMaxAttempts;
        this.signupWindowMinutes = signupWindowMinutes;
        this.loginMaxAttempts = loginMaxAttempts;
        this.loginWindowMinutes = loginWindowMinutes;
    }

    public void checkSignupLimit(String key) {
        checkLimit("signup", key, signupMaxAttempts, signupWindowMinutes);
    }

    public void checkLoginLimit(String key) {
        checkLimit("login", key, loginMaxAttempts, loginWindowMinutes);
    }

    private void checkLimit(String action, String key, int maxAttempts, int windowMinutes) {
        String redisKey = "friendbook:" + action + ":" + key;
        try {
            Long attempts = redisTemplate.opsForValue().increment(redisKey);
            if (attempts != null && attempts == 1L) {
                redisTemplate.expire(redisKey, Duration.ofMinutes(windowMinutes));
            }
            if (attempts != null && attempts > maxAttempts) {
                throw new IllegalArgumentException("Too many " + action + " attempts. Please try again later.");
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ignored) {
            // Fail open if Redis is temporarily unavailable.
        }
    }
}
