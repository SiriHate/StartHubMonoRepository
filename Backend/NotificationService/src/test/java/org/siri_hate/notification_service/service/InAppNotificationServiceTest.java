package org.siri_hate.notification_service.service;

import org.junit.jupiter.api.Test;
import org.siri_hate.notification_service.kafka.message.ChatMessageNotificationMessage;
import org.siri_hate.notification_service.model.inapp.InAppNotification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InAppNotificationServiceTest {

    private final InAppNotificationService inAppNotificationService = new InAppNotificationService();

    @Test
    void processChatMessageEvent_shouldCreateNotificationsForValidRecipients() {
        ChatMessageNotificationMessage message = new ChatMessageNotificationMessage(
                10L,
                "alice",
                "hello",
                null,
                List.of("bob", " ", "carol")
        );

        inAppNotificationService.processChatMessageEvent(message);

        List<InAppNotification> bobNotifications = inAppNotificationService.getNotifications("bob");
        List<InAppNotification> carolNotifications = inAppNotificationService.getNotifications("carol");

        assertEquals(1, bobNotifications.size());
        assertEquals(1, carolNotifications.size());
        assertTrue(bobNotifications.get(0).getBody().contains("alice: hello"));
    }

    @Test
    void markAsRead_shouldDecreaseUnreadCount() {
        ChatMessageNotificationMessage message = new ChatMessageNotificationMessage(
                11L,
                "alice",
                "",
                null,
                List.of("bob")
        );

        inAppNotificationService.processChatMessageEvent(message);
        List<InAppNotification> notifications = inAppNotificationService.getNotifications("bob");
        InAppNotification first = notifications.get(0);

        assertEquals(1, inAppNotificationService.getUnreadCount("bob"));
        assertFalse(first.isRead());
        assertTrue(first.getBody().contains("Новое сообщение"));

        inAppNotificationService.markAsRead("bob", first.getId());

        assertEquals(0, inAppNotificationService.getUnreadCount("bob"));
        assertTrue(inAppNotificationService.getNotifications("bob").get(0).isRead());
    }
}
