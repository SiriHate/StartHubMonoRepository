package org.siri_hate.notification_service.kafka.message;

import java.util.List;

public record ChatMessageNotificationMessage(
        Long chatId,
        String senderUsername,
        String content,
        String sendAt,
        List<String> recipients
) {
}
