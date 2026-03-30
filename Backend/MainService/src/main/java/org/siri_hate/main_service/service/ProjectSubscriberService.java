package org.siri_hate.main_service.service;

import org.siri_hate.main_service.kafka.message.ProjectUpdateNotification;
import org.siri_hate.main_service.kafka.producer.ProjectEventProducer;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.ProjectSubscriber;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.repository.ProjectSubscriberRepository;
import org.siri_hate.main_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectSubscriberService {

    private final ProjectSubscriberRepository projectSubscriberRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectEventProducer projectEventProducer;

    @Autowired
    public ProjectSubscriberService(
            ProjectSubscriberRepository projectSubscriberRepository,
            @Lazy ProjectService projectService,
            @Lazy UserService userService,
            ProjectEventProducer projectEventProducer) {
        this.projectSubscriberRepository = projectSubscriberRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.projectEventProducer = projectEventProducer;
    }

    @Transactional
    public void subscribeToProject(Long projectId, String username) {
        User user = userService.findOrCreateUser(username);
        if (!projectSubscriberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            Project project = projectService.getProjectEntity(projectId);
            ProjectSubscriber subscriber = new ProjectSubscriber(project, user);
            projectSubscriberRepository.save(subscriber);
        }
    }

    @Transactional
    public void unsubscribeFromProject(Long projectId, String username) {
        User user = userService.findOrCreateUser(username);
        projectSubscriberRepository.deleteByProjectIdAndUserId(projectId, user.getId());
    }

    @Transactional
    public void notifySubscribersAboutUpdate(Long projectId, String projectName) {
        List<ProjectSubscriber> subscribers = projectSubscriberRepository.findByProjectId(projectId);
        LocalDateTime updateDate = LocalDateTime.now();
        String projectLink = System.getenv("PROJECTS_LINK") + projectId;
        for (ProjectSubscriber subscriber : subscribers) {
            ProjectUpdateNotification notification =
                    new ProjectUpdateNotification(
                            projectName, updateDate.toString(), projectLink, subscriber.getUser().getUsername());
            projectEventProducer.sendProjectUpdateNotification(notification);
        }
    }

    @Transactional
    public boolean getSubscribeStatus(Long projectId, String username) {
        User user = userService.findOrCreateUser(username);
        return projectSubscriberRepository.existsByProjectIdAndUserId(projectId, user.getId());
    }
}