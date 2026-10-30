package com.smartresume;

import com.smartresume.agent.ModelTrainingOrchestratorAgent;
import com.smartresume.agent.ModelTrainingOrchestratorAgent.ModelCandidateRecord;
import com.smartresume.agent.ModelTrainingOrchestratorAgent.TrainingPipelineStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model Training & Evaluation Orchestrator Agent Tests")
public class ModelTrainingOrchestratorAgentTest {

    private ModelTrainingOrchestratorAgent agent;

    @BeforeEach
    void setUp() {
        agent = new ModelTrainingOrchestratorAgent();
    }

    @Test
    @DisplayName("Verify approved dataset discovery and consent verification")
    void testDatasetCatalogDiscovery() {
        assertFalse(agent.getDatasetCatalog().isEmpty());
        agent.getDatasetCatalog().forEach(ds -> {
            assertNotNull(ds.getDatasetId());
            assertNotNull(ds.getLicense());
            assertTrue(ds.isExplicitConsentObtained(), "All catalog datasets must have verified consent");
            assertFalse(ds.isContainsPrivateUserData(), "Catalog datasets must not contain unconsented private user data");
        });
    }

    @Test
    @DisplayName("Verify candidate model training evaluates against baseline F1")
    void testTrainingPipelineExecution() {
        ModelCandidateRecord result = agent.runTrainingPipeline("ds-open-skills-v1", "SKILL_EXTRACTION", "spaCy EntityRuler");

        assertNotNull(result);
        assertTrue(result.getCandidateF1() >= result.getBaselineF1(), "Candidate F1 must be measured against baseline");
        assertEquals(TrainingPipelineStatus.READY_FOR_APPROVAL, result.getStatus());
        assertTrue(result.getP95LatencyMs() > 0);
    }

    @Test
    @DisplayName("Verify administrator approval gate required for production promotion")
    void testProductionPromotionGate() {
        ModelCandidateRecord candidate = agent.runTrainingPipeline("ds-open-skills-v1", "ROLE_CLASSIFICATION", "TF-IDF + Logistic Regression");

        assertEquals(TrainingPipelineStatus.READY_FOR_APPROVAL, candidate.getStatus());

        boolean promoted = agent.promoteCandidateToProduction(candidate.getModelId(), "admin@smartresume.com");
        assertTrue(promoted);
        assertEquals(TrainingPipelineStatus.PROMOTED_PRODUCTION, candidate.getStatus());
    }

    @Test
    @DisplayName("Verify automated rollback mechanism restores stable baseline")
    void testRollbackMechanism() {
        ModelCandidateRecord candidate = agent.runTrainingPipeline("ds-open-skills-v1", "SKILL_EXTRACTION", "Sentence-Transformers");
        agent.promoteCandidateToProduction(candidate.getModelId(), "admin@smartresume.com");

        boolean rolledBack = agent.rollbackModel(candidate.getModelId(), "Detected inference latency spike > 120ms");
        assertTrue(rolledBack);
        assertEquals(TrainingPipelineStatus.ROLLED_BACK, candidate.getStatus());
        assertTrue(candidate.getEvaluationNotes().contains("Rolled back to stable baseline"));
    }
}
