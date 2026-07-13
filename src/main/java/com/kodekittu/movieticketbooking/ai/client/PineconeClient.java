package com.kodekittu.movieticketbooking.ai.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.kodekittu.movieticketbooking.ai.config.PineconeProperties;
import com.kodekittu.movieticketbooking.ai.dto.VectorSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PineconeClient implements VectorStoreClient {

    private final RestClient restClient;
    private final PineconeProperties properties;

    @Override
    public void upsert(
            String id,
            List<Double> embedding,
            Map<String, String> metadata
    ) {

        String url = properties.getHost() + "/vectors/upsert";

        Map<String, Object> request = Map.of(
                "vectors",
                List.of(
                        Map.of(
                                "id", id,
                                "values", embedding,
                                "metadata", metadata
                        )
                ),
                "namespace",
                properties.getNamespace()
        );

        restClient.post()
                .uri(url)
                .header("Api-Key", properties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        log.info("Vector uploaded successfully : {}", id);
    }

    @Override
    public List<VectorSearchResult> similaritySearch(
            List<Double> embedding,
            int topK
    ) {

        String url = properties.getHost() + "/query";

        Map<String, Object> request = Map.of(
                "vector", embedding,
                "topK", topK,
                "includeMetadata", true,
                "namespace", properties.getNamespace()
        );

        JsonNode response = restClient.post()
                .uri(url)
                .header("Api-Key", properties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(JsonNode.class);

        List<VectorSearchResult> results = new ArrayList<>();

        JsonNode matches = response.path("matches");

        for (JsonNode match : matches) {

            results.add(
                    VectorSearchResult.builder()
                            .id(match.path("id").asText())
                            .score(match.path("score").asDouble())
                            .metadata(
                                    match.has("metadata")
                                            ? Map.of(
                                            "title",
                                            match.path("metadata")
                                                    .path("title")
                                                    .asText(),

                                            "genre",
                                            match.path("metadata")
                                                    .path("genre")
                                                    .asText(),

                                            "language",
                                            match.path("metadata")
                                                    .path("language")
                                                    .asText()
                                    )
                                            : Map.of()
                            )
                            .build()
            );

        }

        return results;

    }

}