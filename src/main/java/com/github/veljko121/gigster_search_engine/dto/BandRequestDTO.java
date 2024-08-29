package com.github.veljko121.gigster_search_engine.dto;

import java.util.Collection;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityRequestDTO;
import com.github.veljko121.gigster_search_engine.enums.BandType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BandRequestDTO extends GenericEntityRequestDTO {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BandType type;

    @NotNull
    private RegisteredUserRequestDTO owner;

    private Collection<String> genres;
    
}
