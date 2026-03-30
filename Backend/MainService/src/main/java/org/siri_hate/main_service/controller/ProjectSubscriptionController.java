package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ProjectSubscriptionApi;
import org.siri_hate.main_service.service.ProjectSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectSubscriptionController implements ProjectSubscriptionApi {

    private final ProjectSubscriberService projectSubscriberService;

    @Autowired
    public ProjectSubscriptionController(ProjectSubscriberService projectSubscriberService) {
        this.projectSubscriberService = projectSubscriberService;
    }

    @Override
    public ResponseEntity<Boolean> isUserSubscribed(Long projectId, String xUserName) {
        var response = projectSubscriberService.getSubscribeStatus(projectId, xUserName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> subscribeToProject(Long projectId, String xUserName) {
        projectSubscriberService.subscribeToProject(projectId, xUserName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> unsubscribeFromProject(Long projectId, String xUserName) {
        projectSubscriberService.unsubscribeFromProject(projectId,xUserName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}