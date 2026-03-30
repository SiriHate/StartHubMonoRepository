package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.EmployeeSearchFullResponseDTO;
import org.siri_hate.main_service.dto.EmployeeSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.search.EmployeeSearch;
import org.siri_hate.main_service.model.mapper.EmployeeSearchMapper;
import org.siri_hate.main_service.repository.EmployeeSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeSearchService {

    private final EmployeeSearchRepository repository;
    private final EmployeeSearchMapper mapper;
    private final ProjectService projectService;

    @Autowired
    public EmployeeSearchService(
            EmployeeSearchRepository repository,
            EmployeeSearchMapper mapper,
            ProjectService projectService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.projectService = projectService;
    }

    @Transactional
    public EmployeeSearchFullResponseDTO create(Long projectId, EmployeeSearchRequestDTO request) {
        Project project = projectService.getProjectEntity(projectId);
        EmployeeSearch entity = mapper.toEntity(request);
        entity.setProject(project);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    public List<EmployeeSearchFullResponseDTO> getByProjectId(Long projectId) {
        List<EmployeeSearch> entities = repository.findByProjectId(projectId);
        return mapper.toFullResponseList(entities);
    }

    @Transactional
    public EmployeeSearchFullResponseDTO update(Long id, EmployeeSearchRequestDTO request) {
        EmployeeSearch entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        mapper.updateEntity(request, entity);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
