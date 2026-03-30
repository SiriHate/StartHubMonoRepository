package org.siri_hate.user_service.kafka.message;

public class ProjectUpdateNotification {

    private String projectName;
    private String updateDate;
    private String projectLink;
    private String username;
    private String userRealName;
    private String userEmailAddress;

    public ProjectUpdateNotification() {}

    public ProjectUpdateNotification(String projectName, String updateDate, String projectLink,
                                     String username, String userRealName, String userEmailAddress) {
        this.projectName = projectName;
        this.updateDate = updateDate;
        this.projectLink = projectLink;
        this.username = username;
        this.userRealName = userRealName;
        this.userEmailAddress = userEmailAddress;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }
}
