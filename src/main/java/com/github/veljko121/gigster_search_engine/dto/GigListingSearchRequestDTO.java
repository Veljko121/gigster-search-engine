package com.github.veljko121.gigster_search_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class GigListingSearchRequestDTO {

    private Integer page;

    private Integer pageSize;

    private String query;

    private String bandType;

    private Collection<String> genres;
    
    private Double minStartingPrice;

    private Double maxStartingPrice;

    private Double minPricePerAdditionalHour;
    
    private Double maxPricePerAdditionalHour;

    private Integer minimumMinimumDurationHours;

    private Integer maximumMinimumDurationHourus;

    private Integer minimumMaximumDurationHours;

    private Integer maximumMaximumDurationHours;
    
}
