package org.siri_hate.main_service.controller;

import org.jspecify.annotations.Nullable;
import org.siri_hate.main_service.api.ProjectApi;
import org.siri_hate.main_service.dto.*;
import org.siri_hate.main_service.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProjectController implements ProjectApi {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public ResponseEntity<ProjectFullResponseDTO> createProject(String xUserName, ProjectRequestDTO project, MultipartFile logo) {
        var response = projectService.createProject(xUserName, project, logo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteProject(Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ProjectPageResponseDTO> getMyProjects(Integer page, Integer size, String xUserName, @Nullable MemberProjectRoleDTO role, @Nullable String query) {
        var response = projectService.getMyProjects(xUserName, query, role, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectFullResponseDTO> getProject(Long id) {
        var response = projectService.getProject(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> getProjectLikesCount(Long id) {
        var response = projectService.getProjectLikesCount(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectPageResponseDTO> getProjects(Boolean moderationPassed, Integer page, Integer size, @Nullable SeekingRoleDTO seekingRole, @Nullable String category, @Nullable String domain, @Nullable String query) {
        var response = projectService.getProjects(category, seekingRole, domain, query, page, size, moderationPassed);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> toggleProjectLike(Long id, String xUserName) {
        var response = projectService.toggleProjectLike(xUserName, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectFullResponseDTO> updateProject(Long id, ProjectRequestDTO project, MultipartFile logo) {
        var response = projectService.updateProject(id, project, logo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateProjectModerationPassed(Long id, Boolean body) {
        projectService.updateProjectModerationStatus(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectPageResponseDTO> searchProjectsHiring(Integer page, Integer size, @Nullable String specialization, @Nullable String query) {
        var response = projectService.searchProjectsHiring(specialization, query, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectPageResponseDTO> searchProjectsLookingFor(String type, Integer page, Integer size, @Nullable String domain, @Nullable String query) {
        var response = projectService.searchProjectsLookingFor(type, domain, query, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
