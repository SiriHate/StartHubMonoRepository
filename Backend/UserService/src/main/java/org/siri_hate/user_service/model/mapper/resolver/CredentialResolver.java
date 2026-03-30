package org.siri_hate.user_service.model.mapper.resolver;

import org.mapstruct.Named;
import org.siri_hate.user_service.security.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CredentialResolver {
    
    private final CredentialService credentialService;

    @Autowired
    public CredentialResolver(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Named("toEncryptedPassword")
    public String toEncryptedPassword(String rawPassword) {
        return credentialService.encryptPassword(rawPassword);
    }
}