package org.siri_hate.main_service.model.entity.news;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

@Entity
@Table(
        name = "news_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "news_id"})
)
public class NewsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    public NewsLike() {}

    public NewsLike(User user, News news) {
        this.user = user;
        this.news = news;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public News getNews() { return news; }
}
