package org.siri_hate.main_service.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.siri_hate.main_service.model.entity.article.Article;
import org.siri_hate.main_service.model.entity.news.News;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.ProjectMember;
import org.siri_hate.main_service.model.entity.project.survey.SurveySubmission;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "username_idx",  columnList="username", unique = true)
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Article> articles;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<News> news;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> ownedProjects;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectMember> memberships = new HashSet<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "respondent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SurveySubmission> surveySubmissions = new HashSet<>();

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public Set<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public Set<ProjectMember> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<ProjectMember> memberProjects) {
        this.memberships = memberProjects;
    }

    public Set<SurveySubmission> getSurveySubmissions() {
        return surveySubmissions;
    }

    public void setSurveySubmissions(Set<SurveySubmission> surveySubmissions) {
        this.surveySubmissions = surveySubmissions;
    }
}
