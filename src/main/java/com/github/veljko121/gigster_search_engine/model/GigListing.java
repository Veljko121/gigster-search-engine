package com.github.veljko121.gigster_search_engine.model;

import org.springframework.data.elasticsearch.annotations.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Document(indexName = "gig-listing")
@Getter @Setter
public class GigListing extends Listing {

    @NotNull
    private Band band;

    @PositiveOrZero @NotNull
    private Double startingPrice;
    
    @PositiveOrZero @NotNull
    private Double pricePerAdditionalHour;

    @PositiveOrZero @NotNull
    private Double minimumDurationHours;

    @PositiveOrZero @NotNull
    private Double maximumDurationHours;

}
