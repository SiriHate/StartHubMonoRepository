package org.siri_hate.notification_service.model.inapp;

import java.time.Instant;

public class InAppNotification {

    private String id;
    private String type;
    private String title;
    private String body;
    private Long chatId;
    private String senderUsername;
    private Instant createdAt;
    private boolean read;

    public InAppNotification() {
    }

    public InAppNotification(String id, String type, String title, String body, Long chatId, String senderUsername, Instant createdAt, boolean read) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.chatId = chatId;
        this.senderUsername = senderUsername;
        this.createdAt = createdAt;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
