package com.kodekittu.movieticketbooking.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai.pinecone")
public class PineconeProperties {

    private String apiKey;

    private String host;

    private String namespace;
}