package com.github.veljko121.gigster_search_engine.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.github.veljko121.gigster_search_engine.core.service.impl.CRUDService;
import com.github.veljko121.gigster_search_engine.dto.GigListingRequestDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingResponseDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingUpdateRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;
import com.github.veljko121.gigster_search_engine.repository.GigListingRepository;
import com.github.veljko121.gigster_search_engine.service.IGigListingService;

@Service
public class GigListingService extends CRUDService<GigListing, GigListingRequestDTO, GigListingResponseDTO, GigListingUpdateRequestDTO, Integer> implements IGigListingService {

    private final ModelMapper modelMapper;

    @SuppressWarnings("unused")
    private final GigListingRepository gigListingRepository;

    public GigListingService(GigListingRepository gigListingRepository, ModelMapper modelMapper) {
        super(gigListingRepository);
        this.gigListingRepository = gigListingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    protected GigListingResponseDTO mapToResponseDTO(GigListing entity) {
        var responseDTO = modelMapper.map(entity, GigListingResponseDTO.class);
        return responseDTO;
    }

    @Override
    protected GigListing mapToDomain(GigListingRequestDTO requestDTO) {
        var entity = modelMapper.map(requestDTO, GigListing.class);
        return entity;
    }

    @Override
    protected GigListing mapUpdatedFieldsToDomain(GigListing entity, GigListingUpdateRequestDTO updatedEntityRequestDTO) {
        entity.setCreatedDateTime(updatedEntityRequestDTO.getCreatedDateTime());
        entity.setDurationDays(updatedEntityRequestDTO.getDurationDays());
        entity.setBand(updatedEntityRequestDTO.getBand());
        entity.setMinimumDurationHours(updatedEntityRequestDTO.getMinimumDurationHours());
        entity.setMaximumDurationHours(updatedEntityRequestDTO.getMaximumDurationHours());
        entity.setStartingPrice(updatedEntityRequestDTO.getStartingPrice());
        entity.setPricePerAdditionalHour(updatedEntityRequestDTO.getPricePerAdditionalHour());
        return entity;
    }
    
}
