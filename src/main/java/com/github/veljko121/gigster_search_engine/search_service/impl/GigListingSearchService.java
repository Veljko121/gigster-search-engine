package com.github.veljko121.gigster_search_engine.search_service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.github.veljko121.gigster_search_engine.dto.GigListingSearchRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;
import com.github.veljko121.gigster_search_engine.search_service.IGigListingSearchService;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GigListingSearchService implements IGigListingSearchService {

    @Autowired
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public PagedModel<GigListing> searchGigListings(GigListingSearchRequestDTO requestDTO) {
        var boolQuery = QueryBuilders.bool();

        if (requestDTO.getQuery() != null) {
            boolQuery.must(QueryBuilders.multiMatch().query(requestDTO.getQuery()).operator(Operator.And).fields("fullTitle^2", "band.description").build()._toQuery());
        }

        if (requestDTO.getBandType() != null) {
            boolQuery.must(QueryBuilders.match().query(requestDTO.getBandType()).field("band.type").build()._toQuery());
        }

        // if (requestDTO.getGenres() != null) {
        //     List<FieldValue> genreFieldValues = requestDTO.getGenres().stream()
        //         .map(FieldValue::of)
        //         .collect(Collectors.toList());
        //     TermsQuery termsQuery = QueryBuilders.terms()
        //         .field("band.genres.keyword")
        //         .terms(tq -> tq.value(genreFieldValues))
        //         .build();
        //     boolQuery.must(termsQuery._toQuery());
        // }

        Pageable pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getPageSize());
        var queryBuilder = NativeQuery.builder()
            .withQuery(boolQuery.build()._toQuery())
            .withPageable(pageable)
            .withTrackTotalHits(true);

        var query = queryBuilder.build();

        var result = elasticsearchTemplate.search(query, GigListing.class);

        long totalHits = result.getTotalHits();
        List<GigListing> gigListings = result.getSearchHits().stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());

        return new PagedModel<>(new PageImpl<>(gigListings, pageable, totalHits));
    }
}
