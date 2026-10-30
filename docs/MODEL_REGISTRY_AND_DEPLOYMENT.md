# Model Registry and Deployment Architecture

## 1. Registry Architecture Overview
The Model Registry provides a centralized, versioned repository of all active, evaluated, and candidate intelligence artifacts utilized across Smart Resume AI. 

The registry decouples model development and evaluation from production serving, enabling hot-swapping and deterministic fallbacks.

---

## 2. Model Inventory & Metadata Schema

Every registered model conforms to a structured metadata specification:
- **`modelId`**: Unique alphanumeric identifier (e.g., `skill-extractor-canonical`).
- **`version`**: Semantic version string (`MAJOR.MINOR.PATCH`).
- **`status`**: Current deployment state (`ACTIVE`, `CANDIDATE`, `DEPRECATED`, `ROLLED_BACK`).
- **`inputContract`**: Accepted input MIME types and schema.
- **`outputContract`**: Expected structure of predictions and scores.
- **`goldenF1`**: Benchmark score on the synthetic evaluation testset.
- **`latencyP95Ms`**: Tail latency under standard production load.
- **`promotedAt`**: Timestamp of admin authorization.

### Registered Model Registry Inventory

| Model ID | Version | Engine / Framework | Status | Latency (p95) | F1 / Score |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `skill-extractor-canonical` | `v2.5.0` | Boundary-safe Regex + ESCO 60+ Vocabulary | **ACTIVE** | 17.4 ms | F1: 0.893 |
| `ats-scoring-deterministic` | `v2.3.2` | 6-Factor Java Weighted Math Engine | **ACTIVE** | 22.1 ms | 100% Explainable |
| `career-knowledge-rag` | `v1.2.0` | Hybrid Lexical/Semantic Scorer | **ACTIVE** | 28.6 ms | Grounded Citations |
| `career-copilot-orchestrator` | `v1.0.0` | Allowlisted Multi-Tool Finite State Machine | **ACTIVE** | 35.0 ms | 17 Allowed Tools |
| `gemini-deep-feedback` | `v1.5-flash`| Google Gemini REST API (with heuristic fallback) | **ACTIVE** | 2,400 ms | Qualitative Review |
| `remote-jobs-aggregator` | `v1.8.0` | Remotive REST API + Multi-Portal Query Engine | **ACTIVE** | 450.0 ms | 7 Aggregated Portals |

---

## 3. Zero-Downtime Hot-Swap & Promotion Protocol
Model deployment is managed through [`ModelTrainingOrchestratorAgent.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/agent/ModelTrainingOrchestratorAgent.java) and exposed via admin endpoints:

1. **Candidate Staging**: Candidate models run in parallel shadow-mode or against isolated evaluation sets without receiving live user traffic.
2. **Promotion Gate**: The candidate must achieve an F1 score greater than or equal to the active baseline and maintain $p95 \le 50\text{ms}$.
3. **Atomic Pointer Swap**: Upon admin promotion (`POST /admin/models/promote`), an `AtomicReference` in Java swaps the active model instance in memory. Active user requests in flight complete cleanly on their existing instance.
4. **Instant Rollback**: If downstream observability detects drift or anomalies, triggering `POST /admin/models/rollback` immediately restores the prior baseline pointer.

---

## 4. Deployment Environment Configuration

Key configuration parameters defined in [`application.properties`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/resources/application.properties):

```properties
# Server Port
server.port=8085

# Database Connectivity (MySQL 8.4 Production / H2 In-Memory Fallback)
spring.datasource.url=jdbc:mysql://localhost:3306/smart_resume_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root123

# Career Copilot Orchestrator Budget
copilot.budget.max-steps=5
copilot.human-gate.timeout-seconds=300

# RAG Service Confidence Threshold
rag.confidence.threshold=0.45

# LLM Integration
gemini.api.key=${GEMINI_API_KEY:demo-key}
gemini.api.model=gemini-1.5-flash
```
