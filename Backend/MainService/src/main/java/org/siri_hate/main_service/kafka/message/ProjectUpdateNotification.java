package org.siri_hate.main_service.kafka.message;

public record ProjectUpdateNotification(String projectName, String updateDate, String projectLink, String username) {
}