package org.siri_hate.main_service.model.entity.project.survey;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.siri_hate.main_service.model.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_submissions")
public class SurveySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private ProjectSurvey survey;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User respondent;

    @Column(name = "submitted_at", nullable = false)
    private final LocalDateTime submittedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyAnswer> responses = new ArrayList<>();

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    public SurveySubmission() {
        this.submittedAt = LocalDateTime.now();
    }

    public SurveySubmission(ProjectSurvey survey, User respondent) {
        this();
        this.survey = survey;
        this.respondent = respondent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(ProjectSurvey survey) {
        this.survey = survey;
    }

    public User getRespondent() {
        return respondent;
    }

    public void setRespondent(User respondent) {
        this.respondent = respondent;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public List<SurveyAnswer> getResponses() {
        return responses;
    }

    public void setResponses(List<SurveyAnswer> responses) {
        this.responses = responses;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer averageRating) {
        this.rating = averageRating;
    }
}
