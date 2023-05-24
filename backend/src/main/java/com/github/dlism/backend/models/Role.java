package com.github.dlism.backend.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_ORGANIZER;

    @Override
    public String getAuthority() {
        return name();
    }
}