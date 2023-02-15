package com.gabrielluciano.blog.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, WRITER;

    @Override
    public String getAuthority() {
        switch (this) {
            case ADMIN:
                return "ADMIN";

            case WRITER:
                return "WRITER";
        }
        return null;
    }
}
