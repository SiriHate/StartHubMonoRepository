package org.siri_hate.main_service.model.entity.project.survey;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.project.Project;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_surveys")
public class ProjectSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false, unique = true)
    private Project project;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveySubmission> submissions = new ArrayList<>();

    public ProjectSurvey() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setSubmissions(List<SurveySubmission> submissions) {
        this.submissions = submissions;
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SurveyQuestion> questions) {
        this.questions = questions;
    }

    public List<SurveySubmission> getSubmissions() {
        return submissions;
    }
}
