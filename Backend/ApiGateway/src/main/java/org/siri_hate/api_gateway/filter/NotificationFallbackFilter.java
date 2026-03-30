package org.siri_hate.api_gateway.filter;

import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;

@Component
public class NotificationFallbackFilter implements GlobalFilter, Ordered {

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (!path.startsWith("/api/v1/notification_service/")) {
            return chain.filter(exchange);
        }

        return chain.filter(exchange).onErrorResume(ConnectException.class, ex -> {
            if ("/api/v1/notification_service/in-app".equals(path)) {
                return writeJson(exchange, HttpStatus.OK, "[]");
            }
            if ("/api/v1/notification_service/in-app/unread-count".equals(path)) {
                return writeJson(exchange, HttpStatus.OK, "{\"count\":0}");
            }
            return writeJson(exchange, HttpStatus.SERVICE_UNAVAILABLE, "{\"error\":\"notification_service_unavailable\"}");
        });
    }

    private Mono<Void> writeJson(ServerWebExchange exchange, HttpStatus status, String body) {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
