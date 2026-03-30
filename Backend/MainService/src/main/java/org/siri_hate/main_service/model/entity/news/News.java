package org.siri_hate.main_service.model.entity.news;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "news")
public class News {

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
    private NewsCategory category;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Column(name = "created_at",  nullable = false)
    private final LocalDate createdAt;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsLike> newsLikes = new ArrayList<>();

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NewsComment> newsComments = new ArrayList<>();

    @Column(name = "moderation_passed",  nullable = false)
    private Boolean moderationPassed;

    public News() {
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

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public NewsCategory getCategory() {
        return category;
    }

    public void setCategory(NewsCategory category) {
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

    public List<NewsLike> getNewsLikes() {
        return newsLikes;
    }

    public void setNewsLikes(List<NewsLike> newsLikes) {
        this.newsLikes = newsLikes;
    }

    public List<NewsComment> getNewsComments() {
        return newsComments;
    }

    public void setNewsComments(List<NewsComment> newsComments) {
        this.newsComments = newsComments;
    }
}
