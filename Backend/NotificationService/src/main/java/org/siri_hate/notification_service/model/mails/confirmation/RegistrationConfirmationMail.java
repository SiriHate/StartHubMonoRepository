package org.siri_hate.notification_service.model.mails.confirmation;

import org.siri_hate.notification_service.model.enums.EmailSubject;
import org.siri_hate.notification_service.model.mails.MailTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;

public class RegistrationConfirmationMail extends MailTemplate {

    private final String confirmationUrl;
    private final String fullName;

    public RegistrationConfirmationMail(String toEmailAddress, String fullName, String token) {
        this.toEmailAddress = toEmailAddress;
        this.fullName = fullName;
        this.subject = EmailSubject.REGISTRATION_CONFIRMATION.getSubject();
        String confirmationBaseUrl = System.getenv("FRONTEND_BASE_URL");
        String confirmRegistrationUrl = System.getenv("CONFIRM_REGISTRATION_URL");
        this.confirmationUrl = "%s/%s/%s".formatted(confirmationBaseUrl, confirmRegistrationUrl, token);
    }

    public String getConfirmationUrl() {
        return confirmationUrl;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public void prepareMessage(Context context, TemplateEngine templateEngine) {
        context.setVariable("fullName", fullName);
        context.setVariable("confirmationUrl", this.confirmationUrl);
        this.message = templateEngine.process("registration_confirmation_mail_template", context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationConfirmationMail that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(confirmationUrl, that.confirmationUrl) &&
                Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), confirmationUrl, fullName);
    }

    @Override
    public String toString() {
        return "RegistrationConfirmationMail{" +
                "confirmationUrl='" + confirmationUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", toEmailAddress='" + toEmailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
