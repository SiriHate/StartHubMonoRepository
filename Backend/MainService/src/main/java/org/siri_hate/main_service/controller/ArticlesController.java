package org.siri_hate.main_service.controller;

import org.jspecify.annotations.Nullable;
import org.siri_hate.main_service.api.ArticleApi;
import org.siri_hate.main_service.dto.ArticleFullResponseDTO;
import org.siri_hate.main_service.dto.ArticlePageResponseDTO;
import org.siri_hate.main_service.dto.ArticleRequestDTO;
import org.siri_hate.main_service.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ArticlesController implements ArticleApi {

    private final ArticleService articleService;

    @Autowired
    public ArticlesController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public ResponseEntity<ArticleFullResponseDTO> createArticle(String xUserName, ArticleRequestDTO article, MultipartFile logo) {
        var response = articleService.createArticle(xUserName, article, logo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteArticle(Long id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ArticleFullResponseDTO> getArticle(Long id) {
        var response = articleService.getArticle(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArticlePageResponseDTO> getArticles(String category, String query, Boolean moderationPassed, Integer page, Integer size) {
        var response = articleService.getArticles(category, query, page, size, moderationPassed);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArticlePageResponseDTO> getMyArticles(String xUserName, @Nullable String query, Integer page, Integer size) {
        var response = articleService.getArticles(xUserName, query, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArticleFullResponseDTO> updateArticle(Long id, ArticleRequestDTO article, MultipartFile logo) {
        var response = articleService.updateArticle(id, article, logo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateArticleModerationPassed(Long id, Boolean body) {
        articleService.updateArticleModerationStatus(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}