# Python ML/NLP Service Forensic Audit

## Subject: Investigation of Python Service Presence, Models and Microservice Architecture
**Audit Status:** **Forensically Verified: Not Currently Integrated (Planned in Architecture Blueprint)**  
**Audit Date:** October 30, 2026

---

## 1. Forensic Presence Check in Current Codebase

An exhaustive search across the entire project filesystem confirms:
* **Total Python Files in `Smart-Resume-Analyzer-Java`:** **0** (`*.py` count = 0).
* **Virtual Environment Directories:** None present (`venv/`, `.venv/`, `env/` do not exist).
* **Python Manifest Files:** None present (`requirements.txt`, `Pipfile`, `pyproject.toml` do not exist in project root).
* **Subprocess Invocations in Java:** No Java classes invoke `Runtime.getRuntime().exec("python ...")` or `ProcessBuilder`.
* **Py4J / JPype Bridges:** No native JVM-Python bridges exist on the classpath.

> [!IMPORTANT]
> **Forensic Conclusion:**
> **There is no active Python microservice currently running or integrated into the executable codebase.**
> All existing resume parsing, keyword matching, ATS scoring, and LLM calls are executed 100% within the Java 21 / Spring Boot JVM process.

---

## 2. Target Python Microservice Blueprint (Planned for Phase 4)

In accordance with the [`LANGUAGE_RESPONSIBILITY_MATRIX.md`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/docs/audit/LANGUAGE_RESPONSIBILITY_MATRIX.md), Python is designated strictly as an isolated ML sidecar microservice:

```
+-----------------------------------------------------------------------------------+
|                         PLANNED PYTHON ML SERVICE TOPOLOGY                        |
+-----------------------------------------------------------------------------------+
| Host & Port           : http://127.0.0.1:8000                                     |
| Framework             : FastAPI 0.111+ (Asynchronous ASGI via Uvicorn)            |
| Schema Validation     : Pydantic v2 (Strict typing, serialization)                |
| Primary NLP Model     : spaCy (en_core_web_sm / en_core_web_md) for NER           |
| Dense Embedding Model : Sentence-Transformers (all-MiniLM-L6-v2, 384 dimensions)  |
| Vector Matcher        : Scikit-learn (Cosine similarity matrices)                 |
| Execution Runtime     : CPU-optimized PyTorch 2.3+ (No CUDA GPU required)         |
+-----------------------------------------------------------------------------------+
```

---

## 3. Planned Endpoints & REST Contract Specification

When implemented in Phase 4, the Python service will expose the following three endpoints exclusively to the Java backend:

### 1. `POST /api/v1/ml/extract-entities`
* **Purpose:** Named Entity Recognition to extract technical skills, certifications, and companies.
* **Input Schema:** `{"resumeText": string}`
* **Output Schema:** `{"skills": list[str], "experienceYears": float, "entities": list[dict]}`

### 2. `POST /api/v1/ml/semantic-score`
* **Purpose:** Compute dense vector cosine similarity between candidate skills and job requirements.
* **Input Schema:** `{"candidateSkills": list[str], "jobRequirements": list[str]}`
* **Output Schema:** `{"semanticScore": float, "matchedConcepts": list[dict], "missingConcepts": list[str]}`

### 3. `POST /api/v1/ml/embed`
* **Purpose:** Generate 384-dimensional dense vectors for vector database indexing.
* **Input Schema:** `{"texts": list[str]}`
* **Output Schema:** `{"embeddings": list[list[float]], "model": "all-MiniLM-L6-v2"}`

---

## 4. Fallback Architecture in Java

To guarantee system resilience:
1. Java will communicate with FastAPI via Spring's `RestClient` with a strict **3.0-second timeout**.
2. If the Python process is offline, times out, or fails to start, the Java `ResumeAnalyzerService` automatically falls back to its deterministic rule-based keyword matching.
3. The platform will never crash or halt due to a Python microservice unavailability.
