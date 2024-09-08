package com.github.veljko121.gigster_search_engine.model;

import org.springframework.data.elasticsearch.annotations.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Document(indexName = "gig-listing")
@Getter @Setter
public class GigListing extends Listing {

    @NotNull
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
    private Double maximumAdditionalHours;

    public Double getMaximumDurationHours() {
        return minimumDurationHours + maximumAdditionalHours;
    }

    public Double getMaximumPrice() {
        return startingPrice + pricePerAdditionalHour * maximumAdditionalHours;
    }

}
