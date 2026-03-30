package org.siri_hate.main_service.model.entity.project.survey;

import jakarta.persistence.*;

@Entity
@Table(name = "survey_answers")
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private SurveySubmission submission;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    @Column(nullable = false)
    private String content;

    public SurveyAnswer() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveySubmission getSubmission() {
        return submission;
    }

    public void setSubmission(SurveySubmission submission) {
        this.submission = submission;
    }

    public SurveyQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SurveyQuestion question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
