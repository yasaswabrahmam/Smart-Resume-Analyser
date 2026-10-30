# Dependency & Technology Compatibility Report

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Framework:** Spring Boot 3.3.4 | Java 21 LTS  
**Audit Date:** October 2026  
**Status:** Phase 1 Technology Audit

---

## 1. Executive Summary

This report establishes the baseline compatibility profile of the existing Java 21 / Spring Boot 3.3.4 technology stack and defines the architectural boundaries for introducing external AI/ML subsystems (FastAPI Python microservice, Spring AI, Qdrant Vector Database, and Gemini LLM Structured Outputs) without creating circular dependencies, runtime regressions, or dependency conflicts.

---

## 2. Current Maven Dependency Matrix (`pom.xml`)

| Dependency Group & Artifact | Current Version | Runtime Scope | Compatibility Status | Notes / Upgrade Path |
| :--- | :--- | :--- | :--- | :--- |
| `org.springframework.boot:spring-boot-starter-web` | `3.3.4` | Compile | **Fully Compatible** | Uses Jakarta EE 10 / Tomcat 10.1. Embedded servlet container. |
| `org.springframework.boot:spring-boot-starter-security` | `3.3.4` | Compile | **Fully Compatible** | Spring Security 6.3. Uses Lambda DSL; deprecates `authorizeRequests()`. |
| `org.springframework.boot:spring-boot-starter-data-jpa` | `3.3.4` | Compile | **Fully Compatible** | Hibernate 6.5 core, HikariCP 5.1 connection pool. |
| `org.springframework.boot:spring-boot-starter-thymeleaf` | `3.3.4` | Compile | **Fully Compatible** | Thymeleaf 3.1.2 with Spring Security 6 dialect integration. |
| `org.springframework.boot:spring-boot-starter-validation` | `3.3.4` | Compile | **Fully Compatible** | Hibernate Validator 8.0.3 (Jakarta Validation 3.0). |
| `com.mysql:mysql-connector-j` | `8.3.0` | Runtime | **Fully Compatible** | Tested and verified against local MySQL 8.4 Server on port 3306. |
| `com.h2database:h2` | `2.2.224` | Runtime/Test | **Compatible** | Available for test database isolation; currently underutilized in test configs. |
| `org.apache.pdfbox:pdfbox` | `2.0.31` | Compile | **Stable** | PDF extraction engine. PDFBox 3.x exists with API breaking changes; retain 2.0.31 to preserve parser stability. |
| `org.apache.poi:poi-ooxml` | `5.2.5` | Compile | **Stable** | DOCX parsing and builder document assembly. Stable with Log4j2 bridges. |
| `com.github.librepdf:openpdf` | `1.3.39` | Compile | **Stable** | Clean LGPL/MPL PDF generation engine for Resume Builder exports. |
| `com.fasterxml.jackson.core:jackson-databind` | `2.17.2` | Compile | **Fully Compatible** | Core JSON serialization for AI requests, parser DTOs, and REST payloads. |
| `org.seleniumhq.selenium:selenium-java` | `4.20.0` | Compile | **Caution** | Heavy footprint in web tier; recommend isolating to background scraping worker. |
| `io.github.bonigarcia:webdrivermanager` | `5.8.0` | Compile | **Caution** | Auto-downloads Chrome/Gecko drivers; susceptible to network timeouts in isolated CI. |
| `org.springframework.boot:spring-boot-starter-test` | `3.3.4` | Test | **Fully Compatible** | JUnit 5.10.3, AssertJ 3.25.3, Mockito 5.11.0. |
| `org.springframework.security:spring-security-test` | `3.3.4` | Test | **Fully Compatible** | `SecurityMockMvcRequestPostProcessors.csrf()`, `withMockUser()`. |

---

## 3. Python ML & Microservice Compatibility Matrix

To support modern Natural Language Processing (semantic parsing, embeddings, and hybrid ATS vector scoring), a dedicated Python microservice (`smart-resume-ml-service`) is planned. The table below delineates the verified library versions and compatibility boundaries:

