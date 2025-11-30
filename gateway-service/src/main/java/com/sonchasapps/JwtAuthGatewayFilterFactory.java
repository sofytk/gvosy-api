package com.sonchasapps;

import com.sonchasapps.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
@Component
public class JwtAuthGatewayFilterFactory
        extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public static class Config {
        // Configuration properties if needed
    }

    public JwtAuthGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
        System.out.println("=== JwtAuthGatewayFilterFactory CREATED ===");
    }

    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("=== JwtAuthGatewayFilterFactory.apply() CALLED ===");

        return (exchange, chain) -> {
            System.out.println("\n=== JWT AUTH FILTER START ===");
            System.out.println("URI: " + exchange.getRequest().getURI());
            System.out.println("Method: " + exchange.getRequest().getMethod());
            System.out.println("Path: " + exchange.getRequest().getPath());

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            System.out.println("Authorization header: " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("❌ ERROR: Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            System.out.println("Token (first 30 chars): " +
                    (token.length() > 30 ? token.substring(0, 30) + "..." : token));

            boolean valid = jwtService.validateToken(token);
            System.out.println("Token valid: " + valid);

            if (!valid) {
                System.out.println("❌ ERROR: Token validation failed");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String userId = jwtService.extractUserId(token);
            System.out.println("Extracted userId: " + userId);

            if (userId == null || userId.isEmpty()) {
                System.out.println("❌ ERROR: userId is null or empty");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId)
                    .build();

            System.out.println("✅ SUCCESS: Added X-User-Id = " + userId);
            System.out.println("=== JWT AUTH FILTER END ===\n");

            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}