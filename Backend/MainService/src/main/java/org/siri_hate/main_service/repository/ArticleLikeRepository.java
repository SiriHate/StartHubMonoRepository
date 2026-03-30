package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.article.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);
    long countByArticleId(Long articleId);
}
