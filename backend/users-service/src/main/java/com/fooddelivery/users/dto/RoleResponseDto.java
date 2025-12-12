package com.fooddelivery.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response model for role")
public class RoleResponseDto {
    @Schema(description = "Role ID",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Role name",example = "ROLE_USER")
    private String name;
}
