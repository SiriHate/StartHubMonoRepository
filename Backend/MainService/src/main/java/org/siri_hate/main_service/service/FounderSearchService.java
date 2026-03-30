package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.FounderSearchFullResponseDTO;
import org.siri_hate.main_service.dto.FounderSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.search.PartnerSearch;
import org.siri_hate.main_service.model.mapper.FounderSearchMapper;
import org.siri_hate.main_service.repository.FounderSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FounderSearchService {

    private final FounderSearchRepository repository;
    private final FounderSearchMapper mapper;
    private final ProjectService projectService;

    @Autowired
    public FounderSearchService(
            FounderSearchRepository repository,
            FounderSearchMapper mapper,
            ProjectService projectService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.projectService = projectService;
    }

    @Transactional
    public FounderSearchFullResponseDTO create(Long projectId, FounderSearchRequestDTO request) {
        Project project = projectService.getProjectEntity(projectId);
        PartnerSearch entity = mapper.toEntity(request);
        entity.setProject(project);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    public List<FounderSearchFullResponseDTO> getByProjectId(Long projectId) {
        List<PartnerSearch> entities = repository.findByProjectId(projectId);
        return mapper.toFullResponseList(entities);
    }

    @Transactional
    public FounderSearchFullResponseDTO update(Long id, FounderSearchRequestDTO request) {
        PartnerSearch entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        mapper.updateEntity(request, entity);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
