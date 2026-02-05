package com.fooddelivery.users;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@Slf4j
public class UsersServiceApplication {

    @PostConstruct
    public void init() {
        // Это решает проблему с потерей SecurityContext между фильтрами
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        log.info("SecurityContextHolder strategy set to MODE_INHERITABLETHREADLOCAL");
    }
    public static void main(String[] args) {
        SpringApplication.run(UsersServiceApplication.class, args);
    }

}
