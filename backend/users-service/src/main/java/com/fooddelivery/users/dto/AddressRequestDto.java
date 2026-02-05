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
@Schema(description = "Request model for creating/updating address")
public class AddressRequestDto {
    @NotBlank(message = "Street cannot be empty")
    @Schema(description = "Street name", example = "Main street", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;
    @NotBlank(message = "City cannot be empty")
    @Schema(description = "City name", example = "Boston", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
    @NotBlank(message = "Zip cannot be empty")
    @Schema(description = "Zip code", example = "02989", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zip;
    @Schema(description = "State name", example = "Massachusetts")
    private String state;
    @NotBlank(message = "Country cannot be empty")
    @Schema(description = "Country name", example = "United States", requiredMode = Schema.RequiredMode.REQUIRED)
    private String country;
}
