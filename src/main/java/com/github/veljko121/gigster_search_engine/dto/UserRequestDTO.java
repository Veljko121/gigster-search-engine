package com.github.veljko121.gigster_search_engine.dto;

import com.github.veljko121.gigster_search_engine.core.dto.GenericEntityRequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDTO extends GenericEntityRequestDTO {

    @NotBlank
    private String username;
    
}
