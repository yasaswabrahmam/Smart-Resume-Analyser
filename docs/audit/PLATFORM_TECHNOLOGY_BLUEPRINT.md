# Official Platform Technology Blueprint & Implementation Priorities

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Core Framework:** Java 21 LTS | Spring Boot 3.3.4 | Python ML | Google Gemini | Qdrant  
**Architecture Classification:** Enterprise Hybrid AI Platform  
**Status:** Approved Master Architecture Blueprint

---

## 1. Overall System Architecture

```
+---------------------------------------------------------------------------------------------------+
|                                        EXISTING FRONTEND                                          |
|                           Thymeleaf · HTML5 · CSS3 · Modern JavaScript                            |
|                 Preserved Dark-Mode Landing Page · Candidate Dashboard · Resume Builder           |
+---------------------------------------------------------------------------------------------------+
                                                  |
                                                  v
+---------------------------------------------------------------------------------------------------+
|                                      JAVA 21 + SPRING BOOT 3.3.4                                  |
|               Main Backend · Security · ATS Scoring · Business Logic · Agent Orchestration         |
+---------------------------------------------------------------------------------------------------+
        |                                       |                                    |
        | (Documented REST API)                 | (HTTPS Direct Call)                | (Relational JDBC)
        v                                       v                                    v
+-------------------------------+   +-------------------------------+   +-----------------------+
|        PYTHON + FASTAPI       |   |       GEMINI + SPRING AI      |   |       MySQL 8.4       |
|  ML · NLP · Deep Learning     |   |  LLM · Generative AI · Tools  |   |  Users, Resumes,      |
|  Semantic Matching · PyTorch  |   |  RAG Orchestration · Prompts  |   |  Jobs, Analysis Data  |
+-------------------------------+   +-------------------------------+   +-----------------------+
        |                                       |
        +-------------------+-------------------+
                            | (Vector Embeddings)
                            v
            +-------------------------------+
            |            QDRANT             |
            |     Vector Similarity Search  |
            +-------------------------------+
```

---

## 2. The 8 Technology Pillars

### Pillar 1: Main Application — Java (Primary Technology Core)
Java manages the existing application, preserved landing page, candidate dashboard, resume lifecycle, user authorization, relational persistence, and high-level AI orchestration.

| Technology | Architectural Purpose |
| :--- | :--- |
| **Java 21 LTS** | Primary language; leverages Virtual Threads for high-concurrency non-blocking I/O. |
| **Spring Boot 3.3.4**| Core enterprise backend framework, dependency injection, and HTTP runtime. |
| **Spring Security 6.3**| Authentication, role-based authorization (`ROLE_USER`, `ROLE_ADMIN`), session fixation protection, and CSRF defense. |
| **Spring Data JPA** | Transactional database operations and repository abstractions over Hibernate 6.5. |
| **Maven** | Dependency management, compilation lifecycle, and automated build verification. |
| **Spring AI** | Connects Java directly to Gemini, handles embeddings, RAG coordination, and LLM tool calling. |
| **Thymeleaf 3.1** | Server-side template rendering for preserved responsive views. |
| **HTML, CSS, JavaScript**| Preserved dark-mode design system (`#0B0F19`), Chart.js gauges, and builder forms. |

---

### Pillar 2: Machine Learning and NLP — Python (Specialized AI Service)
Python operates as an isolated sidecar microservice (`smart-resume-ml-service` on `http://127.0.0.1:8000`) for specialized tasks where its ML ecosystem is mature and pre-trained models are available.

| Technology | Architectural Purpose |
| :--- | :--- |
| **Python 3.11+** | Foundation ML/NLP programming language. |
| **FastAPI** | High-throughput asynchronous REST API communicating with Java. |
| **Pydantic v2** | Strict schema validation and typed data serialization for all API contracts. |
| **Scikit-learn** | Deterministic ML algorithms, TF-IDF, clustering, and cosine similarity scoring. |
| **spaCy** | Linguistic parsing and Named Entity Recognition (NER) for skill, degree, and company extraction. |
| **Sentence Transformers** | Semantic text embedding generation (`all-MiniLM-L6-v2`, 384 dimensions). |
| **PyTorch (CPU-optimized)**| Deep learning model inference without requiring a dedicated CUDA GPU. |
| **NumPy & Pandas** | Vectorized array operations and structured dataset manipulation for evaluation benchmarks. |

