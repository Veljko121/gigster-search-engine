package com.github.veljko121.gigster_search_engine.dto;

import com.github.veljko121.gigster_search_engine.model.Band;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GigListingResponseDTO extends ListingResponseDTO {

    @PositiveOrZero @NotNull
    private Band band;

    @NotBlank
    private String fullTitle;

    @PositiveOrZero @NotNull
    private Double startingPrice;
    
    @PositiveOrZero @NotNull
    private Double pricePerAdditionalHour;

    @PositiveOrZero @NotNull
    private Double minimumDurationHours;

    @PositiveOrZero @NotNull
    private Double maximumDurationHours;

}
