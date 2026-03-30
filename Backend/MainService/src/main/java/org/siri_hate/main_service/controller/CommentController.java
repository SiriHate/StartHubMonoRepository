package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ArticleCommentApi;
import org.siri_hate.main_service.api.NewsCommentApi;
import org.siri_hate.main_service.api.ProjectCommentApi;
import org.siri_hate.main_service.dto.CommentRequestDTO;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.service.ArticleCommentService;
import org.siri_hate.main_service.service.CommentService;
import org.siri_hate.main_service.service.NewsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController implements ProjectCommentApi, NewsCommentApi, ArticleCommentApi {

    private final CommentService commentService;
    private final NewsCommentService newsCommentService;
    private final ArticleCommentService articleCommentService;

    @Autowired
    public CommentController(
            CommentService commentService,
            NewsCommentService newsCommentService,
            ArticleCommentService articleCommentService
    ) {
        this.commentService = commentService;
        this.newsCommentService = newsCommentService;
        this.articleCommentService = articleCommentService;
    }

    // ==================== Project Comments ====================

    @Override
    public ResponseEntity<CommentResponseDTO> createProjectComment(Long projectId, String xUserName, CommentRequestDTO commentRequestDTO) {
        var response = commentService.createComment(xUserName, projectId, commentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long id, String xUserName) {
        commentService.deleteComment(id, xUserName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<CommentResponseDTO>> getProjectComments(Long projectId) {
        var response = commentService.getProjectComments(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== News Comments ====================

    @Override
    public ResponseEntity<CommentResponseDTO> createNewsComment(Long newsId, String xUserName, CommentRequestDTO commentRequestDTO) {
        var response = newsCommentService.createComment(xUserName, newsId, commentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteNewsComment(Long id, String xUserName) {
        newsCommentService.deleteComment(id, xUserName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<CommentResponseDTO>> getNewsComments(Long newsId) {
        var response = newsCommentService.getNewsComments(newsId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== Article Comments ====================

    @Override
    public ResponseEntity<CommentResponseDTO> createArticleComment(Long articleId, String xUserName, CommentRequestDTO commentRequestDTO) {
        var response = articleCommentService.createComment(xUserName, articleId, commentRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteArticleComment(Long id, String xUserName) {
        articleCommentService.deleteComment(id, xUserName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<CommentResponseDTO>> getArticleComments(Long articleId) {
        var response = articleCommentService.getArticleComments(articleId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
