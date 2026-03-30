package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.CommentRequestDTO;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.article.Article;
import org.siri_hate.main_service.model.entity.article.ArticleComment;
import org.siri_hate.main_service.model.mapper.ArticleCommentMapper;
import org.siri_hate.main_service.repository.ArticleCommentRepository;
import org.siri_hate.main_service.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ArticleCommentMapper articleCommentMapper;

    @Autowired
    public ArticleCommentService(
            ArticleCommentRepository articleCommentRepository,
            ArticleRepository articleRepository,
            UserService userService,
            ArticleCommentMapper articleCommentMapper
    ) {
        this.articleCommentRepository = articleCommentRepository;
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.articleCommentMapper = articleCommentMapper;
    }

    @Transactional
    public CommentResponseDTO createComment(String username, Long articleId, CommentRequestDTO request) {
        Article article = articleRepository.findById(articleId).orElseThrow(EntityNotFoundException::new);
        User user = userService.findOrCreateUser(username);
        ArticleComment comment = new ArticleComment(user, article, request.getText());
        articleCommentRepository.save(comment);
        return articleCommentMapper.toCommentResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        ArticleComment comment = articleCommentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
        String commentOwnerUsername = comment.getAuthor().getUsername();
        if (!commentOwnerUsername.equals(username)) {
            throw new RuntimeException("Only the author can delete the comment");
        }
        articleCommentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getArticleComments(Long articleId) {
        List<ArticleComment> comments = articleCommentRepository.findByArticleId(articleId);
        return articleCommentMapper.toCommentResponseListDTO(comments);
    }
}
