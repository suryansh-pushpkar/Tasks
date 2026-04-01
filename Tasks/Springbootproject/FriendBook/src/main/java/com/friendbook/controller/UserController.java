package com.friendbook.controller;

import com.friendbook.dto.ApiMessage;
import com.friendbook.dto.LoginRequest;
import com.friendbook.dto.LoginResponse;
import com.friendbook.dto.ProfileResponse;
import com.friendbook.dto.ProfileUpdateDTO;
import com.friendbook.dto.UserDTO;
import com.friendbook.entity.User;
import com.friendbook.security.AuthenticatedUser;
import com.friendbook.security.JwtService;
import com.friendbook.service.CaptchaService;
import com.friendbook.service.RateLimiterService;
import com.friendbook.service.StorageService;
import com.friendbook.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CaptchaService captchaService;
    private final RateLimiterService rateLimiterService;

    public UserController(UserService userService, StorageService storageService,
                          AuthenticationManager authenticationManager, JwtService jwtService,
                          CaptchaService captchaService, RateLimiterService rateLimiterService) {
        this.userService = userService;
        this.storageService = storageService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.captchaService = captchaService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ApiMessage> register(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        rateLimiterService.checkSignupLimit(clientKey(request, userDTO.getEmail()));
        captchaService.validate(userDTO.getCaptchaToken());
        User user = userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiMessage("Account created. Your username is " + user.getUserName()));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        rateLimiterService.checkLoginLimit(clientKey(httpRequest, request.getEmail()));
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword()));
            AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
            String token = jwtService.generateToken(principal);
            return ResponseEntity.ok(new LoginResponse("Login successful", "/home", token));
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Either email or password is incorrect try again");
        }
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<ApiMessage> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiMessage("Logout successful"));
    }

    @PutMapping("/profile/{username}")
    @ResponseBody
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable String username,
                                                         @Valid @RequestBody ProfileUpdateDTO dto) {
        validateAuthenticatedUsername(username);
        return ResponseEntity.ok(userService.updateProfile(username, dto, null));
    }

    @PostMapping("/profile/{username}/image")
    public ResponseEntity<Void> uploadProfileImage(@PathVariable String username,
                                                   @RequestParam("profileImage") MultipartFile profileImage) {
        validateAuthenticatedUsername(username);
        ProfileUpdateDTO dto = currentProfile(username);
        userService.updateProfile(username, dto, storageService.storeImage(profileImage));
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", "/users/" + username)
            .build();
    }

    private ProfileUpdateDTO currentProfile(String username) {
        User user = userService.getByUsername(username);
        ProfileUpdateDTO dto = new ProfileUpdateDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setFavSongs(user.getFavSongs());
        dto.setFavBooks(user.getFavBooks());
        dto.setFavPlaces(user.getFavPlaces());
        return dto;
    }

    private void validateAuthenticatedUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            throw new IllegalArgumentException("Please login first");
        }
        User currentUser = userService.getById(authenticatedUser.getId());
        if (!currentUser.getUserName().equals(username)) {
            throw new IllegalArgumentException("You can only update your own profile");
        }
    }

    private String clientKey(HttpServletRequest request, String identity) {
        String ip = request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr();
        String normalizedIdentity = identity == null ? "anonymous" : identity.trim().toLowerCase();
        return ip + ":" + normalizedIdentity;
    }
}
