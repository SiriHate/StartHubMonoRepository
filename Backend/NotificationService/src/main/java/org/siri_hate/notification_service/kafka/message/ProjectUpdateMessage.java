package org.siri_hate.notification_service.kafka.message;

public record ProjectUpdateMessage(String projectName, String updateDate, String projectLink, String username,
                                   String userRealName, String userEmailAddress) {
}