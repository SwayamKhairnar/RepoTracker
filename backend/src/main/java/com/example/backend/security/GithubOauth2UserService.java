package com.example.backend.security;

import com.example.backend.entity.User;
import com.example.backend.services.UserService;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service

public class GithubOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    public GithubOauth2UserService(UserService userService) {
        this.userService = userService;
    }
    @Override
    public @Nullable OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String tokenScope = String.join(",", userRequest.getAccessToken().getScopes());

        User user=userService.upsertFromGithub(oAuth2User.getAttributes(), accessToken, tokenScope);
        return new AppUserPrincipal(user, oAuth2User.getAttributes());
    }
}
