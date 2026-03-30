package org.siri_hate.user_service.client;

import jakarta.annotation.PostConstruct;
import org.siri_hate.user_service.model.yandex.YandexUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class YandexClient {

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient
                .builder()
                .baseUrl("https://login.yandex.ru")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public YandexUserInfo fetchYandexUserInfo(String token) {
        return webClient.get()
                .uri("/info")
                .header(HttpHeaders.AUTHORIZATION, "OAuth " + token)
                .retrieve()
                .bodyToMono(YandexUserInfo.class)
                .block();
    }
}
