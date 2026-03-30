package org.siri_hate.main_service.kafka.producer;

import org.siri_hate.main_service.kafka.KafkaProducerService;
import org.siri_hate.main_service.kafka.message.ProjectUpdateNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProjectEventProducer {

    @Value("${project.update.notification.topic}")
    private String projectUpdateTopic;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public ProjectEventProducer(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendProjectUpdateNotification(ProjectUpdateNotification notification) {
        kafkaProducerService.send(projectUpdateTopic, notification);
    }
}