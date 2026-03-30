package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.project.survey.ProjectSurvey;
import org.siri_hate.main_service.model.entity.project.survey.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {
    boolean existsBySurveyAndRespondent(ProjectSurvey survey, User respondent);

    List<SurveySubmission> findBySurvey(ProjectSurvey survey);
} 