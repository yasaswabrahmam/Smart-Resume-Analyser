package com.smartresume;

import com.smartresume.service.CareerKnowledgeRagService;
import com.smartresume.service.CareerKnowledgeRagService.GroundedAnswer;
import com.smartresume.service.CareerKnowledgeRagService.RagRetrievalResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Career Knowledge RAG Service Tests")
public class CareerKnowledgeRagServiceTest {

    private CareerKnowledgeRagService ragService;

    @BeforeEach
    void setUp() {
        ragService = new CareerKnowledgeRagService();
    }

    @Test
    @DisplayName("Verify retrieval returns relevant chunks with score and rationale")
    void testRetrieveKnowledge() {
        List<RagRetrievalResult> results = ragService.retrieveKnowledge("Spring Boot microservices Java backend", "ROLE_TAXONOMY", 3);

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getRelevanceScore() > 0.40);
        assertNotNull(results.get(0).getChunk().getSourceUrl());
        assertNotNull(results.get(0).getRelevanceRationale());
    }

    @Test
    @DisplayName("Verify grounded answer generation cites official sources and timestamps")
    void testGenerateGroundedAnswer() {
        GroundedAnswer answer = ragService.generateGroundedAnswer("What is required for a production RAG system?", null);

        assertNotNull(answer);
        assertFalse(answer.isUncertaintyDisclosed());
        assertFalse(answer.getCitedSources().isEmpty());
        assertTrue(answer.getAnswerText().contains("Qdrant Vector Database Official Docs"));
        assertNotNull(answer.getFreshnessStatement());
    }

    @Test
    @DisplayName("Verify query with zero match discloses uncertainty rather than hallucinating")
    void testUncertaintyDisclosure() {
        GroundedAnswer answer = ragService.generateGroundedAnswer("Quantum teleportation warp drive mechanics", null);

        assertNotNull(answer);
        assertTrue(answer.isUncertaintyDisclosed());
        assertTrue(answer.getAnswerText().contains("does not currently contain verified information"));
    }
}
