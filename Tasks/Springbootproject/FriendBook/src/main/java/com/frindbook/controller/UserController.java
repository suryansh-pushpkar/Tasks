package com.frindbook.controller;

import com.frindbook.dto.SignupResponse;
import com.frindbook.dto.UserDTO;
import com.frindbook.dto.UserLoginDTO;
import com.frindbook.utility.CaptchaUtil;
import com.frindbook.utility.JwtUtil;

import java.util.Map;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.frindbook.entity.User;
import com.frindbook.service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    @RateLimiter(name = "authLimiter", fallbackMethod = "signupRateLimitFallback")
    public ResponseEntity<SignupResponse> signup(@RequestBody UserDTO dto) {
        boolean captchaVerified = CaptchaUtil.verifyCaptcha(dto.getCaptchaToken());
        if (!captchaVerified) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new SignupResponse(false, "Security Check Failed: Bot activity detected."));
        }
        try {

            User user = modelMapper.map(dto, User.class);
            UserDTO registeredUser = userService.registerUser(user);
            if (registeredUser != null) {
                return ResponseEntity.ok(new SignupResponse(true, "Registration Successful! Welcome to FriendBook."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SignupResponse(false, "Registration failed. Please check your details."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SignupResponse(false, "An account with this email already exists."));
        }
    }

    @PostMapping("/login")
    @RateLimiter(name = "authLimiter", fallbackMethod = "loginRateLimitFallback")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginDTO dto) {
        User user = userService.authenticateUser(dto.getEmail(), dto.getPassword());
        if (user != null) {
            String token = jwtUtil.generateToken(user.getUserName());
            return ResponseEntity.ok().body(Map.of(
                    "token", token,
                    "message", "Login successful",
                    "username", user.getUserName(),
                    "redirectUrl", "/profile/" + user.getUserName()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Either email or password is incorrect try again"));
        }
    }

    public ResponseEntity<SignupResponse> signupRateLimitFallback(UserDTO dto, Throwable t) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new SignupResponse(false, "Too many attempts. Please try again after 2 minutes."));
    }

    public ResponseEntity<Map<String, String>> loginRateLimitFallback(UserLoginDTO dto, Throwable t) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("message", "Too many attempts. Please try again after 2 minutes."));
    }
}
