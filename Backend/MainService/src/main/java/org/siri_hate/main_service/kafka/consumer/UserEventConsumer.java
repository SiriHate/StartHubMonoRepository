package org.siri_hate.main_service.kafka.consumer;

import com.google.gson.Gson;
import org.siri_hate.main_service.kafka.message.UserDeletionMessage;
import org.siri_hate.main_service.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    private final UserService userService;
    private final Gson gson;

    public UserEventConsumer(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @KafkaListener(
            topics = "${user.deletion.topic.s2m.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeUserDeletionMessage(String message) {
        UserDeletionMessage userDeletionMessage = gson.fromJson(message, UserDeletionMessage.class);
        try {
            userService.deleteUser(userDeletionMessage.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}