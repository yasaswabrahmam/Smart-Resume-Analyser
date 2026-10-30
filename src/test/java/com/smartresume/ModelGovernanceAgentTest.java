package com.smartresume;

import com.smartresume.agent.ModelGovernanceAgent;
import com.smartresume.agent.ModelGovernanceAgent.ModelMetadata;
import com.smartresume.agent.ModelGovernanceAgent.TrainingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelGovernanceAgentTest {

    private ModelGovernanceAgent governanceAgent;

    @BeforeEach
    void setUp() {
        governanceAgent = new ModelGovernanceAgent();
    }

    @Test
    void testDatasetConsentVerification() {
        // Without consent, training must be strictly forbidden
        boolean denied = governanceAgent.verifyDatasetPermissions("user_resumes_dump.json", false);
        assertFalse(denied, "Training without explicit user consent must be blocked");

        // With explicit consent, training is permitted
        boolean allowed = governanceAgent.verifyDatasetPermissions("permitted_open_benchmark.json", true);
        assertTrue(allowed, "Training with verified consent must be allowed");
    }

    @Test
    void testEvaluationGateDeploymentApproval() {
        // MOD-002 has modelF1 (0.89) > baselineF1 (0.78) and latency (45ms) < 150ms
        boolean approved = governanceAgent.evaluateDeploymentGate("MOD-002");
        assertTrue(approved, "Model exceeding baseline with acceptable latency must pass evaluation gate");

        ModelMetadata meta = governanceAgent.getModel("MOD-002").orElseThrow();
        assertEquals(TrainingStatus.DEPLOYED_ACTIVE, meta.getStatus());
        assertTrue(meta.getDeploymentApprovalNotes().contains("Deployment Approved"));
    }

    @Test
    void testModelRollbackMechanism() {
        governanceAgent.rollbackModel("MOD-002");
        ModelMetadata meta = governanceAgent.getModel("MOD-002").orElseThrow();
        assertEquals(TrainingStatus.ROLLED_BACK, meta.getStatus());
        assertTrue(meta.getDeploymentApprovalNotes().contains("Rolled back"));
    }

    @Test
    void testModelRegistryIntegrity() {
        List<ModelMetadata> models = governanceAgent.getRegisteredModels();
        assertFalse(models.isEmpty(), "Model registry must contain registered models");
        assertTrue(models.stream().anyMatch(m -> "MOD-001".equals(m.getModelId())));
    }
}
