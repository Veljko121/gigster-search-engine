package com.github.veljko121.gigster_search_engine.model;

import java.util.Collection;

import com.github.veljko121.gigster_search_engine.core.service.model.GenericEntity;
import com.github.veljko121.gigster_search_engine.enums.BandType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Band extends GenericEntity {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BandType type;

    @NotNull
    private RegisteredUser owner;

    private Collection<String> genres;

}
