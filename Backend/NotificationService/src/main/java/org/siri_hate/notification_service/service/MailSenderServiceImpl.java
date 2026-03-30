package org.siri_hate.notification_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.siri_hate.notification_service.kafka.message.ConfirmationMessage;
import org.siri_hate.notification_service.kafka.message.NotificationMessage;
import org.siri_hate.notification_service.kafka.message.ProjectUpdateMessage;
import org.siri_hate.notification_service.kafka.message.enums.ConfirmationMessageType;
import org.siri_hate.notification_service.kafka.message.enums.NotificationMessageType;
import org.siri_hate.notification_service.model.mails.MailTemplate;
import org.siri_hate.notification_service.model.mails.confirmation.RecoveryPasswordMail;
import org.siri_hate.notification_service.model.mails.confirmation.RegistrationConfirmationMail;
import org.siri_hate.notification_service.model.mails.notification.DeletedAccountMail;
import org.siri_hate.notification_service.model.mails.notification.PasswordChangeMail;
import org.siri_hate.notification_service.model.mails.notification.ProjectUpdateMail;
import org.siri_hate.notification_service.model.mails.notification.SuccessfulRegistrationMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private static final String LOGO_CLASSPATH_LOCATION = "image/logo.png";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendConfirmationMail(ConfirmationMessage confirmationMessage) throws MessagingException {
        System.out.println("DBG: sendConfirmationMail called messageType=" + confirmationMessage.messageType());
        MailTemplate mailTemplate = formConfirmationMail(confirmationMessage);
        sendEmail(mailTemplate);
    }

    public void sendNotificationMail(NotificationMessage notificationMessage) throws MessagingException {
        MailTemplate mailTemplate = formNotificationMail(notificationMessage);
        sendEmail(mailTemplate);
    }

    public void sendProjectUpdateMail(ProjectUpdateMessage projectUpdateMessage) throws MessagingException {

        LocalDateTime dateTime = LocalDateTime.parse(projectUpdateMessage.updateDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = dateTime.format(formatter);

        MailTemplate mailTemplate = new ProjectUpdateMail(
                projectUpdateMessage.userEmailAddress(),
                projectUpdateMessage.userRealName(),
                projectUpdateMessage.projectName(),
                projectUpdateMessage.projectLink(),
                formattedDate
        );

        sendEmail(mailTemplate);
    }

    private void sendEmail(MailTemplate mailTemplate) throws MessagingException {

        Context context = new Context();
        mailTemplate.prepareMessage(context, templateEngine);

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setFrom(fromEmailAddress);
        helper.setTo(mailTemplate.getToEmailAddress());
        helper.setSubject(mailTemplate.getSubject());
        helper.setText(mailTemplate.getMessage(), true);
        Resource logoResource = new ClassPathResource(LOGO_CLASSPATH_LOCATION);
        helper.addInline("logo", logoResource);

        mailSender.send(mail);
    }

    private MailTemplate formNotificationMail(NotificationMessage notificationMessage) {

        String email = notificationMessage.userEmail();
        String fullName = notificationMessage.userFullName();
        NotificationMessageType messageType = notificationMessage.messageType();

        return switch (messageType) {
            case SUCCESSFUL_REGISTRATION_NOTIFICATION -> new SuccessfulRegistrationMail(email, fullName);
            case DELETED_ACCOUNT_NOTIFICATION -> new DeletedAccountMail(email, fullName);
            case CHANGED_PASSWORD_NOTIFICATION -> new PasswordChangeMail(email, fullName);
        };
    }

    private MailTemplate formConfirmationMail(ConfirmationMessage confirmationMessage) {

        String email = confirmationMessage.userEmail();
        String fullName = confirmationMessage.userFullName();
        String confirmationToken = confirmationMessage.userConfirmationToken();
        ConfirmationMessageType messageType = confirmationMessage.messageType();

        return switch (messageType) {
            case REGISTRATION_CONFIRMATION -> new RegistrationConfirmationMail(email, fullName, confirmationToken);
            case CHANGE_PASSWORD_CONFIRMATION -> new RecoveryPasswordMail(email, fullName, confirmationToken);
        };
    }
}
