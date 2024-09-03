package com.github.veljko121.gigster_search_engine.search_service.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.github.veljko121.gigster_search_engine.dto.GigListingSearchRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;
import com.github.veljko121.gigster_search_engine.search_service.IGigListingSearchService;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
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

        if (requestDTO.getGenres() != null && !requestDTO.getGenres().isEmpty()) {
            var genreQueries = requestDTO.getGenres().stream()
                .map(genre -> QueryBuilders.term().field("band.genres.keyword").value(FieldValue.of(genre)).build()._toQuery())
                .collect(Collectors.toList());

            var genreBoolQuery = QueryBuilders.bool().must(genreQueries);

            boolQuery.must(genreBoolQuery.build()._toQuery());
        }

        if (requestDTO.getDurationHours() != null) {
            var hoursSource = "return doc['minimumDurationHours'].value <= params.get('durationHours') && params.get('durationHours') <= doc['minimumDurationHours'].value + doc['maximumAdditionalHours'].value;";
    
            var hoursScript = new Script.Builder()
                .inline(new InlineScript.Builder()
                .source(hoursSource)
                .params(Map.of(
                    "durationHours", JsonData.of(requestDTO.getDurationHours())
                ))
                .build())
                .build();
            var hoursQuery = QueryBuilders.script().script(hoursScript).build();
            boolQuery.must(hoursQuery._toQuery());

            if (requestDTO.getMaximumPrice() != null) {
                var priceSource = """
                        double price = doc['startingPrice'].value + (params.get('durationHours') - doc['minimumDurationHours'].value) * doc['pricePerAdditionalHour'].value;
                        return price <= params.get('maximumPrice');
                        """;
                var priceScript = new Script.Builder()
                    .inline(new InlineScript.Builder()
                    .source(priceSource)
                    .params(Map.of(
                        "durationHours", JsonData.of(requestDTO.getDurationHours()),
                        "maximumPrice", JsonData.of(requestDTO.getMaximumPrice())
                    ))
                    .build())
                    .build();
                var priceQuery = QueryBuilders.script().script(priceScript).build();
                boolQuery.must(priceQuery._toQuery());
            }

        }
        else {
            if (requestDTO.getMaximumPrice() != null) {
                var priceSource = """
                        return doc['startingPrice'].value <= params.get('maximumPrice');
                        """;
                var priceScript = new Script.Builder()
                    .inline(new InlineScript.Builder()
                    .source(priceSource)
                    .params(Map.of(
                        "maximumPrice", JsonData.of(requestDTO.getMaximumPrice())
                    ))
                    .build())
                    .build();
                var priceQuery = QueryBuilders.script().script(priceScript).build();
                boolQuery.must(priceQuery._toQuery());
            }
        }

        var pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getPageSize());
        var queryBuilder = NativeQuery.builder()
            .withQuery(boolQuery.build()._toQuery())
            .withPageable(pageable)
            .withTrackTotalHits(true);

        var query = queryBuilder.build();

        var result = elasticsearchTemplate.search(query, GigListing.class);

        var totalHits = result.getTotalHits();
        var gigListings = result.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());

        return new PagedModel<>(new PageImpl<>(gigListings, pageable, totalHits));
    }
}
