# RAG, Vector Search and Agentic AI Forensic Audit

## Subject: Implementation Status of Retrieval-Augmented Generation, Vectors and Autonomous Agents
**Audit Date:** October 30, 2026  
**Status:** **Forensically Verified: Formally Planned in Architecture, Not Yet Implemented in Code**

---

## 1. Executive Forensic Determination

A complete scan of `src/main/java/com/smartresume/` and `pom.xml` establishes the following truth:

> [!IMPORTANT]
> **RAG, Vector Database (Qdrant), and Multi-Agent Orchestrator classes are currently PLANNED ONLY.**
> - **Zero vector database dependencies:** Neither `io.qdrant:client` nor Spring AI VectorStore starters are present in `pom.xml`.
> - **Zero vector collections or embeddings:** No vector points or embedding arrays exist in the database.
> - **Zero agent classes:** Classes such as `CareerAgentOrchestrator.java` and `SkillGapAgent.java` do not yet exist in `src/main/java/com/smartresume/service/`.
> - The existing application operates as a classic **Spring MVC 3-tier monolithic service**.

---

## 2. Planned RAG & Vector Search Specification (Phase 5)

When introduced in Phase 5 of the approved roadmap, the RAG architecture will follow this validated design:

```
[Candidate Resume Profile]               [Job Requisition Corpus]
           │                                         │
           ▼                                         ▼
   Text Segmentation                         Text Segmentation
   (Summary, Skills, Exp)                    (Requirements, Skills)
           │                                         │
           ▼                                         ▼
   Sentence-Transformers                     Sentence-Transformers
   (all-MiniLM-L6-v2, 384-dim)               (all-MiniLM-L6-v2, 384-dim)
           │                                         │
           └───────────────────┬─────────────────────┘
                               │
                               ▼
                   [Qdrant Vector Database]
                   - Collection: `job_requisitions`
                   - Collection: `candidate_profiles`
                   - Distance: Cosine Metric
                   - Tenant Scoping: `user_id` payload filter
                               │
                               ▼
                   [Grounded Gemini Synthesis]
                   - Context: "Retrieved Requisition: Java 21, Kafka, AWS"
                   - Output: Personalized 30-Day Skill Bridge Plan
```

---

## 3. Planned Agentic AI Modules Specification (Phase 6)

The multi-agent system will be authored as distinct Java service modules under package `com.smartresume.agent`:

| Agent Class Name | Operational Scope | Approved Tools | Permission Boundaries |
| :--- | :--- | :--- | :--- |
| `CareerAgentOrchestrator.java` | Central pipeline conductor | State machine executor, agent registry | Enforces transactional consistency and request timeouts. |
| `ResumeAnalysisAgent.java` | Structural & visual appraisal | PDFBox token metrics, readability index | Read-only access to resume text. |
| `SkillGapAgent.java` | Live industry skill comparison | Domain skill taxonomy, Qdrant search | Compares candidate skills against job market requisitions. |
| `JobRecommendationAgent.java` | Hyper-aligned job discovery | Qdrant vector similarity API | Retrieves top-k matching jobs scoped to candidate role. |
| `CareerGuidanceAgent.java` | Actionable learning roadmaps | Gemini structured JSON prompt | Formulates 30/60/90-day learning curriculum. |
| `ResumeGenerationAgent.java` | Bullet-point enhancement | OpenPDF / POI builders, Gemini rewrite | Generates revised phrasing without fabricating qualifications. |
| `InterviewPreparationAgent.java`| Mock technical interview prep | Gemini prompt with candidate gaps | Generates 5 technical and 3 behavioral questions based on resume weaknesses. |

---

## 4. Key Architectural Invariant for Agents

* **Shared Gemini Model:** The agents will share a single, managed Gemini HTTP client instance in Spring Boot. They do **not** each spin up a separate LLM process.
* **Deterministic Math Anchor:** Agents cannot alter or fabricate the mathematical ATS score computed by `ResumeAnalyzerService`.
* **Zero Autonomous Rejection:** Agents provide coaching, diagnostic suggestions, and interview preparation; they never automatically reject or disqualify candidates.
