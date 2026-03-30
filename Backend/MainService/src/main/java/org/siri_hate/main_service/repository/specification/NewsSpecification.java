package org.siri_hate.main_service.repository.specification;

import jakarta.persistence.criteria.Join;
import org.siri_hate.main_service.model.entity.article.Article;
import org.siri_hate.main_service.model.entity.article.ArticleCategory;
import org.siri_hate.main_service.model.entity.news.News;
import org.springframework.data.jpa.domain.Specification;

public class NewsSpecification {

    public static Specification<News> titleStartsWith(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(root.get("title"), title + "%");
        };
    }

    public static Specification<News> hasCategory(String category) {
        return (root, query, cb) -> {
            if (category == null || category.isBlank()) {
                return cb.conjunction();
            }
            Join<Article, ArticleCategory> categoryJoin = root.join("category");
            return cb.equal(categoryJoin.get("name"), category);
        };
    }

    public static Specification<News> moderationPassed(Boolean moderationPassed) {
        return (root, query, cb) -> {
            if (moderationPassed == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("moderationPassed"), moderationPassed);
        };
    }

    public static Specification<News> hasUserUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("author").get("username"), username);
        };
    }
}
