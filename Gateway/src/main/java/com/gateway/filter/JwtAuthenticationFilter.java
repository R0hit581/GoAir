package com.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import org.springframework.http.HttpMethod;
import org.springframework.web.server.ResponseStatusException;

import com.gateway.util.Jwtutil;

import org.springframework.http.HttpStatus;


@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private Jwtutil jwtUtil;

    @Autowired
    private RouteValidator validator;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.error("Missing Authorization header");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                HttpMethod method = exchange.getRequest().getMethod();

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authHeader);
                    String role = jwtUtil.extractRole(authHeader);
                    String path = exchange.getRequest().getURI().getPath();

                    if (!checkRoleAccess(role, path, method)) {
                        logger.error("Unauthorized access: Role '{}' does not have access to path '{}'", role, path);
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access");
                    }
                } catch (Exception e) {
                    logger.error("Invalid access: {}", e.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to the application");
                }
            }
            return chain.filter(exchange);
        };
    }

    private boolean checkRoleAccess(String role, String path, HttpMethod method) {
        if ("ADMIN".equals(role)) {
            return true; // Admin can access everything
        } else if ("USER".equals(role)) {
            return (path.startsWith("/flight/getAll") || 
                    path.startsWith("/api/user/") || 
                    path.startsWith("/flight/get/") || 
                    path.startsWith("/book/add") || 
                    path.startsWith("/book") || 
                    path.startsWith("/book/getByBookingId/") || 
                    path.startsWith("/book/deleteByPassengerId/") || 
                    (path.startsWith("/passenger/get/") && method.equals(HttpMethod.GET)));
        }
        return false; // Default: deny access
    }

    public static class Config {
        // Configuration class remains empty
    }
}