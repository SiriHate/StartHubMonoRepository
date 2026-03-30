package org.siri_hate.main_service.kafka.producer;

import org.siri_hate.main_service.kafka.KafkaProducerService;
import org.siri_hate.main_service.kafka.message.UserDeletionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    @Value("${user.deletion.topic.m2s.name}")
    private String userDeletionTopic;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserEventProducer(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendUserDeletionMessage(String username) {
        UserDeletionMessage message = new UserDeletionMessage(username);
        kafkaProducerService.send(userDeletionTopic, message);
    }
}