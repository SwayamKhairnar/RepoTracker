package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

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

            @Override
            public String encrypt(String text) {
                return HexFormat.of().formatHex(
                        encryptor.encrypt(
                                text.getBytes(StandardCharsets.UTF_8)
                        )
                );
            }

            @Override
            public String decrypt(String encryptedText) {
                return new String(
                        encryptor.decrypt(
                                HexFormat.of().parseHex(encryptedText)
                        ),
                        StandardCharsets.UTF_8
                );
            }
        };
    }
}