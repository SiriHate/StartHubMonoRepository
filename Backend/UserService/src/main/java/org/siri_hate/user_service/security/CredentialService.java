package org.siri_hate.user_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CredentialService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CredentialService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(String currentPassword, String newPassword, Credentialed credentialed) {
        String passwordFromDB = credentialed.getPassword();
        if (!passwordEncoder.matches(currentPassword, passwordFromDB)) {
            throw new BadCredentialsException("Invalid old password!");
        }
        credentialed.setPassword(passwordEncoder.encode(newPassword));
    }

    public void setPassword(Credentialed credentialed, String newPassword) {
        credentialed.setPassword(passwordEncoder.encode(newPassword));
    }

    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}