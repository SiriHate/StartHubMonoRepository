package org.siri_hate.notification_service.controller;

import org.siri_hate.notification_service.kafka.message.ChatMessageNotificationMessage;
import org.siri_hate.notification_service.model.inapp.InAppNotification;
import org.siri_hate.notification_service.service.InAppNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification_service")
public class InAppNotificationController {

    private final InAppNotificationService inAppNotificationService;

    public InAppNotificationController(InAppNotificationService inAppNotificationService) {
        this.inAppNotificationService = inAppNotificationService;
    }

    @PostMapping("/internal/chat-message")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingestChatMessage(@RequestBody ChatMessageNotificationMessage message) {
        inAppNotificationService.processChatMessageEvent(message);
    }

    @GetMapping("/in-app")
    public List<InAppNotification> getNotifications(@RequestHeader("X-User-Name") String username) {
        return inAppNotificationService.getNotifications(requireUsername(username));
    }

    @GetMapping("/in-app/unread-count")
    public Map<String, Integer> getUnreadCount(@RequestHeader("X-User-Name") String username) {
        return Map.of("count", inAppNotificationService.getUnreadCount(requireUsername(username)));
    }

    @PatchMapping("/in-app/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@RequestHeader("X-User-Name") String username, @PathVariable("id") String id) {
        inAppNotificationService.markAsRead(requireUsername(username), id);
    }

    private String requireUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return username;
    }
}
