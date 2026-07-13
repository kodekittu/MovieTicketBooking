package com.kodekittu.movieticketbooking.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        GeminiProperties.class,
        PineconeProperties.class
})
public class AIConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

}