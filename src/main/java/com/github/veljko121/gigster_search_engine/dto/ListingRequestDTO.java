package com.github.veljko121.gigster_search_engine.dto;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityRequestDTO;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ListingRequestDTO extends GenericEntityRequestDTO {

    @PositiveOrZero
    private Integer durationDays;
    
}