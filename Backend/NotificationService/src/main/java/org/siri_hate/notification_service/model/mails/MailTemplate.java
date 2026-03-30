package org.siri_hate.notification_service.model.mails;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;

public abstract class MailTemplate {

    protected String toEmailAddress;
    protected String subject;
    protected String message;

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailTemplate that)) return false;
        return Objects.equals(toEmailAddress, that.toEmailAddress) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toEmailAddress, subject, message);
    }

    @Override
    public String toString() {
        return "MailTemplate{" +
                "toEmailAddress='" + toEmailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public abstract void prepareMessage(Context context, TemplateEngine templateEngine);
}
