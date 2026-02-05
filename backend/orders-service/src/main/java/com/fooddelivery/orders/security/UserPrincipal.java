package com.fooddelivery.orders.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserPrincipal(Long userId, String email, Collection<? extends GrantedAuthority> authorities) {
    public Long getId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
