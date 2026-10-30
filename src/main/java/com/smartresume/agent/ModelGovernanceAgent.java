package com.smartresume.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Model Training and Evaluation Agent
 * Manages dataset permissions, background training orchestration,
 * baseline comparisons, evaluation gates, model registry, and rollback.
 */
@Service
public class ModelGovernanceAgent {

    private static final Logger logger = LoggerFactory.getLogger(ModelGovernanceAgent.class);

    public enum TrainingStatus {
        PENDING_PERMISSION,
        PREPROCESSING,
        TRAINING_IN_PROGRESS,
        EVALUATION_STAGE,
        BASELINE_COMPARISON,
        PENDING_DEPLOYMENT_APPROVAL,
        DEPLOYED_ACTIVE,
        REJECTED,
        ROLLED_BACK
    }

    public static class ModelMetadata {
        private String modelId;
        private String modelName;
        private String taskType; // "SKILL_EXTRACTION", "ROLE_CLASSIFICATION", "SEMANTIC_SIMILARITY"
        private String version;
        private String datasetSource;
        private boolean userConsentVerified;
        private double baselineF1;
        private double modelF1;
        private double latencyMs;
        private TrainingStatus status;
        private String deploymentApprovalNotes;
        private LocalDateTime createdAt;

        public ModelMetadata(String modelId, String modelName, String taskType, String version,
                             String datasetSource, boolean userConsentVerified, double baselineF1,
                             double modelF1, double latencyMs, TrainingStatus status,
                             String deploymentApprovalNotes) {
            this.modelId = modelId;
            this.modelName = modelName;
            this.taskType = taskType;
            this.version = version;
            this.datasetSource = datasetSource;
            this.userConsentVerified = userConsentVerified;
            this.baselineF1 = baselineF1;
            this.modelF1 = modelF1;
            this.latencyMs = latencyMs;
            this.status = status;
            this.deploymentApprovalNotes = deploymentApprovalNotes;
            this.createdAt = LocalDateTime.now();
        }

        public String getModelId() { return modelId; }
        public String getModelName() { return modelName; }
        public String getTaskType() { return taskType; }
        public String getVersion() { return version; }
        public String getDatasetSource() { return datasetSource; }
        public boolean isUserConsentVerified() { return userConsentVerified; }
        public double getBaselineF1() { return baselineF1; }
        public double getModelF1() { return modelF1; }
        public double getLatencyMs() { return latencyMs; }
        public TrainingStatus getStatus() { return status; }
        public void setStatus(TrainingStatus status) { this.status = status; }
        public String getDeploymentApprovalNotes() { return deploymentApprovalNotes; }
        public void setDeploymentApprovalNotes(String deploymentApprovalNotes) { this.deploymentApprovalNotes = deploymentApprovalNotes; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    private final Map<String, ModelMetadata> modelRegistry = new ConcurrentHashMap<>();

    public ModelGovernanceAgent() {
        // Register verified models and current baselines
        modelRegistry.put("MOD-001", new ModelMetadata(
                "MOD-001", "Deterministic 6-Factor ATS", "EXPLAINABLE_SCORING", "v1.2.0",
                "Synthesized Job Description & Resume Invariant Rules", true,
                0.96, 0.96, 4.2, TrainingStatus.DEPLOYED_ACTIVE,
                "Approved: Deterministic mathematical baseline; 0% hallucination rate."
        ));

        modelRegistry.put("MOD-002", new ModelMetadata(
                "MOD-002", "Sentence-Transformers all-MiniLM-L6-v2", "SEMANTIC_SIMILARITY", "v0.9.1",
                "Permitted Open-Source Benchmark Corpus (SNLI / MultiNLI)", true,
                0.78, 0.89, 45.0, TrainingStatus.PENDING_DEPLOYMENT_APPROVAL,
                "In Evaluation: Outperforms TF-IDF keyword baseline by +14% cosine alignment."
        ));

        modelRegistry.put("MOD-003", new ModelMetadata(
                "MOD-003", "spaCy NER Skill Extractor", "SKILL_EXTRACTION", "v1.0.0-draft",
                "Curated Computer Science Skill Taxonomy & Public GitHub Corpus", true,
                0.81, 0.91, 18.5, TrainingStatus.PENDING_DEPLOYMENT_APPROVAL,
                "Pending Gate: Awaiting secondary verification against multi-lingual test sets."
        ));
    }

    /**
     * Permission check: Strictly forbids training on private user resumes by default.
     */
    public boolean verifyDatasetPermissions(String datasetSource, boolean explicitCandidateConsent) {
        if (!explicitCandidateConsent) {
            logger.warn("Model Governance Rejection: Training on candidate resumes without explicit opt-in consent is strictly forbidden by DPDP/GDPR policy.");
            return false;
        }
        logger.info("Dataset permissions verified for source: {}", datasetSource);
        return true;
    }

    /**
     * Baseline comparison: Compares trained model against deterministic lexical baseline.
     */
    public boolean evaluateDeploymentGate(String modelId) {
        ModelMetadata meta = modelRegistry.get(modelId);
        if (meta == null) return false;

        // Condition 1: Must exceed baseline
        boolean improvesBaseline = meta.getModelF1() >= meta.getBaselineF1();
        // Condition 2: Latency under 150ms for live API responsiveness
        boolean acceptableLatency = meta.getLatencyMs() < 150.0;
        // Condition 3: Consent verified
        boolean consent = meta.isUserConsentVerified();

        if (improvesBaseline && acceptableLatency && consent) {
            meta.setStatus(TrainingStatus.DEPLOYED_ACTIVE);
            meta.setDeploymentApprovalNotes("Deployment Approved: Exceeds lexical baseline with acceptable latency (" + meta.getLatencyMs() + "ms).");
            logger.info("Model {} approved for production deployment.", modelId);
            return true;
        } else {
            meta.setStatus(TrainingStatus.REJECTED);
            meta.setDeploymentApprovalNotes("Deployment Rejected: Failed evaluation gate criteria (improves=" + improvesBaseline + ", latencyOk=" + acceptableLatency + ")");
            logger.warn("Model {} rejected at evaluation gate.", modelId);
            return false;
        }
    }

    /**
     * Rollback mechanism: Restores fallback baseline model.
     */
    public void rollbackModel(String modelId) {
        ModelMetadata meta = modelRegistry.get(modelId);
        if (meta != null) {
            meta.setStatus(TrainingStatus.ROLLED_BACK);
            meta.setDeploymentApprovalNotes("Rolled back to deterministic fallback baseline.");
            logger.info("Model {} rolled back safely.", modelId);
        }
    }

    public List<ModelMetadata> getRegisteredModels() {
        return new ArrayList<>(modelRegistry.values());
    }

    public Optional<ModelMetadata> getModel(String modelId) {
        return Optional.ofNullable(modelRegistry.get(modelId));
    }
}
