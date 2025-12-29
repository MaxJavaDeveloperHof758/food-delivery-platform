package com.fooddelivery.orders.integration;

import com.fooddelivery.orders.integration.dto.AddressResponseDto;
import com.fooddelivery.orders.integration.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "users-service", url = "${integration.users-service.url}")
public interface UserServiceClient {
    @GetMapping("/api/users/{id}/exists")
    Boolean checkUserExists(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}")
    UserResponseDto getUser(@PathVariable("id") Long id);

    @GetMapping("/api/addresses/user/{userId}")
    List<AddressResponseDto> getAddressesByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/api/addresses/{id}")
    ResponseEntity<AddressResponseDto> getAddressById(@PathVariable("id") Long id);
}
