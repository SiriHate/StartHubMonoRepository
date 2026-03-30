package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ProjectCategoryApi;
import org.siri_hate.main_service.dto.ProjectCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectCategoryRequestDTO;
import org.siri_hate.main_service.dto.ProjectCategorySummaryResponseDTO;
import org.siri_hate.main_service.service.ProjectCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProjectCategoryController implements ProjectCategoryApi {

    private final ProjectCategoryService projectCategoryService;

    @Autowired
    public ProjectCategoryController(ProjectCategoryService projectCategoryService) {
        this.projectCategoryService = projectCategoryService;
    }

    @Override
    public ResponseEntity<ProjectCategoryFullResponseDTO> createProjectCategory(ProjectCategoryRequestDTO projectCategoryRequestDTO) {
        ProjectCategoryFullResponseDTO response = projectCategoryService.createProjectCategory(projectCategoryRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteProjectCategory(Long id) {
        projectCategoryService.deleteProjectCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ProjectCategorySummaryResponseDTO>> getProjectCategories() {
        List<ProjectCategorySummaryResponseDTO> projectCategories = projectCategoryService.getProjectCategories();
        return new ResponseEntity<>(projectCategories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectCategoryFullResponseDTO> getProjectCategory(Long id) {
        ProjectCategoryFullResponseDTO response = projectCategoryService.getProjectCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectCategoryFullResponseDTO> updateProjectCategory(Long id, ProjectCategoryRequestDTO projectCategoryRequestDTO) {
        ProjectCategoryFullResponseDTO response = projectCategoryService.updateProjectCategory(id, projectCategoryRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}