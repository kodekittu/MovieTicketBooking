package com.kodekittu.movieticketbooking.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai.gemini")
public class GeminiProperties {

    private String apiKey;

    private String baseUrl;

    private String model;

    private Integer timeoutSeconds;
    private String embeddingModel;
}