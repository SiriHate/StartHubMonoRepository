package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.SurveyAnswerFullResponseDTO;
import org.siri_hate.main_service.dto.SurveyAnswerRequestDTO;
import org.siri_hate.main_service.model.entity.project.survey.SurveyAnswer;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SurveyAnswerMapper {
    SurveyAnswer toSurveyAnswer(SurveyAnswerRequestDTO request);

    SurveyAnswerFullResponseDTO toSurveyAnswerFullResponseDTO(SurveyAnswer surveyAnswer);
}
