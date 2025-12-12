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
@Schema(description = "Response model for address")
public class AddressResponseDto {
    @Schema(description = "Address unique ID",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Name of street",example = "Main street")
    private String street;
    @Schema(description = "Name of city",example = "Boston")
    private String city;
    @Schema(description = "Name of state",example = "Massachusetts")
    private String state;
    @Schema(description = "Name of country",example = "United States")
    private String country;
    @Schema(description = "User's unique ID",example = "1")
    private Long userId;
    @Schema(description = "User's fullName",example = "Sidorov Ivan Ivanovich")
    private String userFullName;
}
