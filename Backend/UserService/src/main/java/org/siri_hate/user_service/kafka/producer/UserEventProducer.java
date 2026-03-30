package org.siri_hate.user_service.kafka.producer;

import org.siri_hate.user_service.kafka.KafkaProducerService;
import org.siri_hate.user_service.kafka.message.ConfirmationMessage;
import org.siri_hate.user_service.kafka.message.NotificationMessage;
import org.siri_hate.user_service.kafka.message.enums.ConfirmationMessageType;
import org.siri_hate.user_service.kafka.message.enums.NotificationMessageType;
import org.siri_hate.user_service.kafka.message.UserDeletionMessage;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    @Value("${notification.topic.name}")
    private String notificationTopicName;

    @Value("${confirmation.topic.name}")
    private String confirmationTopicName;

    @Value("${user.deletion.topic.s2m.name}")
    private String userDeletionTopicName;

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserEventProducer(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendSuccessfulRegistrationNotification(User user) {
        if (user instanceof Member member) {
            var messageType = NotificationMessageType.SUCCESSFUL_REGISTRATION_NOTIFICATION;
            NotificationMessage notificationMessage = new NotificationMessage(
                    messageType,
                    member.getName(),
                    member.getEmail()
            );
            kafkaProducerService.send(notificationTopicName, notificationMessage);
        }
    }

    public void sendDeletedAccountNotification(String name, String email) {
        var messageType = NotificationMessageType.DELETED_ACCOUNT_NOTIFICATION;
        NotificationMessage notificationMessage = new NotificationMessage(messageType, name, email);
        kafkaProducerService.send(notificationTopicName, notificationMessage);
    }

    public void sendChangedPasswordNotification(String name, String email) {
        var messageType = NotificationMessageType.CHANGED_PASSWORD_NOTIFICATION;
        NotificationMessage notificationMessage = new NotificationMessage(messageType, name, email);
        kafkaProducerService.send(notificationTopicName, notificationMessage);
    }

    public void sendUserDeletionMessage(String username) {
        var message = new UserDeletionMessage(username);
        kafkaProducerService.send(userDeletionTopicName, message);
    }

    public void sendConfirmationToken(
            String memberName,
            String memberEmail,
            String token,
            ConfirmationMessageType messageType
    ) {
        ConfirmationMessage confirmationMessage = new ConfirmationMessage(
                messageType,
                memberName,
                memberEmail,
                token
        );
        kafkaProducerService.send(confirmationTopicName, confirmationMessage);
    }
}