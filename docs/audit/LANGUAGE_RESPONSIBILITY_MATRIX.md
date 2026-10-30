# Strict Language Responsibility and Architecture Matrix

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Primary Language:** Java 21 LTS (Spring Boot 3.3.4)  
**Secondary ML Language:** Python 3.11+ (FastAPI Microservice)  
**Governance Date:** October 2026  
**Status:** Mandatory Architectural Invariant

---

## 1. Core Principle and Boundary Invariant

> [!IMPORTANT]
> **Java is and remains the primary application language.**
> The Smart AI Resume Analyzer is fundamentally an enterprise Java Spring Boot application. 
> - Python is introduced **exclusively** as a specialized sidecar microservice for its mature Machine Learning, Deep Learning, and Natural Language Processing ecosystem.
> - We are **NOT** converting or migrating the Java application into Python.
> - We do **NOT** use Python merely as a proxy or wrapper to connect to an LLM. Google Gemini is integrated directly in Java.
> - No business functionality, database access, session management, or UI rendering shall be moved to Python.
> - Any communication between Java and Python must occur over documented, validated, typed HTTP REST endpoints with strict timeout guards and fallback resilience.

---

## 2. Definitive Language Responsibility Breakdown

```
+---------------------------------------------------------------------------------------------------+
|                                  JAVA 21 / SPRING BOOT 3.3.4 CORE                                 |
|                                     (Primary Application Tier)                                    |
+---------------------------------------------------------------------------------------------------+
| [x] Primary Web Gateway, MVC Controllers, REST Endpoints                                          |
| [x] Spring Security 6.3 Authentication, Authorization, Session Management, CSRF                  |
| [x] User Management, Profile Data, Resume File Storage, and Entity Lifecycles                    |
| [x] MySQL Database Persistence (Spring Data JPA, Hibernate 6.5, HikariCP)                         |
| [x] Deterministic ATS Mathematical Scoring Engine (Baseline Rule Calculation)                     |
| [x] Direct Google Gemini LLM Integration (Java HttpClient / Spring AI Client)                    |
| [x] Prompt Construction, XML Sandboxing, and Structured JSON Schema Validation                    |
| [x] RAG Orchestration (Candidate chunking, query formulation, and retrieval coordination)        |
| [x] Central Multi-Agent Orchestrator and Agent Permission Enforcement                             |
| [x] Binary Document Generation (OpenPDF & Apache POI for Resume Builder)                          |
| [x] Preserved Frontend (Thymeleaf, HTML5, CSS design tokens, JavaScript widgets)                 |
+---------------------------------------------------------------------------------------------------+
                                                  |
                                                  | Typed HTTP/JSON REST API
                                                  | (Spring RestClient with 3.0s Timeout)
                                                  v
+---------------------------------------------------------------------------------------------------+
|                                     PYTHON 3.11+ / FASTAPI                                        |
|                                    (Dedicated ML/NLP Worker)                                      |
+---------------------------------------------------------------------------------------------------+
| [x] Advanced Named Entity Recognition (spaCy: extracting custom skills, organizations, degrees)   |
| [x] Experience and Date Extraction from irregular document patterns                               |
| [x] Resume Role Classification (Supervised ML classifiers evaluated on labeled benchmark sets)     |
| [x] Dense Semantic Embeddings Generation (Sentence-Transformers 'all-MiniLM-L6-v2', 384 dims)     |
| [x] Cosine Semantic Similarity Matching between resume vectors and job requisition vectors         |
| [x] ML/DL Model Preprocessing, Tokenization, and CPU-Optimized Inference                          |
+---------------------------------------------------------------------------------------------------+
```

---

## 3. Justification of Technology Selection

### 3.1 Why Java Retains LLM, RAG & Agent Orchestration
1. **Type Safety & Data Integrity:** RAG workflows and Multi-Agent state machines handle candidate records, session tokens, and database transactions. Java's static typing and concurrency model prevent race conditions and payload corruption.
2. **Elimination of Unnecessary Hops:** Routing LLM calls through Python when Java can directly communicate with Google Gemini via HTTP/2 or Spring AI adds needless latency, network serialization overhead, and another point of failure.
3. **Enterprise Security:** Spring Security provides industry-standard role-based authorization, CSRF mitigation, and session fixation defense that Python micro-frameworks require manual reimplementation to achieve.

