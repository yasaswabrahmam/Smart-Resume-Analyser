# Comprehensive Code Location Guide

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0 (Production Hardened)  
**Framework:** Spring Boot 3.3.4 | Java 21 LTS | Python 3.12 (FastAPI) | MySQL 8.4  
**Audit Date:** October 30, 2026  
**Status:** Complete Reference Architecture & Code Map  

---

## 1. Java Source Code Map (`src/main/java/com/smartresume/`)

### 1.1. Application Entry Point & Configuration
* **`SmartResumeAnalyzerApplication.java`** — `src/main/java/com/smartresume/SmartResumeAnalyzerApplication.java`
  - Main application launcher (`main()`).
* **`SecurityConfig.java`** — `src/main/java/com/smartresume/config/SecurityConfig.java`
  - Configures `SecurityFilterChain`, `BCryptPasswordEncoder` (strength 10), public vs. protected routes, session migration (`migrateSession()`), login processing (`/login`), and logout handling (`/logout`).
* **`DataInitializer.java`** — `src/main/java/com/smartresume/config/DataInitializer.java`
  - Seeds default administrator (`admin@smartresume.com`) and candidate user (`user@smartresume.com`) on startup if absent.
* **`JobRolesConfig.java`** — `src/main/java/com/smartresume/config/JobRolesConfig.java`
  - Defines industry job roles, skill taxonomies, and category mappings.

### 1.2. Authentication & User Management
* **`AuthController.java`** — `src/main/java/com/smartresume/controller/AuthController.java`
  - `loginPage()` (`GET /login`): Renders login view; binds `errorMessage` on failure and `successMessage` on logout/registration.
  - `registerPage()` (`GET /register`): Renders registration view; binds `UserRegistrationDto`.
  - `registerUser()` (`POST /register`): Validates input, hashes password via BCrypt, persists user via `UserService`, and redirects to `/login?registered=true`.
* **`UserService.java`** — `src/main/java/com/smartresume/service/UserService.java`
  - Implements `UserDetailsService`.
  - `loadUserByUsername(String email)`: Loads credentials and granted authorities for Spring Security.
  - `registerUser(UserRegistrationDto dto)`: Performs email uniqueness check, password confirmation verification, and BCrypt encoding.
  - `getCurrentUser()`: Resolves authenticated principal from `SecurityContextHolder`.
* **`UserRegistrationDto.java`** — `src/main/java/com/smartresume/dto/UserRegistrationDto.java`
  - Validation annotations: `@NotBlank`, `@Email`, `@Size(min = 6)`.
* **`User.java`** — `src/main/java/com/smartresume/model/User.java`
  - JPA entity mapped to the `users` table in MySQL (`id`, `email`, `password`, `full_name`, `role`, `created_at`).
* **`UserRepository.java`** — `src/main/java/com/smartresume/repository/UserRepository.java`
  - Spring Data JPA interface: `findByEmail(String email)`, `existsByEmail(String email)`.

### 1.3. Web & REST Controllers
* **`HomeController.java`** — `src/main/java/com/smartresume/controller/HomeController.java`: Landing page (`GET /`, `GET /home`), `GET /about`.
* **`ResumeController.java`** — `src/main/java/com/smartresume/controller/ResumeController.java`: Algorithmic ATS analyzer (`GET /analyzer`, `POST /analyzer/analyze`, `GET /analyzer/view/{id}`).
* **`AIResumeController.java`** — `src/main/java/com/smartresume/controller/AIResumeController.java`: Google Gemini analyzer (`GET /ai-analyzer`, `POST /ai-analyzer/analyze`, `GET /ai-analyzer/download-pdf/{id}`).
* **`ResumeBuilderController.java`** — `src/main/java/com/smartresume/controller/ResumeBuilderController.java`: 4-template resume builder (`GET /builder`, `POST /builder/export/pdf`).
* **`JobController.java`** — `src/main/java/com/smartresume/controller/JobController.java`: 7-portal job aggregator (`GET /jobs`).
* **`DashboardController.java`** — `src/main/java/com/smartresume/controller/DashboardController.java`: Candidate portal (`GET /dashboard`).
* **`AdminController.java`** — `src/main/java/com/smartresume/controller/AdminController.java`: Admin analytics & telemetry (`GET /admin`).
* **`FeedbackController.java`** — `src/main/java/com/smartresume/controller/FeedbackController.java`: Platform feedback (`GET /feedback`, `POST /feedback`).

### 1.4. Multi-Agent Orchestration Package (`com.smartresume.agent`)
* **`CareerAgentOrchestrator.java`**: Coordinates multi-agent pipeline and aggregates results.
* **`ResumeAnalysisAgent.java`**: Analyzes parsing structure, formatting, and ATS score.
* **`SkillGapAgent.java`**: Compares candidate skills against job requirements.
* **`JobRecommendationAgent.java`**: Formulates targeted live search queries across 7 job portals.
* **`CareerGuidanceAgent.java`**: Generates step-by-step career development roadmaps.
* **`ResumeGenerationAgent.java`**: Re-writes bullet points with strong action verbs.
* **`InterviewPreparationAgent.java`**: Formulates targeted technical and situational interview questions.

