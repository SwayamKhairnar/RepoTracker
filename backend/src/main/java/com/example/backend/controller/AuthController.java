package com.example.backend.controller;

import com.example.backend.DTO.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.security.AppUserPrincipal;
import com.example.backend.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CurrentUser currentUser;

    public AuthController(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @GetMapping("/login-url")
    public Map<String, String> loginUrl() {
        return Map.of("url", "/oauth2/authorization/github");
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        AppUserPrincipal principal = currentUser.require();
        User user = principal.getUser();
        return ResponseEntity.ok(new UserResponse(user.getId(),user.getGithubId(), user.getGithubUsername(), user.getDisplayName(), user.getAvatarUrl()));
    }
}
