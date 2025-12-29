package com.fooddelivery.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request model for creating/updating restaurant")
public class RestaurantRequestDto {
    @NotBlank(message = "Restaurant's name cannot be empty")
    @Schema(description = "Restaurant's unique name", example = "White Lotus", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Cuisine type cannot be empty")
    @Schema(description = "Restaurant's cuisine",example = "European, Fusion",requiredMode = Schema.RequiredMode.REQUIRED)
    private String cuisine;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, message = "Address must consist of 5 or more characters")
    @Schema(description = "Restaurant's address which describes it's location",
    example = "70 Nevsky Prospect, Saint Petersburg, Russia",
    requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;
}
