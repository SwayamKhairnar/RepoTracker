package com.example.backend.services;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final TextEncryptor textEncryptor;
    public UserService(UserRepository userRepository, TextEncryptor textEncryptor) {
        this.userRepository = userRepository;
        this.textEncryptor = textEncryptor;
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User upsertFromGithub(Map<String, Object> attributes, String accessToken, String tokenScope) {

        long githubId = ((Number) attributes.get("id")).longValue();
        String githubUsername = (String) attributes.get("login");
        String displayName = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");

        User user = userRepository.findByGithubId(githubId)
                .orElseGet(() -> User.builder()
                        .githubId(githubId)
                        .build());

        user.setGithubUsername(githubUsername);
        user.setDisplayName(displayName);
        user.setAvatarUrl(avatarUrl);
        user.setAccessToken(textEncryptor.encrypt(accessToken));
        user.setTokenScope(tokenScope);

        return userRepository.save(user);
    }
}
