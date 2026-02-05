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
@Schema(description = "User model for registration")
public class UserRequestDto {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Schema(description = "User's unique email", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min=6,message = "Password must be at least 6 characters")
    @Schema(description = "User's unique password", example = "password_2025", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "FullName cannot be empty")
    @Size(min = 2, message = "FullName should consist of 5 or more characters")
    @Schema(description = "User's fullName", example = "John Snow", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;
}
