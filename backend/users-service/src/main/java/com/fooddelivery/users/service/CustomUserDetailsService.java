package com.fooddelivery.users.service;

import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.repository.UserRepository;
import com.fooddelivery.users.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true, noRollbackFor = UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        log.info("=== CustomUserDetailsService.loadUserByUsername() ВЫЗВАН ===");
        log.info("Загрузка пользователя: {}", email);

        Optional<User> userOpt = userRepository.findByEmailWithRoles(email);

        if (!userOpt.isPresent()) {
            userOpt = userRepository.findByEmail(email);
            log.info("Пользователь загружен без ролей");
        }

        User user = userOpt.orElseThrow(() -> {
            log.error("Пользователь не найден: {}", email);
            return new UsernameNotFoundException("User not found: " + email);
        });

        log.info("Найден пользователь: {}, ID: {}", user.getEmail(), user.getId());

        boolean rolesInitialized = org.hibernate.Hibernate.isInitialized(user.getRoles());
        log.info("Роли инициализированы: {}", rolesInitialized);
        log.info("Количество ролей: {}", user.getRoles().size());

        if (!rolesInitialized) {
            log.info("Пытаемся инициализировать роли вручную...");
            org.hibernate.Hibernate.initialize(user.getRoles());
            log.info("После инициализации - количество ролей: {}", user.getRoles().size());
        }

        user.getRoles().forEach(role ->
                log.info("Роль: {}", role.getName()));

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        log.info("UserPrincipal authorities: {}",
                userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        return userPrincipal;
    }
}
