package com.github.veljko121.gigster_search_engine.service;

import org.springframework.data.web.PagedModel;

import com.github.veljko121.gigster_search_engine.core.service.ICRUDService;
import com.github.veljko121.gigster_search_engine.dto.GigListingRequestDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingResponseDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingSearchRequestDTO;
import com.github.veljko121.gigster_search_engine.dto.GigListingUpdateRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;

public interface IGigListingService extends ICRUDService<GigListing, GigListingRequestDTO, GigListingResponseDTO, GigListingUpdateRequestDTO, Integer> {

    PagedModel<GigListing> searchGigListings(GigListingSearchRequestDTO requestDTO);

    PagedModel<Integer> searchGigListingIds(GigListingSearchRequestDTO requestDTO);

}
