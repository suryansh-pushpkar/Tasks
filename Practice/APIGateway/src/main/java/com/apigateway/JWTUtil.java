package com.apigateway;

import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JWTUtil {
	private final String SECRETKEY = "MyNameIsSuryanshPushpkarWorkingAtWebkorps";

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
	    .setSigningKey(Keys.hmacShaKeyFor(SECRETKEY.getBytes()))
        .build()
        .parseClaimsJws(token)
        .getBody();
	}
	
	public void validateToken(String token) {
		extractClaims(token);
	}
}
