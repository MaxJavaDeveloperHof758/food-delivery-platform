package com.fooddelivery.restaurants.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response model for the restaurant")
public class RestaurantResponseDto {
    @Schema(description = "Restaurant's ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Restaurant's name", example = "White Lotus", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Restaurant's cuisine", example = "European, Fusion", accessMode = Schema.AccessMode.READ_ONLY)
    private String cuisine;

    @Schema(description = "Restaurant's address",
            example = "70 Nevsky Prospect, Saint Petersburg, Russia",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DishSlimDto> dishes=new ArrayList<>();
}
