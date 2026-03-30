package org.siri_hate.user_service.kafka.message;

import org.siri_hate.user_service.kafka.message.enums.NotificationMessageType;

public record NotificationMessage(
        NotificationMessageType messageType,
        String userFullName,
        String userEmail
) {
}