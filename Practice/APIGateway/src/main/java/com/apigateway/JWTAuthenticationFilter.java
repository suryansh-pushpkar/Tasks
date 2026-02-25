package com.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import jakarta.ws.rs.core.HttpHeaders;
import reactor.core.publisher.Mono;
@Component
public class JWTAuthenticationFilter implements GlobalFilter, Ordered{
	private final JWTUtil jwt;
	
	public JWTAuthenticationFilter(JWTUtil jwt) {
		this.jwt = jwt;
		}
	
	public int getOrder() {
		return -1;
	}
	
	public Mono<Void> filter( ServerWebExchange exchange ,GatewayFilterChain chain){
		ServerHttpRequest request = exchange.getRequest();
		String requestedRoute = request.getURI().getPath();
		
		if(requestedRoute.startsWith("/auth")||requestedRoute.startsWith("/auth/signin")) {
			return chain.filter(exchange);
		}
		
		if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
			return unauthorized(exchange,"Authorization Header is missing");
			
		}
		
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		System.out.println(authHeader);
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			return unauthorized(exchange, "Invalid Authorization Header");
		}
		
		String token  = authHeader.substring(7);
		try {
			jwt.validateToken(token);
		}catch(Exception e) {
			return unauthorized(exchange,"Invalid JWT Token");
		}
		return chain.filter(exchange);
		
		}


	public  Mono<Void> unauthorized(ServerWebExchange exchange,String message){
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}
}
