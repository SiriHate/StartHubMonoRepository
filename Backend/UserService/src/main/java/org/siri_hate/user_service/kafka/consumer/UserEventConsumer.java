package org.siri_hate.user_service.kafka.consumer;

import com.google.gson.Gson;
import org.siri_hate.user_service.kafka.message.UserDeletionMessage;
import org.siri_hate.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserEventConsumer {

    private final UserService userService;
    private final Gson gson;

    @Autowired
    public UserEventConsumer(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @KafkaListener(topics = "${user.deletion.topic.m2s.name}", groupId = "${spring.application.name}")
    @Transactional
    public void consumeUserDeletionMessage(String message) {
        UserDeletionMessage userDeletionMessage = gson.fromJson(message, UserDeletionMessage.class);
        String username = userDeletionMessage.username();
        userService.deleteUserByUsername(username);
    }
}