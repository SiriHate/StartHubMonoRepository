package org.siri_hate.notification_service.model.mails.notification;

import org.siri_hate.notification_service.model.mails.MailTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;

public class ProjectUpdateMail extends MailTemplate {

    private final String userFullName;
    private final String projectName;
    private final String projectLink;
    private final String updateDate;

    public ProjectUpdateMail(String userEmail, String userFullName, String projectName, String projectLink, String updateDate) {
        this.toEmailAddress = userEmail;
        this.userFullName = userFullName;
        this.projectName = projectName;
        this.projectLink = projectLink;
        this.updateDate = updateDate;
        this.subject = "Обновление проекта " + projectName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    @Override
    public void prepareMessage(Context context, TemplateEngine templateEngine) {
        context.setVariable("userFullName", userFullName);
        context.setVariable("projectName", projectName);
        context.setVariable("projectLink", projectLink);
        context.setVariable("updateDate", updateDate);
        this.message = templateEngine.process("project-update-mail", context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectUpdateMail that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userFullName, that.userFullName) &&
                Objects.equals(projectName, that.projectName) &&
                Objects.equals(projectLink, that.projectLink) &&
                Objects.equals(updateDate, that.updateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userFullName, projectName, projectLink, updateDate);
    }

    @Override
    public String toString() {
        return "ProjectUpdateMail{" +
                "userFullName='" + userFullName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectLink='" + projectLink + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", toEmailAddress='" + toEmailAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}