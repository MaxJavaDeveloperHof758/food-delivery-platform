package com.fooddelivery.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request model for creating/updating roles")
public class RoleRequestDto {
    @NotBlank(message = "Role cannot be null")
    @Schema(description = "Role name",example = "ROLE_USER",requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
