package com.kodekittu.movieticketbooking.ai.service;

import com.kodekittu.movieticketbooking.ai.client.GeminiClient;
import com.kodekittu.movieticketbooking.ai.dto.ChatRequest;
import com.kodekittu.movieticketbooking.ai.dto.ChatResponse;
import com.kodekittu.movieticketbooking.ai.dto.LLMRequest;
import com.kodekittu.movieticketbooking.ai.dto.LLMResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIChatService {

    private final GeminiClient geminiClient;

    public ChatResponse chat(ChatRequest request) {

        LLMResponse response = geminiClient.generateResponse(
                LLMRequest.builder()
                        .prompt(request.getPrompt())
                        .build()
        );

        return ChatResponse.builder()
                .response(response.getResponse())
                .build();

    }

}