---

### Pillar 3: LLM and Generative AI (Core AI Capability)
Gemini powers qualitative candidate feedback, cover letter drafting, resume enhancement, career coaching, and interview preparation. 

> [!IMPORTANT]
> **Direct Java Integration:** Gemini is integrated **directly into the Java Spring Boot application** using Spring AI / native Java HTTP/2 client. Python is **never** used merely as a proxy to call Gemini.

| Technology | Architectural Purpose |
| :--- | :--- |
| **Google Gemini (1.5 / 2.5 Flash)**| High-speed LLM reasoning, 1M+ token context window, deep contextual candidate appraisal. |
| **Spring AI** | Integrates Gemini client directly into Spring Boot services with native retry and error handling. |
| **Prompt Engineering** | Task-specific instructions with XML sandboxing (`<candidate_resume>`) against prompt injections. |
| **Structured JSON Output** | Native `response_schema` mode ensuring zero markdown parsing failures and predictable DTO mapping. |
| **Function Calling** | Allows Gemini to invoke approved Java platform tools (e.g., retrieving ATS keyword benchmarks). |

---

### Pillar 4: RAG and Vector Database
Retrieval-Augmented Generation grounds all career and skill advice on real job market data and curriculum benchmarks, eliminating hallucinated suggestions.

| Technology | Architectural Purpose |
| :--- | :--- |
| **RAG Architecture** | Retrieves relevant documents, job descriptions, and course syllabi before generating responses. |
| **Qdrant** | High-speed, Rust-engineered vector database storing 384-dimensional embeddings on port 6333. |
| **Dense Embeddings** | Numerical vectors capturing the deep semantic meaning of skills, resumes, and job listings. |
| **Semantic Search** | Locates job openings and skill alignments by meaning rather than naive keyword matching. |
| **Document Chunking** | Splits resumes and lengthy job specifications into structured, searchable section chunks. |

*Workflow Example:* When a student inquires *"What skills do I need to learn for a Backend Java Developer role?"*, RAG retrieves top-matched industry requisitions from Qdrant, and Gemini formulates an exact, grounded 30-day learning curriculum.

---

### Pillar 5: Agentic AI — Specialized Java Modules
The multi-agent system is structured as discrete **Java classes** coordinated by a central orchestrator. They share a unified Gemini client and approved tools—**they do not each require a separate LLM instance**.

```
                           +-------------------------------+
                           |  CareerAgentOrchestrator.java |
                           +-------------------------------+
                                           |
         +-----------------+---------------+-----------------+-----------------+
         |                 |                                 |                 |
         v                 v                                 v                 v
+-----------------+ +-----------------+             +-----------------+ +-----------------+
| ResumeAnalysis  | |    SkillGap     |             |JobRecommendation| | CareerGuidance  |
|    Agent.java   | |   Agent.java    |             |   Agent.java    | |   Agent.java    |
+-----------------+ +-----------------+             +-----------------+ +-----------------+
         |                 |                                 |                 |
         +-----------------+---------------+-----------------+-----------------+
                                           |
                         +-----------------+-----------------+
                         |                                   |
                         v                                   v
              +---------------------+             +---------------------+
              |  ResumeGeneration   |             | InterviewPreparation|
              |     Agent.java      |             |     Agent.java      |
              +---------------------+             +---------------------+
```

