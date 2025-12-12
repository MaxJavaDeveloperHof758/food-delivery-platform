package com.fooddelivery.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request model for creating/updating user")
public class UserRequestDto {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Schema(description = "User's unique email", example = "ivanov1999@mail.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min=8,message = "Password must be at least 8 characters")
    @Schema(description = "User's unique password", example = "My_password2025_xxx", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "FullName cannot be null")
    @Size(min = 2, message = "FullName should consist of 5 or more characters")
    @Schema(description = "User's fullName", example = "Sidorov Ivan Ivanovich", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;
}
