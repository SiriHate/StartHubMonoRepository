package org.siri_hate.user_service.kafka.producer;

import org.siri_hate.user_service.kafka.KafkaProducerService;
import org.siri_hate.user_service.kafka.message.ProjectUpdateNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProjectEventProducer {

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public ProjectEventProducer(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Value("${project.update.notification.topic.producer}")
    private String projectUpdateNotificationTopic;

    public void sendProjectUpdateNotification(ProjectUpdateNotification notification) {
        kafkaProducerService.send(projectUpdateNotificationTopic, notification);
    }
}