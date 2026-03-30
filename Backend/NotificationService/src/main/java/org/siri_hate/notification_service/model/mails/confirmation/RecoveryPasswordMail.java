package org.siri_hate.notification_service.model.mails.confirmation;

import org.siri_hate.notification_service.model.enums.EmailSubject;
import org.siri_hate.notification_service.model.mails.MailTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;

public class RecoveryPasswordMail extends MailTemplate {

    private final String confirmationBaseUrl = System.getenv("FRONTEND_BASE_URL");
    private final String confirmPasswordRecoveryUrl = System.getenv("CHANGE_PASSWORD_URL");
    private final String fullName;
    private final String confirmationUrl;

    public RecoveryPasswordMail(String toEmailAddress, String fullName, String token) {
        this.toEmailAddress = toEmailAddress;
        this.fullName = fullName;
        this.subject = EmailSubject.CHANGE_PASSWORD_CONFIRMATION.getSubject();
        this.confirmationUrl = "%s/%s/%s".formatted(confirmationBaseUrl, confirmPasswordRecoveryUrl, token);
    }

    public String getConfirmationBaseUrl() {
        return confirmationBaseUrl;
    }

    public String getConfirmPasswordRecoveryUrl() {
        return confirmPasswordRecoveryUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getConfirmationUrl() {
        return confirmationUrl;
    }

    @Override
    public void prepareMessage(Context context, TemplateEngine templateEngine) {
        context.setVariable("fullName", fullName);
        context.setVariable("confirmationUrl", this.confirmationUrl);
        this.message = templateEngine.process("recovery_password_mail_template", context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecoveryPasswordMail that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(confirmationBaseUrl, that.confirmationBaseUrl) &&
                Objects.equals(confirmPasswordRecoveryUrl, that.confirmPasswordRecoveryUrl) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(confirmationUrl, that.confirmationUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), confirmationBaseUrl, confirmPasswordRecoveryUrl, fullName, confirmationUrl);
    }

    @Override
    public String toString() {
        return "RecoveryPasswordMail{" +
                "confirmationBaseUrl='" + confirmationBaseUrl + '\'' +
                ", confirmPasswordRecoveryUrl='" + confirmPasswordRecoveryUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", confirmationUrl='" + confirmationUrl + '\'' +
                ", toEmailAddress='" + toEmailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
