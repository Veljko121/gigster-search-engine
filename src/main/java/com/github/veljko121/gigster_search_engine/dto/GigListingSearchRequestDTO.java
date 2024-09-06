package com.github.veljko121.gigster_search_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
public class GigListingSearchRequestDTO {

    @NotNull @PositiveOrZero
    private Integer page;
    
    @NotNull @PositiveOrZero
    private Integer pageSize;

    private String query;

    private Collection<String> bandTypes;

    private Collection<String> genres;
    
    @PositiveOrZero
    private Double maximumPrice;
    
    @PositiveOrZero
    private Double durationHours;

    private String sortBy;

}
