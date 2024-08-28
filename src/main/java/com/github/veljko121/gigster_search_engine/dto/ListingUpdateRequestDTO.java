package com.github.veljko121.gigster_search_engine.dto;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityUpdateRequestDTO;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ListingUpdateRequestDTO extends GenericEntityUpdateRequestDTO {

    @PositiveOrZero
    private Integer durationDays;
    
}
