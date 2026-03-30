package org.siri_hate.user_service.security;

public interface Credentialed {
    void setPassword(String password);
    String getPassword();
}
