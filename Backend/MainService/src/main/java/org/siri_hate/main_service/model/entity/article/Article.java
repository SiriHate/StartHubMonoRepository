package org.siri_hate.main_service.model.entity.article;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "image_key")
    private String imageKey;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ArticleCategory category;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "createdAt", nullable = false)
    private final LocalDate createdAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleComment> articleComments = new ArrayList<>();

    @Column(name = "moderation_passed",  nullable = false)
    private Boolean moderationPassed;

    public Article() {
        this.createdAt = LocalDate.now();
        this.moderationPassed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String logoUrl) {
        this.imageKey = logoUrl;
    }

    public ArticleCategory getCategory() {
        return category;
    }

    public void setCategory(ArticleCategory category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Boolean getModerationPassed() {
        return moderationPassed;
    }

    public void setModerationPassed(Boolean moderationPassed) {
        this.moderationPassed = moderationPassed;
    }

    public List<ArticleLike> getArticleLikes() {
        return articleLikes;
    }

    public void setArticleLikes(List<ArticleLike> articleLikes) {
        this.articleLikes = articleLikes;
    }

    public List<ArticleComment> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(List<ArticleComment> articleComments) {
        this.articleComments = articleComments;
    }
}
