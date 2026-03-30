package org.siri_hate.main_service.model.entity.news;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_comments")
public class NewsComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt;

    public NewsComment() {
        this.createdAt = LocalDateTime.now();
    }

    public NewsComment(User author, News news, String text) {
        this();
        this.author = author;
        this.text = text;
        this.news = news;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public News getNews() { return news; }
    public void setNews(News news) { this.news = news; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
