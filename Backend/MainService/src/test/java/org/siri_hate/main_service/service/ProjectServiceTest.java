package org.siri_hate.main_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.ProjectLike;
import org.siri_hate.main_service.model.mapper.ProjectMapper;
import org.siri_hate.main_service.model.mapper.SearchMapper;
import org.siri_hate.main_service.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private ProjectCategoryService projectCategoryService;
    @Mock
    private UserService userService;
    @Mock
    private ProjectSubscriberService projectSubscriberService;
    @Mock
    private FileService fileService;
    @Mock
    private SearchMapper searchMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void searchProjectsLookingFor_shouldThrowForUnknownType() {
        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.searchProjectsLookingFor("unknown", null, null, 0, 10)
        );
        verify(projectRepository, never()).findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void toggleProjectLike_shouldReturnFalseWhenAlreadyLiked() {
        User user = new User("user");
        user.setId(1L);

        Project project = new Project();
        project.setId(100L);
        project.setProjectLikes(new ArrayList<>());
        project.getProjectLikes().add(new ProjectLike(user, project));

        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));
        when(userService.findOrCreateUser("user")).thenReturn(user);

        boolean result = projectService.toggleProjectLike("user", 100L);

        assertFalse(result);
        assertTrue(project.getProjectLikes().isEmpty());
        verify(projectRepository).save(project);
    }
}
