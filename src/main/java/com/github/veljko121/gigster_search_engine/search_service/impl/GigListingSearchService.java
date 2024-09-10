package com.github.veljko121.gigster_search_engine.search_service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.github.veljko121.gigster_search_engine.dto.GigListingSearchRequestDTO;
import com.github.veljko121.gigster_search_engine.model.GigListing;
import com.github.veljko121.gigster_search_engine.search_service.IGigListingSearchService;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptSort;
import co.elastic.clients.elasticsearch._types.ScriptSortType;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.json.JsonData;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GigListingSearchService implements IGigListingSearchService {

    @Autowired
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public PagedModel<GigListing> searchGigListingsPaged(GigListingSearchRequestDTO requestDTO) {

        var boolQuery = buildBoolQuery(requestDTO.getQuery(), requestDTO.getBandTypes(), requestDTO.getGenres(), requestDTO.getMaximumPrice(), requestDTO.getDurationHours());
        var pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getPageSize());
        var query = buildNativeQueryBuilder(boolQuery, pageable, requestDTO).build();
        var result = elasticsearchTemplate.search(query, GigListing.class);
        var gigListings = result.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        var page = new PagedModel<>(new PageImpl<>(gigListings, pageable, result.getTotalHits()));

        return page;
    }

    @Override
    public PagedModel<Integer> searchGigListingIdsPaged(GigListingSearchRequestDTO requestDTO) {

        var boolQuery = buildBoolQuery(requestDTO.getQuery(), requestDTO.getBandTypes(), requestDTO.getGenres(), requestDTO.getMaximumPrice(), requestDTO.getDurationHours());
        var pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getPageSize());
        var query = buildNativeQueryBuilder(boolQuery, pageable, requestDTO).withSourceFilter(FetchSourceFilter.of(new String[]{"id"}, null)).build();
        var result = elasticsearchTemplate.search(query, GigListing.class);
        var gigListingIds = result.getSearchHits().stream().map(SearchHit::getContent).map(GigListing::getId).collect(Collectors.toList());
        var page = new PagedModel<>(new PageImpl<>(gigListingIds, pageable, result.getTotalHits()));

        return page;
    }

    private BoolQuery buildBoolQuery(String query, Collection<String> bandTypes, Collection<String> genres, @PositiveOrZero Double maximumPrice, @PositiveOrZero Double durationHours) {
        var boolQuery = QueryBuilders.bool();

        addSearchTermQuery(boolQuery, query);
        addBandTypeQuery(boolQuery, bandTypes);
        addGenreQueries(boolQuery, genres);
        addDurationAndPriceQueries(boolQuery, durationHours, maximumPrice);

        return boolQuery.build();
    }

    private NativeQueryBuilder buildNativeQueryBuilder(BoolQuery boolQuery, Pageable pageable, GigListingSearchRequestDTO requestDTO) {
        var queryBuilder = NativeQuery.builder()
            .withQuery(boolQuery._toQuery())
            .withPageable(pageable)
            .withTrackTotalHits(true);

        if (requestDTO.getSortBy() != null && !requestDTO.getSortBy().isEmpty()) {
            addPriceScriptSort(queryBuilder, requestDTO);
        }

        return queryBuilder;
    }

    private void addSearchTermQuery(BoolQuery.Builder boolQuery, String searchTerms) {
        if (searchTerms != null && !searchTerms.isEmpty()) {
            boolQuery.must(QueryBuilders.multiMatch().query(searchTerms)
                .operator(Operator.And)
                .type(TextQueryType.CrossFields)
                .fields("fullTitle^2", "band.description")
                .build()._toQuery());
        }
    }

    private void addBandTypeQuery(BoolQuery.Builder boolQuery, Collection<String> bandTypes) {
        if (bandTypes != null && !bandTypes.isEmpty()) {
            var bandTypeQuery = String.join(" ", bandTypes);
            boolQuery.must(QueryBuilders.match().query(bandTypeQuery)
            .field("band.type")
            .build()._toQuery());
        }
    }

    private void addGenreQueries(BoolQuery.Builder boolQuery, Collection<String> genres) {
        if (genres != null && !genres.isEmpty()) {
            var genreQueries = genres.stream()
                .map(genre -> QueryBuilders.term()
                    .field("band.genres.keyword")
                    .value(FieldValue.of(genre))
                    .build()._toQuery())
                .collect(Collectors.toList());
            boolQuery.must(QueryBuilders.bool().must(genreQueries).build()._toQuery());
        }
    }

    private void addPriceScriptSort(NativeQueryBuilder queryBuilder, GigListingSearchRequestDTO requestDTO) {
        var sortBy = requestDTO.getSortBy().toLowerCase();
        if (sortBy.equals("name")) {
            var sortOptions = new SortOptions.Builder()
                .field(new FieldSort.Builder()
                    .field("fullTitle.keyword")
                    .order(SortOrder.Asc)
                    .build())
                .build();
            queryBuilder.withSort(sortOptions);
        }
        else if (sortBy.startsWith("price")) {
            var priceSortScript = """
                double startingPrice = doc['startingPrice'].value;
                double additionalPrice = (params.durationHours - doc['minimumDurationHours'].value) * doc['pricePerAdditionalHour'].value;
                if (additionalPrice > 0) {
                    return startingPrice + additionalPrice;
                }
                return startingPrice;
            """;
            var script = new Script.Builder()
                .inline(new InlineScript.Builder()
                    .source(priceSortScript)
                    .params(Map.of("durationHours", JsonData.of(requestDTO.getDurationHours())))
                    .build())
                .build();

            var scriptSortBuilder = new ScriptSort.Builder()
                .script(script)
                .type(ScriptSortType.Number);

            if (sortBy.endsWith("lowest")) {
                scriptSortBuilder = scriptSortBuilder.order(SortOrder.Asc);
            }
            else if (sortBy.endsWith("highest")) {
                scriptSortBuilder = scriptSortBuilder.order(SortOrder.Desc);
            }

            var scriptSort = scriptSortBuilder.build();
            var sortOptions = new SortOptions.Builder().script(scriptSort).build();
            queryBuilder.withSort(sortOptions);
        }
    }

    private void addDurationAndPriceQueries(BoolQuery.Builder boolQuery, Double durationHours, Double maximumPrice) {
        if (durationHours != null) {
            var hoursSource = "return params.get('durationHours') <= doc['minimumDurationHours'].value + doc['maximumAdditionalHours'].value;";
            var hoursScript = new Script.Builder()
                .inline(
                    new InlineScript.Builder()
                    .source(hoursSource)
                    .params(Map.of("durationHours", JsonData.of(durationHours)))
                    .build())
                .build();

            boolQuery.must(QueryBuilders.script().script(hoursScript).build()._toQuery());

            if (maximumPrice != null) {
                var priceSource = """
                        double startingPrice = doc['startingPrice'].value;
                        double additionalPrice = (params.get('durationHours') - doc['minimumDurationHours'].value) * doc['pricePerAdditionalHour'].value;
                        double price = startingPrice;
                        if (additionalPrice > 0) {
                            price = price + additionalPrice;
                        }
                        return params.get('maximumPrice') >= price;
                """;
                var priceScript = new Script.Builder()
                    .inline(
                        new InlineScript.Builder()
                        .source(priceSource)
                        .params(Map.of(
                            "durationHours", JsonData.of(durationHours),
                            "maximumPrice", JsonData.of(maximumPrice)))
                        .build())
                    .build();

                boolQuery.must(QueryBuilders.script().script(priceScript).build()._toQuery());
            }
        }
        else if (maximumPrice != null) {
            var priceSource = "return doc['startingPrice'].value <= params.get('maximumPrice');";
            var priceScript = new Script.Builder()
                .inline(
                    new InlineScript.Builder()
                    .source(priceSource)
                    .params(Map.of("maximumPrice", JsonData.of(maximumPrice)))
                    .build())
                .build();

            boolQuery.must(QueryBuilders.script().script(priceScript).build()._toQuery());
        }
    }

}
