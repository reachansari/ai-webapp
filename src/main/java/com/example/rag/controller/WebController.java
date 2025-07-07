package com.example.rag.controller;

import com.example.rag.model.ChunkMetadata;
import com.example.rag.service.MilvusService;
import com.example.rag.service.PdfService;
import com.example.rag.service.WeatherService;
import com.example.rag.util.EmbeddingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class WebController {

    private final PdfService pdfService;
    private final MilvusService milvusService;
    private final WeatherService weatherService;
    private final EmbeddingUtils embeddingUtils;

    @Value("${OPENAI_API_KEY}")
    private String openaiApiKey;

    public WebController(PdfService pdfService,
                         MilvusService milvusService,
                         WeatherService weatherService) {
        this.pdfService = pdfService;
        this.milvusService = milvusService;
        this.weatherService = weatherService;
        this.embeddingUtils = new EmbeddingUtils(openaiApiKey);
    }

    @GetMapping("/")
    public String index(Model model) {
        File dir = new File("./pdfs");
        String[] files = dir.list((d, name) -> name.endsWith(".pdf"));
        model.addAttribute("pdfFiles", files == null ? List.of() : Arrays.asList(files));
        return "index";
    }

    @PostMapping("/upload")
    public String uploadPdf(@RequestParam("file") MultipartFile file) throws Exception {
        String savePath = "./pdfs/" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(savePath));
        List<ChunkMetadata> chunks = pdfService.splitPdfToChunks(savePath, 1000, file.getOriginalFilename());
        List<List<Double>> embeddings = new ArrayList<>();
        for (ChunkMetadata chunk : chunks) {
            embeddings.add(embeddingUtils.embed(chunk.getChunkText()));
        }
        milvusService.insertChunks(chunks, embeddings);
        return "redirect:/";
    }

    @PostMapping("/weather")
    public String getWeather(@RequestParam("cities") String cities, Model model) {
        Map<String, Map<String, Object>> weatherData = weatherService.getWeatherForCities(cities);
        model.addAttribute("weatherData", weatherData);
        return "index";
    }

    @GetMapping("/download/{fileName}")
    @ResponseBody
    public byte[] downloadPdf(@PathVariable String fileName) throws Exception {
        return Files.readAllBytes(Paths.get("./pdfs/" + fileName));
    }
}
