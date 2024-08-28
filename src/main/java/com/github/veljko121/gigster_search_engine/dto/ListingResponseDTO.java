package com.github.veljko121.gigster_search_engine.dto;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityResponseDTO;
import com.github.veljko121.gigster_search_engine.enums.ListingType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class ListingResponseDTO extends GenericEntityResponseDTO {
    
    @NotNull
    private ListingType type;

    @PositiveOrZero
    private Integer durationDays;

    private boolean active;
    
}
