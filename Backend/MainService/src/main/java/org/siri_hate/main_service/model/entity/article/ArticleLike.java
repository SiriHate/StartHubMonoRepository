package org.siri_hate.main_service.model.entity.article;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

@Entity
@Table(
        name = "article_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "article_id"})
)
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public ArticleLike() {}

    public ArticleLike(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Article getArticle() { return article; }
}
