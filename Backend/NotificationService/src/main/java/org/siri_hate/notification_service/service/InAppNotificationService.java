package org.siri_hate.notification_service.service;

import org.siri_hate.notification_service.kafka.message.ChatMessageNotificationMessage;
import org.siri_hate.notification_service.model.inapp.InAppNotification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InAppNotificationService {

    private static final int MAX_NOTIFICATIONS_PER_USER = 200;

    private final Map<String, Deque<InAppNotification>> notificationsByUser = new ConcurrentHashMap<>();

    public void processChatMessageEvent(ChatMessageNotificationMessage message) {
        if (message == null || message.recipients() == null || message.recipients().isEmpty()) {
            return;
        }

        String preview = message.content();
        if (preview == null || preview.isBlank()) {
            preview = "Новое сообщение";
        }

        for (String recipient : message.recipients()) {
            if (recipient == null || recipient.isBlank()) {
                continue;
            }

            InAppNotification notification = new InAppNotification(
                    UUID.randomUUID().toString(),
                    "CHAT_MESSAGE",
                    "Новое сообщение",
                    message.senderUsername() + ": " + preview,
                    message.chatId(),
                    message.senderUsername(),
                    Instant.now(),
                    false
            );

            Deque<InAppNotification> userNotifications = notificationsByUser.computeIfAbsent(recipient, key -> new ArrayDeque<>());
            synchronized (userNotifications) {
                userNotifications.addFirst(notification);
                while (userNotifications.size() > MAX_NOTIFICATIONS_PER_USER) {
                    userNotifications.removeLast();
                }
            }
        }
    }

    public List<InAppNotification> getNotifications(String username) {
        Deque<InAppNotification> userNotifications = notificationsByUser.computeIfAbsent(username, key -> new ArrayDeque<>());
        synchronized (userNotifications) {
            return new ArrayList<>(userNotifications);
        }
    }

    public int getUnreadCount(String username) {
        Deque<InAppNotification> userNotifications = notificationsByUser.computeIfAbsent(username, key -> new ArrayDeque<>());
        synchronized (userNotifications) {
            return (int) userNotifications.stream().filter(notification -> !notification.isRead()).count();
        }
    }

    public void markAsRead(String username, String notificationId) {
        Deque<InAppNotification> userNotifications = notificationsByUser.computeIfAbsent(username, key -> new ArrayDeque<>());
        synchronized (userNotifications) {
            for (InAppNotification notification : userNotifications) {
                if (notification.getId().equals(notificationId)) {
                    notification.setRead(true);
                    return;
                }
            }
        }
    }
}
