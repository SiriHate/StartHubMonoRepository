package org.siri_hate.notification_service.kafka.message;

import org.siri_hate.notification_service.kafka.message.enums.ConfirmationMessageType;

public record ConfirmationMessage(ConfirmationMessageType messageType, String userFullName, String userEmail,
                                  String userConfirmationToken) {

}
