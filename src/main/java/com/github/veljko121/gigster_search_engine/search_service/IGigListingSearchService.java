package com.github.veljko121.gigster_search_engine.search_service;

import org.springframework.data.web.PagedModel;

import com.github.veljko121.gigster_search_engine.dto.GigListingSearchRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;

public interface IGigListingSearchService {

    PagedModel<GigListing> searchGigListingsPaged(GigListingSearchRequestDTO requestDTO);

    PagedModel<Integer> searchGigListingIdsPaged(GigListingSearchRequestDTO requestDTO);

}
