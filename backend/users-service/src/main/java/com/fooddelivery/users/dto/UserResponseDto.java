package com.fooddelivery.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response model for user")
public class UserResponseDto {
    @Schema(description = "User's unique ID",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "User's unique email", example = "ivanov1999@mail.ru")
    private String email;
    @Schema(description = "User's fullName", example = "Sidorov Ivan Ivanovich")
    private String fullName;
    @Schema(description = "User's set of roles")
    private Set<RoleResponseDto> roles;
    @Schema(description = "User's set of addresses")
    private Set<AddressSlimDto> addresses=new HashSet<>();
}
