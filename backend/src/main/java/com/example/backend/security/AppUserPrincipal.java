package com.example.backend.security;

import com.example.backend.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AppUserPrincipal implements OAuth2User {
    private final User user;
    private final Map<String, Object> attributes;

    AppUserPrincipal(User user) {
        this.user = user;
        this.attributes = Map.of();
    }
    public AppUserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public UUID getUserId() {
        return user.getId();
    }
    public User getUser() {
        return user;
    }

    @Override
    public @Nullable <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getName() {
        return user.getId().toString();
    }
}
