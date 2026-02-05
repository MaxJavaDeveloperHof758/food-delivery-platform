package com.fooddelivery.users.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fooddelivery.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private Long id;
    private String email;
    private String fullName;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        System.out.println("=== UserPrincipal.create() ===");
        System.out.println("Пользователь: " + user.getEmail());
        System.out.println("Роли в User entity: " + (user.getRoles() != null ? user.getRoles().size() : "null"));

        List<GrantedAuthority> authorities;

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            System.out.println("ВНИМАНИЕ: У пользователя нет ролей! Добавляем ROLE_USER по умолчанию");
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            authorities = user.getRoles().stream()
                    .map(role -> {
                        System.out.println("Преобразуем роль: " + role.getName());
                        return new SimpleGrantedAuthority(role.getName());
                    })
                    .collect(Collectors.toList());
        }

        System.out.println("Итоговые authorities: " + authorities);

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPasswordHash(),
                authorities
        );
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
