package com.example.rag.service;

import com.example.rag.model.ChunkMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class MilvusService {

    private final WebClient webClient;

    public MilvusService(@Value("${MILVUS_URL}") String milvusUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(milvusUrl)
                .build();
    }

    public void insertChunks(List<ChunkMetadata> chunks, List<List<Double>> embeddings) {
        // simplified example — you'd construct vectors here for Milvus REST
        System.out.println("Inserted " + embeddings.size() + " vectors into Milvus.");
    }

    public List<String> search(String queryEmbedding) {
        // simplified example — implement Milvus REST search here
        return List.of("Matching chunk 1", "Matching chunk 2");
    }
}
