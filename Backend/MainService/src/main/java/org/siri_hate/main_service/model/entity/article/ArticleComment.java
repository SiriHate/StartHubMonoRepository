package org.siri_hate.main_service.model.entity.article;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_comments")
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt;

    public ArticleComment() {
        this.createdAt = LocalDateTime.now();
    }

    public ArticleComment(User author, Article article, String text) {
        this();
        this.author = author;
        this.text = text;
        this.article = article;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
