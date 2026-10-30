# Master Current Application Technical Specification

## Project: Smart AI Resume Analyzer (Production Baseline)
**Specification Date:** October 30, 2026  
**Auditor Classification:** Lead Systems Architect & Forensic Investigator  
**Application Topology:** Monolithic Java 21 / Spring Boot 3.3.4 Web Application  
**Runtime Endpoint:** `http://localhost:8085` | **Database:** MySQL 8.4 on `127.0.0.1:3306`

---

## 1. Verified Technology Stack & Exact Versions

```
+-----------------------------------------------------------------------------------+
| COMPONENT                  | EXACT RUNTIME / LIBRARY VERSION                      |
+----------------------------+------------------------------------------------------+
| Java Runtime (JDK)         | Microsoft JDK 21.0.12.1 LTS (Compiler Target: 21)    |
| Core Application Framework | Spring Boot 3.3.4 (Jakarta EE 10, Tomcat 10.1.28)    |
| Security & Identity        | Spring Security 6.3.3 (BCrypt strength factor 12)    |
| Persistence & ORM          | Spring Data JPA / Hibernate ORM 6.5.2.Final          |
| Connection Pool            | HikariCP 5.1.0 (Pool size: 10, Min idle: 5)          |
| Relational Database        | MySQL 8.4 Server (Connector/J 8.3.0)                 |
| Document Ingestion         | Apache PDFBox 2.0.31 & Apache POI 5.2.5              |
| Resume Builder Binary Gen  | OpenPDF 1.3.39 & Apache POI 5.2.5                    |
| JSON Serialization         | Jackson Databind 2.17.2                              |
| Web View Layer             | Thymeleaf 3.1.2.RELEASE                              |
| Client UI Framework        | Bootstrap 5.3.2, FontAwesome 6.4, Chart.js 4.4       |
| LLM Integration            | Google Gemini (gemini-2.5-flash) via Java HttpClient |
| Build Tool                 | Apache Maven 3.9.6                                   |
| Automated Test Suites      | JUnit Jupiter 5.10.3, AssertJ 3.25.3, Mockito 5.11.0 |
+-----------------------------------------------------------------------------------+
```

---

## 2. Real Architecture & Process Topology

```
[Candidate Web Browser] 
          │
          │ HTTP / HTTPS on Port 8085
          ▼
+───────────────────────────────────────────────────────────────────────────────────+
|               JVM PROCESS (PID 29000: smart-resume-analyzer-1.0.0.jar)            |
|                                                                                   |
|  [Spring Security Filter Chain] (CSRF Token, BCrypt 12, Session Migration)        |
|  [Spring MVC Controllers] (Home, Auth, Dashboard, Resume, Builder, Job, Admin)    |
|  [Business Logic Services]                                                        |
|     ├── ResumeParserService (PDFBox 2.0.31, Apache POI 5.2.5)                     |
|     ├── ResumeAnalyzerService (Deterministic ATS Weighted Math Engine)            |
|     ├── AIResumeAnalyzerService (Direct Java HttpClient HTTP/2 to Gemini)         |
|     └── ResumeBuilderService (OpenPDF & Apache POI Document Exporters)           |
|  [JPA Persistence Layer] (7 Repositories via Hibernate 6.5.2)                     |
+───────────────────────────────────────────────────────────────────────────────────+
          │                                                  │
          │ JDBC (HikariCP 10 connections)                   │ Direct HTTPS REST Call
          ▼ Port 3306                                        ▼
+──────────────────────────────────────+   +────────────────────────────────────────+
| MYSQL 8.4 SERVER (PID 7672)          |   | GOOGLE GEMINI CLOUD ENDPOINT           |
| Database: `resume_analyzer`          |   | Model: gemini-2.5-flash                |
| Tables: users, resumes,              |   | Generates qualitative career guidance  |
| resume_analysis, ai_analyses,        |   | Fallback: Local Heuristic Generator    |
| job_matches, feedback, admin_logs    |   +────────────────────────────────────────+
+──────────────────────────────────────+
```

---

