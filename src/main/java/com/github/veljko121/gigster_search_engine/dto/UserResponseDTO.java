package com.github.veljko121.gigster_search_engine.dto;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityResponseDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO extends GenericEntityResponseDTO {
    
    @NotBlank
    private String username;

}
