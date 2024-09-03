package com.github.veljko121.gigster_search_engine.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.github.veljko121.gigster_search_engine.core.service.impl.CRUDService;
import com.github.veljko121.gigster_search_engine.dto.GigListingRequestDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingResponseDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingSearchRequestDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingUpdateRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;
import com.github.veljko121.gigster_search_engine.repository.GigListingRepository;
import com.github.veljko121.gigster_search_engine.search_service.IGigListingSearchService;
import com.github.veljko121.gigster_search_engine.service.IGigListingService;

@Service
public class GigListingService extends CRUDService<GigListing, GigListingRequestDTO, GigListingResponseDTO, GigListingUpdateRequestDTO, Integer> implements IGigListingService {

    private final ModelMapper modelMapper;

    private final IGigListingSearchService gigListingSearchService;

    @SuppressWarnings("unused")
    private final GigListingRepository gigListingRepository;

    public GigListingService(GigListingRepository gigListingRepository, IGigListingSearchService gigListingSearchService, ModelMapper modelMapper) {
        super(gigListingRepository);
        this.gigListingRepository = gigListingRepository;
        this.modelMapper = modelMapper;
        this.gigListingSearchService = gigListingSearchService;
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
        entity.setMaximumAdditionalHours(updatedEntityRequestDTO.getMaximumAdditionalHours());
        entity.setStartingPrice(updatedEntityRequestDTO.getStartingPrice());
        entity.setPricePerAdditionalHour(updatedEntityRequestDTO.getPricePerAdditionalHour());
        return entity;
    }

    @Override
    public PagedModel<GigListing> searchGigListings(GigListingSearchRequestDTO requestDTO) {
        return gigListingSearchService.searchGigListings(requestDTO);
    }
    
}
