package org.siri_hate.api_gateway.security;

import org.jspecify.annotations.NonNull;
import org.siri_hate.api_gateway.model.CustomGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;
    private final CustomGatewayProperties customGatewayProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public JwtAuthFilter(JwtService jwtService, CustomGatewayProperties customGatewayProperties) {
        this.jwtService = jwtService;
        this.customGatewayProperties = customGatewayProperties;
    }

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();

        if (path.startsWith("/ws")) {
            return handleWebSocketAuth(exchange, chain);
        }

        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }

        return handleHttpAuth(exchange, chain);
    }

    private boolean isExcludedPath(String path) {
        if (customGatewayProperties.getFilterExcludePaths() == null || customGatewayProperties.getFilterExcludePaths().isEmpty()) {
            return false;
        }
        for (String pattern : customGatewayProperties.getFilterExcludePaths()) {
            if (pattern != null && antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> handleHttpAuth(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        return validateAndForward(exchange, chain, token);
    }

    private Mono<Void> handleWebSocketAuth(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token == null || token.isBlank()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return validateAndForward(exchange, chain, token);
    }

    private Mono<Void> validateAndForward(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        String username;
        String roles;
        try {
            if (!jwtService.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            username = jwtService.extractUsername(token);
            roles = jwtService.extractRoles(token);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> {
                    builder.header("X-User-Name", username);
                    builder.header("X-User-Roles", roles);
                    builder.headers(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION));
                })
                .build();
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}