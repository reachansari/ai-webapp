package com.example.rag.util;

import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

public class EmbeddingUtils {

    private final WebClient webClient;

    public EmbeddingUtils(String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/embeddings")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public List<Double> embed(String text) {
        Map<String, Object> request = Map.of(
                "model", "text-embedding-3-small",
                "input", text
        );

        Map<String, Object> response = webClient
                .post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<List<Double>> embeddings = (List<List<Double>>)
                ((Map<String, Object>) ((List<Object>) response.get("data")).get(0)).get("embedding");

        return embeddings.get(0);
    }
}