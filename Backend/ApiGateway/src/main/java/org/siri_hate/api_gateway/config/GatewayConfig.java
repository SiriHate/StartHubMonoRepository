package org.siri_hate.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${services.user-service-url:http://localhost:8081}")
    private String userServiceUrl;

    @Value("${services.notification-service-url:http://localhost:8082}")
    private String notificationServiceUrl;

    @Value("${services.main-service-url:http://localhost:8083}")
    private String mainServiceUrl;

    @Value("${services.chat-service-url:http://localhost:8084}")
    private String chatServiceUrl;

    @Value("${services.chat-service-ws-url:ws://localhost:8084}")
    private String chatServiceWsUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_service_route", r -> r.path("/api/v?/user_service/**")
                        .filters(f -> f.addRequestHeader("X-Gateway", "true"))
                        .uri(userServiceUrl))
                .route("notification_service_route", r -> r.path("/api/v?/notification_service/**")
                        .filters(f -> f.addRequestHeader("X-Gateway", "true"))
                        .uri(notificationServiceUrl))
                .route("main_service_route", r -> r.path("/api/v?/main_service/**")
                        .filters(f -> f.addRequestHeader("X-Gateway", "true"))
                        .uri(mainServiceUrl))
                .route("chat_service_ws_route", r -> r.path("/ws*")
                        .filters(f -> f.addRequestHeader("X-Gateway", "true"))
                        .uri(chatServiceWsUrl))
                .route("chat_service_route", r -> r.path("/api/v?/chat_service/**")
                        .filters(f -> f.addRequestHeader("X-Gateway", "true"))
                        .uri(chatServiceUrl))
                .build();
    }
}