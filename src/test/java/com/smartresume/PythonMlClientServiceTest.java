package com.smartresume;

import com.smartresume.service.PythonMlClientService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PythonMlClientServiceTest {

    private final PythonMlClientService clientService = new PythonMlClientService();

    @Test
    void testOfflineGracefulFallback() {
        // When Python microservice is not running on port 8000, call must gracefully fall back without throwing an exception
        PythonMlClientService.SemanticMatchResult result = clientService.computeSemanticScore(
                List.of("Java", "Spring Boot"),
                List.of("Java", "Docker", "AWS")
        );

        assertNotNull(result);
        assertFalse(result.isServiceAvailable(), "Should detect that external microservice is offline or mock URL");
        assertNotNull(result.getMissingConcepts());
    }
}
