package com.example.rag.service;

import com.example.rag.model.ChunkMetadata;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfService {

    public List<ChunkMetadata> splitPdfToChunks(String filePath, int chunkSizeTokens, String fileName) throws Exception {
        List<ChunkMetadata> chunks = new ArrayList<>();
        File file = new File(filePath);
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            String[] words = text.split("\\s+");
            int chunkIndex = 0;
            for (int i = 0; i < words.length; i += chunkSizeTokens) {
                int end = Math.min(i + chunkSizeTokens, words.length);
                String chunkText = String.join(" ", java.util.Arrays.copyOfRange(words, i, end));

                ChunkMetadata chunk = new ChunkMetadata();
                chunk.setChunkId(fileName + "-" + chunkIndex);
                chunk.setFileName(fileName);
                chunk.setChunkIndex(chunkIndex);
                chunk.setChunkText(chunkText);
                chunks.add(chunk);

                chunkIndex++;
            }
        }
        return chunks;
    }
}
