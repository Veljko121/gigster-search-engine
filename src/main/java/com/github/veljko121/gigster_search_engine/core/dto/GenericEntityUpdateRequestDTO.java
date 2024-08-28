package com.github.veljko121.gigster_search_engine.core.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenericEntityUpdateRequestDTO {

    @NotNull
    private LocalDateTime createdDateTime;
    
}