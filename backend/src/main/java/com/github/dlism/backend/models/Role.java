package com.github.dlism.backend.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_ORGANIZER, ROLE_NOT_ACTIVE;

    @Override
    public String getAuthority() {
        return name();
    }
}