package org.siri_hate.notification_service.service;

import jakarta.mail.MessagingException;
import org.siri_hate.notification_service.kafka.message.ConfirmationMessage;
import org.siri_hate.notification_service.kafka.message.NotificationMessage;
import org.siri_hate.notification_service.kafka.message.ProjectUpdateMessage;

public interface MailSenderService {
    void sendConfirmationMail(ConfirmationMessage confirmationMessage) throws MessagingException;
    void sendNotificationMail(NotificationMessage notificationMessage) throws MessagingException;
    void sendProjectUpdateMail(ProjectUpdateMessage projectUpdateMessage) throws MessagingException;
}