package org.siri_hate.main_service.model.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.ProjectSurveyFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectSurveyRequestDTO;
import org.siri_hate.main_service.model.entity.project.survey.ProjectSurvey;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProjectSurveyMapper {

    ProjectSurvey toProjectSurvey(ProjectSurveyRequestDTO projectSurveyRequestDTO);

    @AfterMapping
    default void setSurveyOnQuestions(@MappingTarget ProjectSurvey survey) {
        if (survey.getQuestions() != null) {
            survey.getQuestions().forEach(question -> question.setSurvey(survey));
        }
    }

    @Mapping(source = "project.id", target = "projectId")
    ProjectSurveyFullResponseDTO toProjectSurveyFullResponseDTO(ProjectSurvey projectSurvey);
}