| Technology / Library | Target Version | Python Target | Purpose in Architecture | Compatibility Assessment |
| :--- | :--- | :--- | :--- | :--- |
| **Python** | `3.11.x` / `3.12.x` | Base Runtime | Foundation runtime for ML worker | High compatibility with wheels; avoids 3.13 GIL transitions. |
| **FastAPI** | `0.111.0+` | Python 3.11+ | Asynchronous REST API server | High throughput via Starlette/Uvicorn; native OpenAPI auto-docs. |
| **Pydantic** | `2.7.0+` | Python 3.11+ | Request/response schema validation | High-speed Rust core (Pydantic V2); strict type enforcement. |
| **Scikit-learn** | `1.5.0+` | Python 3.11+ | Cosine similarity, TF-IDF, clustering | Deterministic keyword vector scoring; negligible latency. |
| **spaCy** | `3.7.4+` | Python 3.11+ | Named Entity Recognition (NER) | Entity extraction (`ORG`, `DATE`, `DEGREE`, `SKILL`). |
| **Sentence-Transformers** | `3.0.1+` | Python 3.11+ | Semantic vector embedding generation | Pre-trained models (`all-MiniLM-L6-v2`, 384 dims) optimized for CPU. |
| **PyTorch (torch)** | `2.3.1+cpu` | Python 3.11+ | Underlying tensor computation | Use CPU-only distribution to avoid requiring CUDA/GPU on developer machines. |
| **Qdrant Client** | `1.9.1+` | Python 3.11+ | Vector search client | Connects to Qdrant vector database via gRPC or REST on port 6333. |

---

## 4. Integration Conflict Analysis

### 4.1 Java Spring AI vs. Existing Gemini HTTP/2 Client
- **Current State:** `AIResumeAnalyzerService.java` communicates directly with Google Gemini via standard Java 11 `HttpClient` using HTTP/2 POST requests to `https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent`.
- **Spring AI Compatibility Evaluation:**
  - Spring AI `1.0.0-M3` / `1.0.0-SNAPSHOT` introduces `spring-ai-gemini-ai` and `ChatClient` abstractions.
  - *Risk:* Spring AI milestones are evolving rapidly, and dependency management can pull experimental transitive dependencies that may conflict with Spring Boot 3.3.4 starters.
  - *Architectural Recommendation:* Maintain the existing robust, zero-dependency `HttpClient` implementation in `AIResumeAnalyzerService.java` as the stable primary channel. Upgrade prompt templates to demand `response_schema` (JSON schema mode) supported by Gemini `gemini-1.5-flash` and `gemini-2.5-flash`. Optionally wrap Spring AI inside an isolated modular adapter if conversational memory or Spring AI Document Readers are needed.

### 4.2 Python IPC Communication Strategy
- **Option A (In-Process JPype/Py4J):** High JVM memory overhead, native library crashing risks, thread contention. **Not recommended.**
- **Option B (Separate Process REST via FastAPI):** Standard Microservice pattern. Spring Boot communicates with FastAPI on `http://127.0.0.1:8000` via Spring `RestClient` or `WebClient` with HTTP connection timeouts and circuit breaker fallback. **Strongly Recommended.**
  - Zero JVM pollution.
  - Independent lifecycle, independent logging, independent scaling.
  - If Python service is offline, Java backend seamlessly falls back to existing local rule-based ATS scoring without interrupting user experience.

### 4.3 Java 21 LTS Compatibility Verification
- Target bytecode: Java 21 (`<java.version>21</java.version>` in `pom.xml`).
- Virtual Threads (`Project Loom`): Fully supported in Spring Boot 3.3.x (`spring.threads.virtual.enabled=true`). Enables high-concurrency non-blocking I/O when calling external AI/ML endpoints.
- Pattern Matching and Record classes: Fully compatible with Jakarta validation and Jackson 2.17.

---

## 5. Architectural Upgrade Feasibility Summary

```
+---------------------------------------------------------------------------------+
|                                 APPLICATION TIER                                |
|   Java 21 + Spring Boot 3.3.4 (Tomcat 10.1 / Virtual Threads Ready)             |
|   Security 6.3 + Hibernate 6.5 + MySQL 8.4 + OpenPDF + POI                     |
+---------------------------------------------------------------------------------+
                                      |
       +------------------------------+------------------------------+
       | (HTTP/JSON RestClient)                                      | (HTTPS / REST)
       v                                                             v
+-------------------------------+                         +---------------------+
|      PYTHON ML SERVICE        |                         |     GOOGLE GEMINI   |
| FastAPI + Pydantic v2         |                         | gemini-1.5-flash    |
| Sentence Transformers (MiniLM)|                         | Structured JSON     |
| Scikit-learn + spaCy          |                         | REST API Endpoint   |
+-------------------------------+                         +---------------------+
       |
       | (gRPC / HTTP 6333)
       v
+-------------------------------+
|      VECTOR DATABASE          |
| Qdrant 1.9+ (Docker/Cloud)    |
| Domain Embeddings (384-dim)   |
+-------------------------------+
```

All technologies have been evaluated for version alignment, licensing compliance, and failure-mode resilience.
