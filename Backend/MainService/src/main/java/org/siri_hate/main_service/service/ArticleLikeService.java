package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.article.Article;
import org.siri_hate.main_service.model.entity.article.ArticleLike;
import org.siri_hate.main_service.repository.ArticleLikeRepository;
import org.siri_hate.main_service.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;

    @Autowired
    public ArticleLikeService(
            ArticleLikeRepository articleLikeRepository,
            ArticleRepository articleRepository,
            UserService userService
    ) {
        this.articleLikeRepository = articleLikeRepository;
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @Transactional
    public boolean toggleLike(String username, Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(EntityNotFoundException::new);
        User user = userService.findOrCreateUser(username);
        Optional<ArticleLike> existingLike = articleLikeRepository.findByArticleIdAndUserId(articleId, user.getId());
        if (existingLike.isPresent()) {
            articleLikeRepository.delete(existingLike.get());
            return false;
        } else {
            articleLikeRepository.save(new ArticleLike(user, article));
            return true;
        }
    }

    @Transactional(readOnly = true)
    public long getLikesCount(Long articleId) {
        return articleLikeRepository.countByArticleId(articleId);
    }
}
