package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.article.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findByArticleId(Long articleId);
}
