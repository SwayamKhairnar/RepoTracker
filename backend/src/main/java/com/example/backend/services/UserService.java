package com.example.backend.services;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import org.springframework.stereotype.Service;

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
}