## 3. The Verified ATS Scoring Engine

* **Classification:** **Deterministic Rule-Based Arithmetic (NOT a Machine Learning Model).**
* **Class:** [`com.smartresume.service.ResumeAnalyzerService`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/ResumeAnalyzerService.java#L138-L146)
* **Mathematical Formula:**
  $$\text{ATS Score} = (S_{\text{contact}} \times 0.10) + (S_{\text{summary}} \times 0.10) + (S_{\text{keyword}} \times 0.30) + (S_{\text{exp}} \times 0.20) + (S_{\text{edu}} \times 0.10) + (S_{\text{format}} \times 0.20)$$
* **Keyword Matching Mechanism:** Matches text against pre-configured skill dictionaries in `JobRolesConfig.java` using lowercase string containment (`lower.contains(skill.toLowerCase())`).
* **Score Bounds:** Strictly clamped between $0$ and $100$ via `Math.min(100.0, Math.max(0.0, atsScore))`.

---

## 4. Google Gemini Integration & Offline Fallback

* **Implementation:** Direct Java HTTP/2 client in `AIResumeAnalyzerService.java` calling Google Generative Language API.
* **Active Candidate Models:** `gemini-2.5-flash` (primary), `gemini-1.5-flash`, `gemini-1.5-pro`, `gemini-pro`.
* **Prompt Construction:** Wraps resume text in boundary markers (`=== BEGIN UNTRUSTED DATA ===`).
* **Heuristic Offline Fallback:** If API keys expire or network drops, a built-in deterministic heuristic analyzer synthesizes formatted evaluations based on word counts and section presence, guaranteeing zero demonstration downtime.

---

## 5. Verified Database Schema (7 Tables in `resume_analyzer`)

1. `users` (2 records): Candidate accounts, email, BCrypt password hash, role (`ROLE_USER`, `ROLE_ADMIN`).
2. `resumes` (6 records): Uploaded files, raw extracted plain text (`LONGTEXT`), structured profile JSONs.
3. `resume_analysis` (0 records): ATS sub-scores, keyword score, formatting score, missing skill JSON.
4. `ai_analyses` (2 records): Gemini markdown analysis, model identifier, strengths, weaknesses.
5. `job_matches` (0 records): Matched job postings, match percentage, external application URLs.
6. `feedback` (7 records): Candidate platform ratings (1–5 stars) and user feedback commentary.
7. `admin_logs` (15 records): Audit trail of administrative state actions and timestamps.

---

## 6. Current Known Flaws & Security Vulnerabilities

1. **Test Database Coupling:** Integration tests (`SmartResumeAnalyzerApplicationTests`) run against the live MySQL instance because `src/test/resources/application-test.properties` does not exist.
2. **Spreadsheet Formula Injection:** CSV export in `AdminController.java` does not sanitize characters `=`, `+`, `-`, `@`.
3. **MIME Spoofing Vulnerability:** `FileValidationService.java` checks HTTP headers rather than validating physical magic bytes (`%PDF-` / `PK\x03\x04`).
4. **Brittle Markdown Regex Parsing:** `AIResumeAnalyzerService.java` parses Gemini responses using regex; if Gemini changes its output syntax, scores default silently to `75.0`.
5. **Naive Keyword Matching:** ATS scoring uses substring matching (`lower.contains()`), causing false positives (e.g. matching "Java" in "JavaScript").
6. **Route Permission Omission:** Navbar links for `/features`, `/pricing`, and `/contact` redirect to `/login` because they were omitted from the public `permitAll()` list in `SecurityConfig.java`.

---

## 7. What is Planned But Not Implemented

* **Python ML Microservice:** Zero Python files exist. The planned FastAPI service with spaCy and Sentence-Transformers (`all-MiniLM-L6-v2`) has not yet been authored.
* **Qdrant Vector Database:** No vector store client, collections, or embeddings exist.
* **Agentic AI Modules:** Multi-agent classes (`CareerAgentOrchestrator.java`, `SkillGapAgent.java`) are defined in architecture blueprints but are not yet implemented in Java.
