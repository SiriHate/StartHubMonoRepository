package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ArticleLikeApi;
import org.siri_hate.main_service.api.NewsLikeApi;
import org.siri_hate.main_service.service.ArticleLikeService;
import org.siri_hate.main_service.service.NewsLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikeController implements NewsLikeApi, ArticleLikeApi {

    private final NewsLikeService newsLikeService;
    private final ArticleLikeService articleLikeService;

    @Autowired
    public LikeController(NewsLikeService newsLikeService, ArticleLikeService articleLikeService) {
        this.newsLikeService = newsLikeService;
        this.articleLikeService = articleLikeService;
    }

    @Override
    public ResponseEntity<Boolean> toggleNewsLike(Long id, String xUserName) {
        var response = newsLikeService.toggleLike(xUserName, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> getNewsLikesCount(Long id) {
        var response = newsLikeService.getLikesCount(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> toggleArticleLike(Long id, String xUserName) {
        var response = articleLikeService.toggleLike(xUserName, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> getArticleLikesCount(Long id) {
        var response = articleLikeService.getLikesCount(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
