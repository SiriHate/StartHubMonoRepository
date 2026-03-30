package org.siri_hate.main_service.controller;

import org.jspecify.annotations.Nullable;
import org.siri_hate.main_service.api.NewsApi;
import org.siri_hate.main_service.dto.NewsFullResponseDTO;
import org.siri_hate.main_service.dto.NewsPageResponseDTO;
import org.siri_hate.main_service.dto.NewsRequestDTO;
import org.siri_hate.main_service.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class NewsController implements NewsApi {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public ResponseEntity<NewsFullResponseDTO> createNews(String xUserName, NewsRequestDTO news, MultipartFile logo) {
        var response = newsService.createNews(xUserName, news, logo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteNews(Long id) {
        newsService.deleteNews(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<NewsPageResponseDTO> getMyNews(String xUserName, @Nullable String category, @Nullable String query, @Nullable Boolean moderationPassed, Integer page, Integer size) {
        var response = newsService.getNews(xUserName, query, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsPageResponseDTO> getNews(String category, String query, Boolean moderationPassed, Integer page, Integer size) {
        var response = newsService.getNews(category, query, page, size, moderationPassed);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsFullResponseDTO> getNewsById(Long id) {
        var response = newsService.getNewsById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsFullResponseDTO> updateNews(Long id, NewsRequestDTO news, MultipartFile logo) {
        var response = newsService.updateNews(id, news, logo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateNewsModerationPassed(Long id, Boolean body) {
        newsService.updateNewsModerationStatus(id, body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}