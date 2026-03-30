package org.siri_hate.main_service.repository;

import org.siri_hate.main_service.model.entity.project.survey.ProjectSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSurveyRepository extends JpaRepository<ProjectSurvey, Long> {
}