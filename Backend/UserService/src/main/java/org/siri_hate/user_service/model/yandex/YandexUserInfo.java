package org.siri_hate.user_service.model.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class YandexUserInfo {

    private String id;
    private String login;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("real_name")
    private String realName;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String sex;
    @JsonProperty("default_email")
    private String defaultEmail;
    private List<String> emails;
    private String birthday;
    @JsonProperty("default_phone")
    private Phone defaultPhone;

    private String psuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Phone getDefaultPhone() {
        return defaultPhone;
    }

    public void setDefaultPhone(Phone defaultPhone) {
        this.defaultPhone = defaultPhone;
    }

    public String getPsuid() {
        return psuid;
    }

    public void setPsuid(String psuid) {
        this.psuid = psuid;
    }

    public static class Phone {

        private Long id;

        private String number;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
