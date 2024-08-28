package com.github.veljko121.gigster_search_engine.model;

import com.github.veljko121.gigster_search_engine.core.service.model.GenericEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User extends GenericEntity {

    @NotBlank
    private String username;

}