| Agent Class Name | Exact Responsibility |
| :--- | :--- |
| `CareerAgentOrchestrator.java` | Coordinates execution pipelines, manages state transitions, and enforces permissions. |
| `ResumeAnalysisAgent.java` | Evaluates structure, typography, action-verb density, section completeness, and readability. |
| `SkillGapAgent.java` | Compares candidate skills against live job descriptions to pinpoint missing proficiencies. |
| `JobRecommendationAgent.java` | Executes semantic vector queries against Qdrant to recommend hyper-relevant jobs. |
| `CareerGuidanceAgent.java` | Creates personalized 30/60/90-day learning roadmaps, course recommendations, and career tracks. |
| `ResumeGenerationAgent.java` | Rewrites weak bullet points, optimizes summaries, and formats content for ATS parsers. |
| `InterviewPreparationAgent.java`| Generates targeted technical and behavioral interview questions tailored to the resume's weaknesses. |

---

### Pillar 6: Document Processing Engine
Provides robust multi-format document ingestion, text sanitization, and optical character recognition.

| Technology | Architectural Purpose |
| :--- | :--- |
| **Apache PDFBox 2.0.31** | High-fidelity text extraction from standard digital PDF resumes. |
| **Apache POI 5.2.5** | Paragraph and tabular extraction from Microsoft Word `.docx` documents. |
| **Tesseract OCR** | Optical Character Recognition to extract text from scanned images and flattened PDFs. |
| **Python Preprocessing** | Advanced string cleaning, unicode normalization, and regex-based header segmentation. |

---

### Pillar 7: Database and Infrastructure
| Technology | Architectural Purpose |
| :--- | :--- |
| **MySQL 8.4** | Relational storage for users, resumes, analyses, job matches, and audit logs. |
| **Qdrant** | Vector storage for candidate embeddings and job description chunks. |
| **Docker** | Containerization of services (Qdrant, Python ML worker, MySQL). |
| **Docker Compose** | Single-command local orchestration running Java, Python, MySQL, and Qdrant in concert. |
| **Git & GitHub** | Source code management, semantic commit history, and branching workflows. |
| **GitHub Actions** | Automated CI/CD pipeline executing Maven tests, Pytest suites, and build verification. |

---

### Pillar 8: Testing Technologies
| Technology | Architectural Purpose |
| :--- | :--- |
| **JUnit 5** | Java unit testing for business services, validation, and ATS math algorithms. |
| **Mockito** | Dependency mocking for Spring services, repositories, and external HTTP clients. |
| **Pytest** | Unit testing for FastAPI routes, spaCy entity extraction, and vector normalization. |
| **Integration Testing** | Spring Boot test context verifying live Java–Python REST communication and H2 isolation. |
| **API Testing** | Automated validation of JSON contracts, status codes, and error fallbacks. |

---

## 3. Implementation Priorities: Essential vs. Later

To ensure steady progress without regressions, implementation follows this prioritized sequence:

| Priority Level | Technologies & Deliverables | Rationale & Scope |
| :--- | :--- | :--- |
| **First (Foundation)** | **Java 21, Spring Boot 3.3.4, MySQL 8.4, Preserved Frontend** | Baseline stability, test isolation (H2 database), security hardening (PII masking, magic bytes, CSV protection). |
| **Second (ML Core)** | **Python 3.11+, FastAPI, spaCy, Scikit-learn** | Dedicated ML sidecar for skill extraction, experience detection, and structured REST contracts. |
| **Third (Generative AI)**| **Google Gemini, Spring AI, Sentence Transformers** | Direct Java integration with Gemini using Structured JSON mode; dense vector semantic scoring. |
| **Fourth (RAG & Vectors)**| **Qdrant Vector Database, RAG Pipeline** | Vector indexing, chunking, and grounded candidate-to-job semantic retrieval. |
| **Fifth (Agentic AI)** | **Java Multi-Agent Modules, PyTorch Models** | `CareerAgentOrchestrator` and 6 specialized sub-agents; personalized roadmaps and interview prep. |
| **Final (DevOps & CI/CD)**| **Docker Compose, GitHub Actions, Monitoring** | Containerization, automated CI pipeline, and health telemetry. |
