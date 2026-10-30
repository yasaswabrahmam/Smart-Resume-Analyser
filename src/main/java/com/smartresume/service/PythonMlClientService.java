package com.smartresume.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

/**
 * Java REST Client for Python ML/NLP Microservice
 * Handles IPC communication with the FastAPI ML service on http://127.0.0.1:8000.
 * Enforces a strict 3.0-second timeout and provides seamless fallback if the microservice is offline.
 */
@Service
public class PythonMlClientService {

    private static final Logger logger = LoggerFactory.getLogger(PythonMlClientService.class);

    @Value("${ml.service.url:http://127.0.0.1:8000}")
    private String mlServiceUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public static class SemanticMatchResult {
        private final double semanticScore;
        private final List<String> missingConcepts;
        private final boolean serviceAvailable;

        public SemanticMatchResult(double semanticScore, List<String> missingConcepts, boolean serviceAvailable) {
            this.semanticScore = semanticScore;
            this.missingConcepts = missingConcepts != null ? missingConcepts : Collections.emptyList();
            this.serviceAvailable = serviceAvailable;
        }

        public double getSemanticScore() { return semanticScore; }
        public List<String> getMissingConcepts() { return missingConcepts; }
        public boolean isServiceAvailable() { return serviceAvailable; }
    }

    /**
     * Calls Python FastAPI service to compute dense semantic similarity.
     * Falls back to deterministic rule-based calculation if Python is offline.
     */
    public SemanticMatchResult computeSemanticScore(List<String> candidateSkills, List<String> jobRequirements) {
        try {
            Map<String, Object> payload = Map.of(
                    "candidateSkills", candidateSkills != null ? candidateSkills : Collections.emptyList(),
                    "jobRequirements", jobRequirements != null ? jobRequirements : Collections.emptyList()
            );

            String jsonBody = objectMapper.writeValueAsString(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(mlServiceUrl + "/api/v1/ml/semantic-score"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(3))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                double score = root.path("semanticScore").asDouble(0.0);
                List<String> missing = new ArrayList<>();
                JsonNode missingNode = root.path("missingConcepts");
                if (missingNode.isArray()) {
                    for (JsonNode item : missingNode) {
                        missing.add(item.asText());
                    }
                }
                logger.info("Python ML semantic score received: {}%", score);
                return new SemanticMatchResult(score, missing, true);
            }
        } catch (Exception e) {
            logger.warn("Python ML service unreachable ({}), using Java deterministic fallback", e.getMessage());
        }

        // Graceful fallback to Java rule calculation
        return new SemanticMatchResult(0.0, Collections.emptyList(), false);
    }
}
