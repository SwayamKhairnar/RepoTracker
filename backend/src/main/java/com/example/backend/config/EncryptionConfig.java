package com.example.backend.config;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class EncryptionConfig {

    @Bean
    public TextEncryptor textEncryptor(
            @Value("${app.encryption.password}") String password,
            @Value("${app.encryption.salt}") String salt
    ) {
        AesGcmBytesEncryptor encryptor =
                AesGcmBytesEncryptor.withPassword(password, salt).build();

        return new TextEncryptor() {
            @Transactional
            @Override
            public String encrypt(String text) {
                return java.util.HexFormat.of().formatHex(
                        encryptor.encrypt(text.getBytes(java.nio.charset.StandardCharsets.UTF_8))
                );
            }
            @Transactional
            @Override
            public String decrypt(String encryptedText) {
                return new String(
                        encryptor.decrypt(java.util.HexFormat.of().parseHex(encryptedText)),
                        java.nio.charset.StandardCharsets.UTF_8
                );
            }
        };
    }
}