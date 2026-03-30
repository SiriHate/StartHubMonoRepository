package org.siri_hate.main_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.ProjectSurveyFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectSurveyRequestDTO;
import org.siri_hate.main_service.dto.SurveyAnswerRequestDTO;
import org.siri_hate.main_service.dto.SurveySubmissionFullResponseDTO;
import org.siri_hate.main_service.dto.SurveySubmissionRequestDTO;
import org.siri_hate.main_service.model.entity.project.survey.ProjectSurvey;
import org.siri_hate.main_service.model.entity.project.survey.SurveyAnswer;
import org.siri_hate.main_service.model.entity.project.survey.SurveyQuestion;
import org.siri_hate.main_service.model.mapper.ProjectSurveyMapper;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.project.survey.SurveySubmission;
import org.siri_hate.main_service.model.mapper.SurveySubmissionMapper;
import org.siri_hate.main_service.repository.ProjectSurveyRepository;
import org.siri_hate.main_service.repository.SurveySubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectSurveyService {

    private final ProjectSurveyRepository projectSurveyRepository;
    private final ProjectService projectService;
    private final ProjectSurveyMapper projectSurveyMapper;
    private final SurveySubmissionRepository surveySubmissionRepository;
    private final UserService userService;
    private final SurveySubmissionMapper surveySubmissionMapper;

    @Autowired
    public ProjectSurveyService(
            ProjectSurveyRepository projectSurveyRepository,
            ProjectService projectService,
            ProjectSurveyMapper projectSurveyMapper,
            SurveySubmissionRepository surveySubmissionRepository,
            UserService userService,
            SurveySubmissionMapper surveySubmissionMapper
    )
    {
        this.projectSurveyRepository = projectSurveyRepository;
        this.projectService = projectService;
        this.projectSurveyMapper = projectSurveyMapper;
        this.surveySubmissionRepository = surveySubmissionRepository;
        this.userService = userService;
        this.surveySubmissionMapper = surveySubmissionMapper;
    }

    @Transactional
    public ProjectSurveyFullResponseDTO createSurvey(Long projectId, ProjectSurveyRequestDTO request) {
        Project project = projectService.getProjectEntity(projectId);
        if (project.getSurvey() != null) {
            throw new EntityExistsException("Survey already exists for this project");
        }
        ProjectSurvey survey = projectSurveyMapper.toProjectSurvey(request);
        survey.setProject(project);
        project.setSurvey(survey);
        ProjectSurvey savedSurvey = projectSurveyRepository.save(survey);
        projectService.updateProject(project);
        return projectSurveyMapper.toProjectSurveyFullResponseDTO(savedSurvey);
    }

    @Transactional
    public ProjectSurveyFullResponseDTO getSurvey(Long projectId) {
        Project project = projectService.getProjectEntity(projectId);
        ProjectSurvey survey = project.getSurvey();
        if (survey == null) {
            throw new EntityNotFoundException();
        }
        return projectSurveyMapper.toProjectSurveyFullResponseDTO(survey);
    }

    @Transactional
    public void deleteSurvey(Long projectId) {
        Project project = projectService.getProjectEntity(projectId);
        ProjectSurvey survey = project.getSurvey();
        if (survey == null) {
            throw new EntityNotFoundException();
        }
        project.setSurvey(null);
        projectService.updateProject(project);
    }

    @Transactional
    public SurveySubmissionFullResponseDTO submitSurveyAnswers(
            String username,
            Long projectId,
            SurveySubmissionRequestDTO request
    )
    {
        ProjectSurvey survey = Optional
                .ofNullable(projectService.getProjectEntity(projectId).getSurvey())
                .orElseThrow(EntityNotFoundException::new);
        User respondent = userService.findOrCreateUser(username);

        SurveySubmission submission = new SurveySubmission(survey, respondent);
        Integer ratingValue = request.getRating();
        if (ratingValue == null || ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        submission.setRating(ratingValue);

        Map<Long, SurveyQuestion> questionsById = survey.getQuestions()
                .stream()
                .collect(Collectors.toMap(SurveyQuestion::getId, q -> q));

        for (SurveyAnswerRequestDTO answerRequest : request.getAnswers()) {
            SurveyQuestion question = questionsById.get(answerRequest.getQuestionId());
            if (question == null) {
                throw new EntityNotFoundException(
                        "Question with id " + answerRequest.getQuestionId() + " not found in survey"
                );
            }
            SurveyAnswer answer = new SurveyAnswer();
            answer.setSubmission(submission);
            answer.setQuestion(question);
            answer.setContent(answerRequest.getAnswerText());
            submission.getResponses().add(answer);
        }

        SurveySubmission savedSubmission = surveySubmissionRepository.save(submission);
        return surveySubmissionMapper.toSurveySubmissionFullResponseDTO(savedSubmission);
    }

    @Transactional
    public List<SurveySubmissionFullResponseDTO> getAllSurveySubmissions(Long projectId, String sort) {
        Project project = projectService.getProjectEntity(projectId);
        ProjectSurvey survey = project.getSurvey();
        if (survey == null) {
            throw new EntityNotFoundException();
        }
        List<SurveySubmission> submissions = surveySubmissionRepository.findBySurvey(survey);
        return surveySubmissionMapper.toSurveySubmissionFullResponseListDTO(submissions);
    }
}