package com.github.veljko121.gigster_search_engine.dto;

import java.util.Collection;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityResponseDTO;
import com.github.veljko121.gigster_search_engine.enums.BandType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BandResponseDTO extends GenericEntityResponseDTO {

    @NotBlank
    private String name;
    
    private String description;

    @NotNull
    private BandType type;

    @NotEmpty
    private Collection<String> genres;

    @NotNull
    private RegisteredUserResponseDTO owner;

}
