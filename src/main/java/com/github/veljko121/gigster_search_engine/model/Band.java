package com.github.veljko121.gigster_search_engine.model;

import java.util.Collection;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.github.veljko121.gigster_search_engine.core.enums.BandType;
import com.github.veljko121.gigster_search_engine.core.service.model.GenericEntity;

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
    @Field(type = FieldType.Keyword)
    private BandType type;

    @NotNull
    private RegisteredUser owner;

    @Field(type = FieldType.Keyword)
    private Collection<@NotBlank String> genres;

}
