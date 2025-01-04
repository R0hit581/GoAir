package com.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.function.Predicate;

/**
 * RouteValidator is a component responsible for validating routes to determine if they require security/authentication.
 * It defines a set of open API endpoints that do not require authentication.
 * It provides a predicate to check if a given request is secured based on its URI path.
 */
@Component
public class RouteValidator {

    private static final Logger logger = LoggerFactory.getLogger(RouteValidator.class);

    // Set of open API endpoints that do not require authentication
    public static final Set<String> openApiEndpoints = Set.of(
            "/auth/register",
            "/auth/token",
            "/auth/validate"
    );

    // Predicate to check if a request is secured
    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                boolean isSecured = openApiEndpoints.stream().noneMatch(uri -> uri.equals(path));
                if (!isSecured) {
                    logger.debug("Unsecured access to: {}", path);
                }
                return isSecured;
            };

}