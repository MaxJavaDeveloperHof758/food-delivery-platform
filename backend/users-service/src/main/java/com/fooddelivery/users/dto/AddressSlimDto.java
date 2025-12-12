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
@Schema(description = "Response model for short address representation")
public class AddressSlimDto {
    @Schema(description = "Address unique ID",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Name of street",example = "Main street")
    private String street;
    @Schema(description = "Name of city",example = "Boston")
    private String city;
    @Schema(description = "Name of country",example = "United States")
    private String country;
}
