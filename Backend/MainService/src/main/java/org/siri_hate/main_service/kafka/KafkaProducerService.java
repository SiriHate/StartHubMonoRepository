package org.siri_hate.main_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public <T> void send(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }

    public <T> void send(String topic, T message, String key) {
        kafkaTemplate.send(topic, key, message);
    }
}