### 3.2 Why Python is Utilized for ML/NLP
1. **Mature Pre-Trained NLP Models:** `spaCy` and Hugging Face `sentence-transformers` have state-of-the-art weights specifically fine-tuned for semantic text similarity (`all-MiniLM-L6-v2`).
2. **Memory & CPU Efficiency:** Standard sentence-transformer inference runs with minimal overhead in C-optimized PyTorch CPU binaries without polluting JVM garbage collection or heap allocation.

---

## 4. Documented Java-to-Python REST API Contract

The Python service (`smart-resume-ml-service`) runs independently on `http://127.0.0.1:8000`. All endpoints must be documented with OpenAPI/Swagger.

### Endpoint 1: Semantic Entity & Skill Extraction
* **Route:** `POST /api/v1/ml/extract-entities`
* **Request Payload (JSON):**
  ```json
  {
    "resumeText": "Experienced Senior Java Developer with 5 years in Spring Boot, Docker, and AWS."
  }
  ```
* **Response Payload (JSON):**
  ```json
  {
    "skills": ["Java", "Spring Boot", "Docker", "AWS"],
    "experienceYears": 5.0,
    "entities": [
      { "text": "Senior Java Developer", "label": "ROLE" },
      { "text": "AWS", "label": "CLOUD" }
    ],
    "extractionLatencyMs": 42
  }
  ```

### Endpoint 2: Semantic Vector Similarity Matching
* **Route:** `POST /api/v1/ml/semantic-score`
* **Request Payload (JSON):**
  ```json
  {
    "candidateSkills": ["Java", "Spring Boot", "MySQL", "REST APIs"],
    "jobRequirements": ["Java 21", "Microservices", "Spring Boot", "Relational Databases", "Docker"]
  }
  ```
* **Response Payload (JSON):**
  ```json
  {
    "semanticScore": 84.5,
    "matchedConcepts": [
      { "candidate": "Java", "job": "Java 21", "similarity": 0.98 },
      { "candidate": "MySQL", "job": "Relational Databases", "similarity": 0.91 }
    ],
    "missingConcepts": ["Docker", "Microservices"]
  }
  ```

### Endpoint 3: Dense Vector Embeddings Generation
* **Route:** `POST /api/v1/ml/embed`
* **Request Payload (JSON):**
  ```json
  {
    "texts": ["Proficient in Spring Cloud and Kafka streaming pipelines."]
  }
  ```
* **Response Payload (JSON):**
  ```json
  {
    "embeddings": [
      [-0.034, 0.082, 0.015, "... 384 dimensions ..."]
    ],
    "model": "all-MiniLM-L6-v2"
  }
  ```

---

## 5. Resilience & Fallback Invariant

If the Python ML microservice becomes unavailable, crashes, or exceeds the 3.0-second HTTP timeout:
1. **No Application Crash:** The Java Spring Boot backend catches the `ResourceAccessException` / `TimeoutException`.
2. **Seamless Fallback:** The ATS scoring engine in `ResumeAnalyzerService.java` automatically falls back to 100% deterministic rule-based keyword matching.
3. **Transparent Logging:** An administrative warning is logged, but the user receives their ATS evaluation without interruption.

---

## 6. Prohibited Actions (Change Control Rule)

To prevent architectural drift:
* ❌ **PROHIBITED:** Rewriting Spring Boot controllers or services in Python.
* ❌ **PROHIBITED:** Connecting Python directly to the MySQL database or altering schemas from Python.
* ❌ **PROHIBITED:** Using Python as a middleman for Google Gemini API requests.
* ❌ **PROHIBITED:** Moving the Thymeleaf frontend or static assets to Python.
* ❌ **PROHIBITED:** Removing or bypassing the deterministic Java ATS formula.
* ❌ **PROHIBITED:** Moving any feature between languages without prior documentation, test verification, and explicit user approval.
