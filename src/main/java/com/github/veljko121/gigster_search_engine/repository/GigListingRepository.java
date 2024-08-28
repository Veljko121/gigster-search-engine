package com.github.veljko121.gigster_search_engine.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.github.veljko121.gigster_search_engine.model.GigListing;

public interface GigListingRepository extends ElasticsearchRepository<GigListing, Integer> { }
