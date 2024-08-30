package com.github.veljko121.gigster_search_engine.dto;

import java.time.LocalDateTime;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityResponseDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class ListingResponseDTO extends GenericEntityResponseDTO {

    @PositiveOrZero
    private Integer durationDays;

    @NotNull
    private LocalDateTime endDate;

    private boolean active;
    
}
