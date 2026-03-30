package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.MentorSearchFullResponseDTO;
import org.siri_hate.main_service.dto.MentorSearchRequestDTO;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.search.MentorSearch;
import org.siri_hate.main_service.model.mapper.MentorSearchMapper;
import org.siri_hate.main_service.repository.MentorSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MentorSearchService {

    private final MentorSearchRepository repository;
    private final MentorSearchMapper mapper;
    private final ProjectService projectService;

    @Autowired
    public MentorSearchService(
            MentorSearchRepository repository,
            MentorSearchMapper mapper,
            ProjectService projectService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.projectService = projectService;
    }

    @Transactional
    public MentorSearchFullResponseDTO create(Long projectId, MentorSearchRequestDTO request) {
        Project project = projectService.getProjectEntity(projectId);
        MentorSearch entity = mapper.toEntity(request);
        entity.setProject(project);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    public List<MentorSearchFullResponseDTO> getByProjectId(Long projectId) {
        List<MentorSearch> entities = repository.findByProjectId(projectId);
        return mapper.toFullResponseList(entities);
    }

    @Transactional
    public MentorSearchFullResponseDTO update(Long id, MentorSearchRequestDTO request) {
        MentorSearch entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        mapper.updateEntity(request, entity);
        repository.save(entity);
        return mapper.toFullResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