### 1.5. Services & Integration Clients
* **`ResumeAnalyzerService.java`**: Deterministic 6-factor ATS scoring equation and regex word-boundary keyword matching.
* **`AIResumeAnalyzerService.java`**: Direct Java HTTP/2 client for Google Gemini (`gemini-2.5-flash`), PII masking (`maskPii`), and local heuristic fallback.
* **`PythonMlClientService.java`**: REST client consuming the Python FastAPI ML microservice with 3.0s timeout and circuit breaker fallback.
* **`ResumeParserService.java`**: Multi-format document parser (Apache PDFBox + Apache POI) with PII redaction pipeline.
* **`FileValidationService.java`**: Physical stream magic byte validation (`%PDF-`, `PK\x03\x04`).
* **`FileStorageService.java`**: Safe file persistence with directory traversal protection.
* **`ExportService.java`**: CSV export with CWE-1236 spreadsheet formula injection neutralization.
* **`ResumeBuilderService.java`**: Compiles resume drafts into PDF documents using Flying Saucer / OpenPDF.

### 1.6. Entities & Repositories (MySQL Relational Schema)
* **`User` / `UserRepository`**: Table `users`
* **`Resume` / `ResumeRepository`**: Table `resumes`
* **`ResumeAnalysis` / `ResumeAnalysisRepository`**: Table `resume_analysis`
* **`AIAnalysis` / `AIAnalysisRepository`**: Table `ai_analyses`
* **`JobMatch` / `JobMatchRepository`**: Table `job_matches`
* **`Feedback` / `FeedbackRepository`**: Table `feedback`
* **`AdminLog` / `AdminLogRepository`**: Table `admin_logs`

---

## 2. Python ML Microservice Map (`ml-service/`)

* **`ml-service/main.py`**: Production FastAPI application exposing:
  - `GET /health`: Health status and model readiness.
  - `POST /api/v1/ml/extract-entities`: Extracts technical skills and years of experience.
  - `POST /api/v1/ml/semantic-score`: Computes Sentence-Transformers cosine similarity.
  - `POST /api/v1/ml/classify-role`: TF-IDF role classification.
  - `POST /api/v1/ml/embed`: Generates 384-dimensional dense vectors for RAG.
* **`ml-service/requirements.txt`**: FastAPI, Uvicorn, Pydantic v2, Scikit-learn, Sentence-Transformers, Pytest, NumPy, Pandas.
* **`ml-service/test_ml_service.py`**: 5 unit tests for FastAPI endpoints (100% pass rate).

---

## 3. Frontend & Resource Map (`src/main/resources/`)

### 3.1. Static Assets
* **CSS:** [`src/main/resources/static/css/main.css`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/resources/static/css/main.css) (1,343 lines, dark theme `#0B0F19`, glassmorphic cards, glowing borders).
* **JS:** [`src/main/resources/static/js/main.js`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/resources/static/js/main.js) (UI interactivity, file drag-and-drop, flash alert auto-dismiss).

### 3.2. Thymeleaf Templates (`src/main/resources/templates/`)
* **Landing Page:** `templates/index.html` (Preserved dark-mode home view)
* **Layout:** `templates/layout/base.html` (Master wrapper with flash messages and responsive sidebar)
* **Brand Fragments:** `templates/fragments/brand.html` (3D animated brand icon)
* **Authentication:**
  - Login: `templates/auth/login.html` (Glassmorphic card, demo credentials autofill)
  - Register: `templates/auth/register.html` (Form with validation error badges)
* **Dashboard:** `templates/dashboard/index.html` (Candidate telemetry and history)
* **Resume Analyzer:** `templates/analyzer/index.html` (Upload form), `templates/analyzer/results.html` (ATS breakdown)
* **AI Gemini Analyzer:** `templates/analyzer/ai-index.html`, `templates/analyzer/ai-results.html`
* **Resume Builder:** `templates/builder/index.html` (Dynamic multi-section builder), `templates/builder/preview.html`
* **Job Aggregator:** `templates/jobs/index.html` (7-portal query builder)
* **Admin Console:** `templates/dashboard/admin.html` (Audit logs and platform statistics)
* **About Page:** `templates/about.html` (Architecture, privacy, and technology documentation)

---

## 4. Test Suite Map (`src/test/java/com/smartresume/`)

* **`CareerAgentOrchestratorTest.java`**: Validates the Java multi-agent orchestrator execution graph.
* **`FileValidationServiceTest.java`**: 7 unit tests for magic bytes (`%PDF-`, `PK\x03\x04`), MIME types, file size limits, and binary cloaking defense.
* **`PythonMlClientServiceTest.java`**: 1 unit test validating circuit breaker fallback when Python ML service is offline.
* **`ResumeAnalyzerServiceTest.java`**: 2 unit tests verifying 6-factor ATS scoring mathematics and weight balance.
* **`ResumeAnalyzerEdgeCasesTest.java`**: 4 unit tests verifying regex word boundary matching (`\b<skill>\b`), empty text, and special characters.
* **`ResumeBuilderServiceTest.java`**: 1 unit test for PDF compilation.
* **`ResumeBuilderTemplatesTest.java`**: 3 unit tests verifying HTML template rendering across Modern, Classic, and Creative layouts.
* **`ResumeParserServiceTest.java`**: 1 unit test for PDFBox/POI parsing and automated PII masking (`maskPii`).
* **`SecurityHardenedTest.java`**: 3 unit tests for BCrypt password hashing, CSRF protection, and security headers.
* **`SmartResumeAnalyzerApplicationTests.java`**: 1 integration test verifying Spring application context bootstrap using isolated H2 database (`application-test.properties`).
