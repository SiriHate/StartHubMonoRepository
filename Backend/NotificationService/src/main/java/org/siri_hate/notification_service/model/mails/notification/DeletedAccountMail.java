package org.siri_hate.notification_service.model.mails.notification;

import org.siri_hate.notification_service.model.enums.EmailSubject;
import org.siri_hate.notification_service.model.mails.MailTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Objects;

public class DeletedAccountMail extends MailTemplate {

    private final String fullName;

    public DeletedAccountMail(String toEmailAddress, String fullName) {
        this.toEmailAddress = toEmailAddress;
        this.subject = EmailSubject.DELETED_ACCOUNT_NOTIFICATION.getSubject();
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public void prepareMessage(Context context, TemplateEngine templateEngine) {
        context.setVariable("fullName", fullName);
        this.message = templateEngine.process("deleted_account_mail_template", context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeletedAccountMail that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName);
    }

    @Override
    public String toString() {
        return "DeletedAccountMail{" +
                "fullName='" + fullName + '\'' +
                ", toEmailAddress='" + toEmailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}