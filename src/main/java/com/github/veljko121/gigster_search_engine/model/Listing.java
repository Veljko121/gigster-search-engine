package com.github.veljko121.gigster_search_engine.model;

import java.time.LocalDateTime;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.github.veljko121.gigster_search_engine.core.service.model.GenericEntity;
import com.github.veljko121.gigster_search_engine.enums.ListingType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Listing extends GenericEntity {

    @NotNull
    @Field(type = FieldType.Keyword)
    private ListingType type = ListingType.GIG;

    @NotNull @Positive
    private Integer durationDays;

    public LocalDateTime getEndDate() {
        return getCreatedDateTime().plusDays(durationDays);
    }

    public boolean isActive() {
        var now = LocalDateTime.now();
        return now.isAfter(getCreatedDateTime()) && now.isBefore(getEndDate());
    }
    
}
