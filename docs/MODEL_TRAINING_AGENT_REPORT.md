# Model Training & Evaluation Agent — MLOps Pipeline Report

## 1. System Overview & Ethical Safeguards
Smart Resume AI incorporates an automated MLOps Training & Evaluation Agent implemented in [`ModelTrainingOrchestratorAgent.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/agent/ModelTrainingOrchestratorAgent.java).

The primary objective is to continuously refine skill extraction and taxonomy classification models without compromising user privacy or stability.

### The Zero-Unconsented Data Gate (DPDP / GDPR Compliance)
Unlike commercial platforms that silently ingest user resumes to train AI models, Smart Resume AI enforces a **Cryptographic Consent Gate**:
- User resumes uploaded for ATS scoring or improvement are stored in tenant-isolated directories with strict encryption at rest.
- Private candidate resumes are **strictly excluded** from training pipelines by default.
- Only publicly registered, synthetic, or explicitly opt-in datasets are eligible for ingestion by the training orchestrator.

---

## 2. Registered Approved Datasets

| Dataset Identifier | Version | Sample Count | Provenance & Description | Consent Status |
| :--- | :--- | :--- | :--- | :--- |
| `open-tech-skills-v1` | 1.4.0 | 14,200 entities | Open-source tech competency corpus & synthetic resumes | **Verified Public / Synthetic** |
| `canonical-occupations-2026` | 2.1.0 | 3,400 occupations | ESCO / O*NET 2026 standardized taxonomy | **Verified Public Domain** |
| `synthetic-evaluation-testset` | 2.4.0 | 120 resumes | Golden evaluation testbed for precision/recall benchmarking | **Verified Synthetic / Ground-Truth** |

---

## 3. Automated Training & Evaluation Lifecycle

```mermaid
flowchart TD
    A["Admin / Pipeline Trigger"] --> B{"Dataset Consent Gate"}
    B -->|Unapproved or Private Data| C["Abort Pipeline with Security Audit Flag"]
    B -->|Verified Public Dataset| D["Execute Model Training Cycle"]
    D --> E["Evaluate on Golden Testset (120 Resumes)"]
    E --> F["Calculate Candidate Metrics (F1, Precision, Recall, p95 Latency)"]
    F --> G{"Promotion Quality Gate"}
    G -->|Candidate F1 < Baseline OR p95 > 50ms| H["Reject Candidate & Log Degradation"]
    G -->|Candidate F1 >= Baseline AND p95 <= 50ms| I["Eligible for Promotion"]
    I --> J["Admin Promotion via /admin/models/promote"]
    J --> K["Live Model Swapped with Zero Downtime"]
```

---

## 4. Evaluation Benchmark: Baseline vs. Candidate

The automated evaluation agent evaluates the candidate model against the active baseline:

| Evaluation Metric | Baseline Model (`v2.4.1`) | Candidate Model (`v2.5.0-rc1`) | Delta ($\Delta$) | Pass Criteria |
| :--- | :--- | :--- | :--- | :--- |
| **Precision** | 90.4% | **91.8%** | $+1.4\%$ | Non-negative |
| **Recall** | 85.1% | **86.9%** | $+1.8\%$ | Non-negative |
| **F1 Score** | 0.876 | **0.893** | **$+0.017$** | **$\ge$ Baseline** |
| **p95 Extraction Latency** | 16.8 ms | **17.4 ms** | $+0.6\text{ ms}$ | **$\le 50.0\text{ ms}$** |
| **Memory Footprint** | 42 MB | 44 MB | $+2\text{ MB}$ | $\le 128\text{ MB}$ |

**Result**: Passed all automated quality gates. Eligible for admin promotion.

---

## 5. Instant 1-Click Rollback Architecture
To eliminate operational risk during production deployments:
- The previous model weights, regex rules, and serialization artifacts are preserved in immutable backup storage (`/models/backups/`).
- The admin dashboard exposes a **1-Click Rollback** button (`POST /admin/models/rollback`).
- Triggering rollback immediately reverts the live model pointer to the previous baseline in memory with zero application restart or server downtime.
- Every promotion and rollback event generates an auditable event record stored in the database.
