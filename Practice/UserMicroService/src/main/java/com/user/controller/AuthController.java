package com.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.JwtUtil;
import com.user.dto.OtpRequest;
import com.user.dto.UserDTO;
import com.user.entity.RefreshToken;
import com.user.entity.User;
import com.user.service.RefreshTokenService;
import com.user.service.UserService;
import com.user.utility.EmailUtil;
import com.user.utility.OtpGenerator;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService detailsService;
	private final JwtUtil jwt;
	private final RefreshTokenService refreshTokenService;

	private final HashMap<String, User> temporaryStorage = new HashMap<>();

	public AuthController(UserService userService, AuthenticationManager authenticationManager,
			UserDetailsService detailsService, JwtUtil jwt, RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.detailsService = detailsService;
		this.jwt = jwt;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping
	public ResponseEntity<?> registerRequest(@RequestBody User user) {
		String otp = OtpGenerator.generateOtp();
		EmailUtil.sendOtpEmail(user.getEmail(), user.getUsername(), otp);
		temporaryStorage.put(user.getEmail(), user);
		return ResponseEntity.ok("OTP sent to your email.");
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyAndRegister(@RequestBody OtpRequest request) {
		if (OtpGenerator.verifyOtp(request.getOtp())) {
			User user = temporaryStorage.get(request.getEmail());
			if (user != null) {
				UserDTO savedUser = userService.registerUser(user);
				temporaryStorage.remove(request.getEmail());
				UserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());
				RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());
				String token = jwt.generateToken(userDetails);
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("message", "Sign in success");
				hm.put("token", token);
				hm.put("User", savedUser);
				hm.put("refreshToken", refreshToken);
				return ResponseEntity.ok(hm);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session expired.");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody User user) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		UserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());
		String token = jwt.generateToken(userDetails);
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("message", "Sign in success");
		hm.put("token", token);
		return ResponseEntity.ok(hm);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
		String requestToken = request.get("refreshToken");

		RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
				.orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
		refreshTokenService.verifyExpiration(refreshToken);

		User user = refreshToken.getUser();

		UserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());

		String newJwtToken = jwt.generateToken(userDetails);

		HashMap<String, String> response = new HashMap<>();
		response.put("accessToken", newJwtToken);
		response.put("refreshToken", requestToken);

		return ResponseEntity.ok(response);
	}
}