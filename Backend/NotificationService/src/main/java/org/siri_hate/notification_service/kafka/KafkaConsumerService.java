package org.siri_hate.notification_service.kafka;

import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import org.siri_hate.notification_service.kafka.message.ChatMessageNotificationMessage;
import org.siri_hate.notification_service.kafka.message.ConfirmationMessage;
import org.siri_hate.notification_service.kafka.message.NotificationMessage;
import org.siri_hate.notification_service.kafka.message.ProjectUpdateMessage;
import org.siri_hate.notification_service.service.InAppNotificationService;
import org.siri_hate.notification_service.service.MailSenderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final MailSenderServiceImpl mailSenderService;
    private final InAppNotificationService inAppNotificationService;
    private final Gson gson;

    @Autowired
    public KafkaConsumerService(MailSenderServiceImpl mailSenderService, InAppNotificationService inAppNotificationService, Gson gson) {
        this.mailSenderService = mailSenderService;
        this.inAppNotificationService = inAppNotificationService;
        this.gson = gson;
    }

    @KafkaListener(topics = "notification_topic", groupId = "consumers")
    public void NotificationTopicListener(String message) throws MessagingException {
        NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
        mailSenderService.sendNotificationMail(notificationMessage);
    }

    @KafkaListener(topics = "confirmation_topic", groupId = "consumers")
    public void ConfirmationTopicListener(String message) throws MessagingException {
        ConfirmationMessage confirmationMessage = gson.fromJson(message, ConfirmationMessage.class);
        mailSenderService.sendConfirmationMail(confirmationMessage);
    }

    @KafkaListener(topics = "project_update_notification_s2n", groupId = "consumers")
    public void ProjectUpdateTopicListener(String message) throws MessagingException {
        ProjectUpdateMessage projectUpdateMessage = gson.fromJson(message, ProjectUpdateMessage.class);
        mailSenderService.sendProjectUpdateMail(projectUpdateMessage);
    }

    @KafkaListener(topics = "chat_message_notification_topic", groupId = "consumers")
    public void chatMessageTopicListener(String message) {
        ChatMessageNotificationMessage notificationMessage = gson.fromJson(message, ChatMessageNotificationMessage.class);
        inAppNotificationService.processChatMessageEvent(notificationMessage);
    }
}