package org.siri_hate.main_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.InvestorSearchFullResponseDTO;
import org.siri_hate.main_service.dto.InvestorSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.search.InvestorSearch;
import org.siri_hate.main_service.model.mapper.InvestorSearchMapper;
import org.siri_hate.main_service.repository.InvestorSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvestorSearchService {

    private final InvestorSearchRepository repository;
    private final InvestorSearchMapper mapper;
    private final ProjectService projectService;

    @Autowired
    public InvestorSearchService(
            InvestorSearchRepository repository,
            InvestorSearchMapper mapper,
            ProjectService projectService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.projectService = projectService;
    }

    @Transactional
    public InvestorSearchFullResponseDTO create(Long projectId, InvestorSearchRequestDTO request) {
        if (repository.existsByProjectId(projectId)) {
            throw new EntityExistsException("Investor search already exists for this project");
        }
        Project project = projectService.getProjectEntity(projectId);
        InvestorSearch entity = mapper.toEntity(request);
        entity.setProject(project);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    public List<InvestorSearchFullResponseDTO> getByProjectId(Long projectId) {
        List<InvestorSearch> entities = repository.findByProjectId(projectId);
        return mapper.toFullResponseList(entities);
    }

    @Transactional
    public InvestorSearchFullResponseDTO update(Long id, InvestorSearchRequestDTO request) {
        InvestorSearch entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        mapper.updateEntity(request, entity);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
