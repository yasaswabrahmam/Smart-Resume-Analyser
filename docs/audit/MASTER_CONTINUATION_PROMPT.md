# Master Project Continuation Prompt

> [!TIP]
> **Instructions for the User:**
> Copy and paste the entire block below into any future Antigravity or AI coding session. It gives the AI an exact, verified understanding of the codebase so it will never guess, hallucinate non-existent ML models, or break your existing working landing page.

---

```markdown
# CONTEXT & INSTRUCTIONS: SMART AI RESUME ANALYZER PROJECT

You are continuing development on the **SMART AI RESUME ANALYZER** project located in the workspace folder `Smart-Resume-Analyzer-Java`.

Before taking any action, review and strictly adhere to these verified facts, architecture rules, and implementation priorities.

---

## 1. CORE REPOSITORY FACTS & RUNTIME TOPOLOGY (VERIFIED OCTOBER 2026)

- **Primary Application Core:** Enterprise Java 21 LTS, Spring Boot 3.3.4, Spring Security 6.3, Spring Data JPA / Hibernate 6.5, Apache Maven 3.9.6.
- **Relational Database:** MySQL 8.4 Server on `127.0.0.1:3306` with database name `resume_analyzer`.
- **Active Web Port:** `http://localhost:8085` (Configured in `application.properties`).
- **Verified Test Baseline:** 8 test classes, 22 test methods, 100% pass rate (`mvn test`).
- **Frontend Stack:** Thymeleaf 3, Bootstrap 5.3, FontAwesome 6, Chart.js 4.4, and custom dark-theme CSS (`src/main/resources/static/css/main.css`, 1,343 lines).

---

## 2. STRICT INVARIANTS & PRESERVATION RULES

1. **JAVA IS THE PRIMARY LANGUAGE:**
   - Java and Spring Boot manage web traffic, security, database persistence, ATS scoring, document building, Gemini API calls, and agent orchestration.
   - Under no circumstances should the Java backend be rewritten in Python.
2. **PRESERVE THE LANDING PAGE & DESIGN SYSTEM:**
   - The landing page at `src/main/resources/templates/index.html` and layout `src/main/resources/templates/layout/base.html` must be preserved. Do not redesign, rewrite, or replace the UI theme.
3. **GEMINI IS INTEGRATED DIRECTLY IN JAVA:**
   - Google Gemini is called directly from Java in `AIResumeAnalyzerService.java` using native HTTP/2. Python is NOT used as an LLM middleman.
4. **FORENSIC REALITY OF CURRENT CODEBASE:**
   - **ATS Scoring is RULE-BASED:** No trained machine learning model exists in the codebase today. The ATS score is computed by a 6-component weighted arithmetic formula in `ResumeAnalyzerService.java`:
     ATS Score = (Contact * 0.1) + (Summary * 0.1) + (Keyword * 0.3) + (Exp * 0.2) + (Edu * 0.1) + (Format * 0.2).
   - **Python ML Microservice is PLANNED ONLY:** There are currently 0 Python files in the repository. Python will be introduced as an isolated sidecar microservice (FastAPI on port 8000) solely for ML/NLP (spaCy NER, Sentence-Transformers embeddings).
   - **Qdrant Vector DB & AI Agents are PLANNED ONLY:** Neither Qdrant nor classes like `CareerAgentOrchestrator.java` exist yet in source code.
5. **CANDIDATE PROTECTION POLICY:**
   - The platform is an educational diagnostic tool for candidates, not an automated candidate rejection system.

---

## 3. IDENTIFIED FLAWS & PRIORITY 1 REMEDIATION PLAN

Before implementing new features, execute the following **Priority 1 (Phase 2)** tasks:

1. **Test Database Isolation (High Priority):**
   - Create `src/test/resources/application-test.properties` configured with an in-memory H2 database (`jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL`) so automated unit and integration tests run independently of MySQL.
2. **File Upload Magic Bytes Verification (Medium Priority):**
   - In `FileValidationService.java`, add physical stream magic byte checks (`%PDF-` for PDF, `PK\x03\x04` for DOCX) to prevent MIME-type spoofing.
3. **In-Memory PII Masking (High Priority / DPDP Act 2023):**
   - In `ResumeParserService.java`, implement regex masking for candidate phone numbers and email addresses before transmitting text to external AI endpoints.
4. **CSV Spreadsheet Formula Sanitization (Medium Priority):**
   - In `AdminController.java`, prepend an apostrophe (`'`) to exported CSV cells beginning with `=`, `+`, `-`, or `@` to prevent formula injection.
5. **Public Route Whitelist Update (Low Priority):**
   - In `SecurityConfig.java`, add `/features`, `/pricing`, and `/contact` to the public `permitAll()` list so clicking navbar links doesn't trigger a redirect to `/login`.

---

## 4. PHASED ROADMAP FOR SUBSEQUENT PHASES

- **Phase 3:** Gemini Native Structured JSON Mode (`response_schema`) & Prompt Injection Sandboxing.
- **Phase 4:** Python FastAPI ML Microservice (`all-MiniLM-L6-v2`, spaCy NER) & Hybrid ATS Engine (60% Rule + 40% Vector).
- **Phase 5:** Qdrant Vector Search & Grounded RAG Pipeline.
- **Phase 6:** Java Multi-Agent Modules (`CareerAgentOrchestrator.java`, `SkillGapAgent.java`, etc.).
- **Final:** Docker Compose local containerization & GitHub Actions CI/CD.

Always run `mvn test` after making modifications to verify 0 regressions.
```
