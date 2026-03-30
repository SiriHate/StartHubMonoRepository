package org.siri_hate.main_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.siri_hate.main_service.dto.SurveyAnswerFullResponseDTO;
import org.siri_hate.main_service.dto.SurveySubmissionFullResponseDTO;
import org.siri_hate.main_service.dto.SurveySubmissionRequestDTO;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.project.survey.ProjectSurvey;
import org.siri_hate.main_service.model.entity.project.survey.SurveyAnswer;
import org.siri_hate.main_service.model.entity.project.survey.SurveySubmission;
import org.siri_hate.main_service.model.mapper.utils.UserResolver;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                UserResolver.class
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SurveySubmissionMapper {

    @Mapping(target = "id", ignore = true)
    SurveySubmission toSurveySubmission(User respondent, ProjectSurvey survey, SurveySubmissionRequestDTO request);

    @Mapping(target = "respondentUsername", source = "respondent", qualifiedByName = "toUsername")
    @Mapping(target = "answers", source = "responses")
    SurveySubmissionFullResponseDTO toSurveySubmissionFullResponseDTO(SurveySubmission surveySubmission);

    @Mapping(target = "answerText", source = "content")
    @Mapping(target = "questionText", source = "question.questionText")
    SurveyAnswerFullResponseDTO toSurveyAnswerFullResponseDTO(SurveyAnswer surveyAnswer);

    List<SurveySubmissionFullResponseDTO> toSurveySubmissionFullResponseListDTO(List<SurveySubmission> surveySubmissions);
}
