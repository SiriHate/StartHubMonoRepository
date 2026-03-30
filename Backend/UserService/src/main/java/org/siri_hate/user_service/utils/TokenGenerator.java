package org.siri_hate.user_service.utils;

import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class TokenGenerator {

    private final static int TOKEN_LENGTH = 32;

    private final BytesKeyGenerator bytesKeyGenerator = KeyGenerators.secureRandom(TOKEN_LENGTH);

    public String generateRandomToken() {
        byte[] key = bytesKeyGenerator.generateKey();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(key);
    }
}