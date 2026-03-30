package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.main_service.dto.*;
import org.siri_hate.main_service.model.entity.project.ProjectLike;
import org.siri_hate.main_service.model.entity.project.search.SeekingRole;
import org.siri_hate.main_service.model.mapper.ProjectMapper;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.mapper.SearchMapper;
import org.siri_hate.main_service.repository.ProjectRepository;
import org.siri_hate.main_service.repository.specification.ProjectSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectCategoryService projectCategoryService;
    private final UserService userService;
    private final ProjectSubscriberService projectSubscriberService;
    private final FileService fileService;
    private final SearchMapper searchMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper,
                          ProjectCategoryService projectCategoryService,
                          UserService userService,
                          ProjectSubscriberService projectSubscriberService,
                          FileService fileService,
                          SearchMapper searchMapper
    ) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectCategoryService = projectCategoryService;
        this.userService = userService;
        this.projectSubscriberService = projectSubscriberService;
        this.fileService = fileService;
        this.searchMapper = searchMapper;
    }

    @Transactional
    public ProjectFullResponseDTO createProject(String username, ProjectRequestDTO request, MultipartFile logo) {
        Project project = projectMapper.toProject(request);
        project.setOwner(userService.findOrCreateUser(username));
        project.setCategory(projectCategoryService.getProjectCategoryEntityById(request.getCategoryId()));
        String imageKey = fileService.uploadProjectLogo(logo);
        project.setImageKey(imageKey);
        projectRepository.save(project);
        return projectMapper.toProjectFullResponse(project);
    }

    public ProjectPageResponseDTO getProjects(String category, SeekingRoleDTO seekingRoleDTO, String domain, String query, int page, int size, boolean isModerationPassed) {
        Specification<Project> spec = Specification.where(ProjectSpecification.projectNameContainsIgnoreCase(query));
        spec = spec.and(ProjectSpecification.hasCategory(category));
        spec = spec.and(ProjectSpecification.moderationPassed(isModerationPassed));
        if (seekingRoleDTO != null) {
            SeekingRole seekingRole = searchMapper.toSeekingRole(seekingRoleDTO);
            switch (seekingRole) {
                case EMPLOYEE -> spec = spec.and(ProjectSpecification.hasEmployeeSearch(domain));
                case PARTNER -> spec = spec.and(ProjectSpecification.hasFounderSearch(domain));
                case INVESTOR -> spec = spec.and(ProjectSpecification.hasInvestorSearch());
                case MENTOR -> spec = spec.and(ProjectSpecification.hasMentorSearch(domain));
            }
        }
        Page<Project> projects = projectRepository.findAll(spec, PageRequest.of(page, size));
        return projectMapper.toProjectPageResponse(projects);
    }

    public ProjectPageResponseDTO getMyProjects(
            String username,
            String query,
            MemberProjectRoleDTO role,
            int page,
            int size
    )
    {
        Specification<Project> spec = Specification.where(ProjectSpecification.projectNameStartsWith(query));

        if (role == null) {
            spec = spec.and(
                    ProjectSpecification.hasOwnerUsername(username)
                            .or(ProjectSpecification.hasMemberUsername(username))
            );
        } else {
            switch (role) {
                case OWNER -> spec = spec.and(ProjectSpecification.hasOwnerUsername(username));
                case MEMBER -> spec = spec.and(ProjectSpecification.hasMemberUsername(username));
            }
        }

        Page<Project> projects = projectRepository.findAll(spec, PageRequest.of(page, size));
        return projectMapper.toProjectPageResponse(projects);
    }

    public ProjectPageResponseDTO searchProjectsHiring(String specialization, String query, int page, int size) {
        Specification<Project> spec = Specification.where(ProjectSpecification.projectNameStartsWith(query))
                .and(ProjectSpecification.moderationPassed(true))
                .and(ProjectSpecification.hasEmployeeSearch(specialization));
        Page<Project> projects = projectRepository.findAll(spec, PageRequest.of(page, size));
        return projectMapper.toProjectPageResponse(projects);
    }

    public ProjectPageResponseDTO searchProjectsLookingFor(String type, String domain, String query, int page, int size) {
        Specification<Project> spec = Specification.where(ProjectSpecification.projectNameStartsWith(query))
                .and(ProjectSpecification.moderationPassed(true));
        switch (type) {
            case "founder" -> spec = spec.and(ProjectSpecification.hasFounderSearch(domain));
            case "investor" -> spec = spec.and(ProjectSpecification.hasInvestorSearch());
            case "mentor" -> spec = spec.and(ProjectSpecification.hasMentorSearch(domain));
            default -> throw new IllegalArgumentException("Unknown search type: " + type);
        }
        Page<Project> projects = projectRepository.findAll(spec, PageRequest.of(page, size));
        return projectMapper.toProjectPageResponse(projects);
    }

    @Transactional
    public ProjectFullResponseDTO getProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return projectMapper.toProjectFullResponse(project);
    }

    public Project getProjectEntity(Long id) {
        return projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public ProjectFullResponseDTO updateProject(Long id, ProjectRequestDTO request, MultipartFile logo) {
        Project project = projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        projectMapper.projectUpdate(request, project);

        project.setCategory(projectCategoryService.getProjectCategoryEntityById(request.getCategoryId()));

        if (logo != null && !logo.isEmpty()) {
            String imageKey = fileService.uploadProjectLogo(logo);
            project.setImageKey(imageKey);
        }

        projectRepository.save(project);
        projectSubscriberService.notifySubscribersAboutUpdate(id, project.getName());
        return projectMapper.toProjectFullResponse(project);
    }

    @Transactional
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public boolean toggleProjectLike(String username, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        User user = userService.findOrCreateUser(username);
        boolean alreadyLiked = project.getProjectLikes()
                .stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));
        if (alreadyLiked) {
            project.getProjectLikes().removeIf(like -> like.getUser().getId().equals(user.getId()));
            projectRepository.save(project);
            return false;
        } else {
            project.addLike(new ProjectLike(user, project));
            projectRepository.save(project);
            return true;
        }
    }

    public long getProjectLikesCount(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        return project.getProjectLikes().size();
    }

    @Transactional
    public void updateProjectModerationStatus(Long projectId, Boolean moderationPassed) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        project.setModerationPassed(moderationPassed);
        projectRepository.save(project);
    }
}
