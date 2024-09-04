package com.github.veljko121.gigster_search_engine.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.PutIndicesSettingsRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final ElasticsearchClient elasticsearchClient;

    private final Logger logger;

    public void updateMaxResultWindow(String indexName) throws IOException {
        IndexSettings settings = new IndexSettings.Builder()
            .maxResultWindow(50000)
            .build();

        var request = new PutIndicesSettingsRequest.Builder().index(indexName).settings(settings).build();

        var response = elasticsearchClient.indices().putSettings(request);
        
        if (response.acknowledged()) {
            logger.info("Settings updated successfully.");
        } else {
            logger.error("Failed to update settings.");
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            updateMaxResultWindow("gig-listing");
        } catch (IOException e) {
            logger.error("Error while setting up max results window.");
        }
    }
}