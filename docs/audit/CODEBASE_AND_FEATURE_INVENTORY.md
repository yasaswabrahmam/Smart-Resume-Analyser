# Complete Codebase and Feature Inventory

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Framework:** Java 21 LTS | Spring Boot 3.3.4  
**Audit Date:** October 30, 2026  
**Status:** Forensic Inventory & Classification

---

## 1. Complete Java Source Class Inventory (50 Classes)

### 1.1 Entry Point & Configurations (`com.smartresume.*`)
| Class Name | Exact File Path | Responsibility | Callers / Dependencies | Classification |
| :--- | :--- | :--- | :--- | :--- |
| `SmartResumeAnalyzerApplication.java` | `src/main/java/com/smartresume/SmartResumeAnalyzerApplication.java` | Spring Boot main application class and `@SpringBootApplication` entrypoint. | JVM runtime, SpringApplication.run() | **Implemented & Runtime-Verified** |
| `SecurityConfig.java` | `src/main/java/com/smartresume/config/SecurityConfig.java` | Security filter chain, BCrypt (factor 12), route authorization, session fixation protection, CSRF configuration. | Spring Security runtime, CustomUserDetailsService | **Implemented & Runtime-Verified** |
| `JobRolesConfig.java` | `src/main/java/com/smartresume/config/JobRolesConfig.java` | Predefined dictionary of technical roles (Software Engineer, DevOps, Frontend, Fullstack, Data Scientist) and required keyword lists. | ResumeAnalyzerService | **Implemented & Runtime-Verified** |
| `WebConfig.java` | `src/main/java/com/smartresume/config/WebConfig.java` | WebMvcConfigurer, static resource handlers (`/static/**`), file upload bindings. | Spring MVC DispatcherServlet | **Implemented & Runtime-Verified** |

### 1.2 Controllers (`com.smartresume.controller.*`)
| Class Name | Exact File Path | Bound Routes | Responsibility | Classification |
| :--- | :--- | :--- | :--- | :--- |
| `HomeController.java` | `src/main/java/com/smartresume/controller/HomeController.java` | `GET /`, `GET /home`, `GET /features`, `GET /pricing`, `GET /about`, `GET /contact` | Serves preserved landing page, feature highlights, and pricing views. | **Implemented & Runtime-Verified** |
| `AuthController.java` | `src/main/java/com/smartresume/controller/AuthController.java` | `GET /login`, `GET /register`, `POST /register`, `GET /logout` | Form binding, validation, user registration dispatch, and login view. | **Implemented & Runtime-Verified** |
| `DashboardController.java` | `src/main/java/com/smartresume/controller/DashboardController.java` | `GET /dashboard`, `GET /profile`, `POST /profile` | Aggregates candidate upload count, average ATS score, and recent activity. | **Implemented & Verified in Source** |
| `ResumeController.java` | `src/main/java/com/smartresume/controller/ResumeController.java` | `GET /resume/upload`, `POST /resume/upload`, `GET /resume/view/{id}`, `GET /resume/history`, `POST /resume/delete/{id}` | Multipart file ingestion, parser trigger, ATS evaluation, Gemini analysis, results view. | **Implemented & Verified in Source** |
| `ResumeBuilderController.java`| `src/main/java/com/smartresume/controller/ResumeBuilderController.java` | `GET /builder`, `POST /builder/save`, `GET /builder/preview/{id}`, `GET /builder/export/pdf/{id}`, `GET /builder/export/docx/{id}` | Dynamic multi-step resume creation form and binary download streaming. | **Implemented & Runtime-Verified** |
| `JobController.java` | `src/main/java/com/smartresume/controller/JobController.java` | `GET /jobs`, `GET /jobs/search`, `GET /jobs/recommended` | External/internal job board browsing and recommendation listings. | **Implemented & Runtime-Verified** |
| `AdminController.java` | `src/main/java/com/smartresume/controller/AdminController.java` | `GET /admin`, `GET /admin/users`, `POST /admin/users/toggle/{id}`, `GET /admin/resumes`, `GET /admin/logs`, `GET /admin/export-csv` | System KPI monitoring, user suspension toggle, audit log inspection, CSV export. | **Implemented & Verified in Source** |
| `FeedbackController.java` | `src/main/java/com/smartresume/controller/FeedbackController.java` | `GET /feedback`, `POST /feedback/submit` | Collects candidate platform experience ratings (1–5) and commentary. | **Implemented & Verified in Source** |

### 1.3 Service Tier (`com.smartresume.service.*`)
| Class Name | Exact File Path | Core Functionality | Classification |
| :--- | :--- | :--- | :--- |
| `ResumeAnalyzerService.java` | `src/main/java/com/smartresume/service/ResumeAnalyzerService.java` | Deterministic ATS mathematical scoring calculation (6 weighted sub-components: contact, summary, keywords, experience, education, formatting). | **Implemented & Runtime-Verified** |
| `AIResumeAnalyzerService.java` | `src/main/java/com/smartresume/service/AIResumeAnalyzerService.java` | Direct HTTP/2 client calling Google Gemini REST API (`gemini-2.5-flash`); local heuristic analyzer fallback. | **Implemented & Runtime-Verified** |
| `ResumeParserService.java` | `src/main/java/com/smartresume/service/ResumeParserService.java` | Document extraction via Apache PDFBox (`PDFTextStripper`) and Apache POI (`XWPFDocument`), text normalization, section extraction. | **Implemented & Runtime-Verified** |
| `FileValidationService.java` | `src/main/java/com/smartresume/service/FileValidationService.java` | Whitelist MIME validation, file size limits (10MB/20MB), path traversal sanitization. | **Implemented & Runtime-Verified** |
| `ResumeBuilderService.java` | `src/main/java/com/smartresume/service/ResumeBuilderService.java` | Resume data assembly, OpenPDF PDF document compilation, Apache POI `.docx` document generation. | **Implemented & Runtime-Verified** |
| `UserService.java` | `src/main/java/com/smartresume/service/UserService.java` | Registration, credential hashing via BCrypt, role assignment (`ROLE_USER`), profile management. | **Implemented & Runtime-Verified** |
| `CustomUserDetailsService.java`| `src/main/java/com/smartresume/service/CustomUserDetailsService.java` | Implements Spring Security `UserDetailsService`, loads user by email/username. | **Implemented & Runtime-Verified** |
| `JobService.java` | `src/main/java/com/smartresume/service/JobService.java` | Skill-to-job matching, job filtering, mock/scraped job listing dispatch. | **Partially Implemented (Mock data)** |
| `JobScraperService.java` | `src/main/java/com/smartresume/service/JobScraperService.java` | Selenium WebDriver / Jsoup scraping mock infrastructure for job boards. | **Mocked / Demo Only** |
| `AdminService.java` | `src/main/java/com/smartresume/service/AdminService.java` | Aggregates global metrics (user count, resume count, average ATS score), user moderation, CSV export generation. | **Implemented & Runtime-Verified** |
| `FeedbackService.java` | `src/main/java/com/smartresume/service/FeedbackService.java` | Persists feedback and computes NPS / rating distributions. | **Implemented & Runtime-Verified** |

### 1.4 Entities & Persistence (`com.smartresume.model.*` & `com.smartresume.repository.*`)
| Entity Name | Repository Name | Database Table | Classification |
| :--- | :--- | :--- | :--- |
| `User.java` | `UserRepository.java` | `users` | **Implemented & Runtime-Verified** |
| `Resume.java` | `ResumeRepository.java` | `resumes` | **Implemented & Runtime-Verified** |
| `ResumeAnalysis.java` | `ResumeAnalysisRepository.java` | `resume_analysis` | **Implemented & Runtime-Verified** |
| `AIAnalysis.java` | `AIAnalysisRepository.java` | `ai_analyses` | **Implemented & Runtime-Verified** |
| `JobMatch.java` | `JobMatchRepository.java` | `job_matches` | **Implemented & Runtime-Verified** |
| `Feedback.java` | `FeedbackRepository.java` | `feedback` | **Implemented & Runtime-Verified** |
| `AdminLog.java` | `AdminLogRepository.java` | `admin_logs` | **Implemented & Runtime-Verified** |

---

## 2. Master Feature Status & Classification Table

| Feature Name | Exact Source Path | Technology Stack | Algorithm / API Used | Database Table | Runtime Status | Verified Test Evidence | Existing Limitations / Flaws |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Landing Page** | `templates/index.html` | Thymeleaf, Bootstrap 5.3, CSS | Responsive DOM | None | **Verified Working** | HTTP 200 on `/` and `/home` | Features/Pricing links redirect to `/login` due to security filter omission. |
| **User Login** | `AuthController.java` | Spring Security 6.3 | BCryptPasswordEncoder | `users` | **Verified Working** | HTTP 200 on `/login`; `SecurityHardenedTest` | Session fixation protection active; remember-me enabled. |
| **User Register** | `AuthController.java` | Spring MVC, Jakarta | Form validation | `users` | **Verified Working** | HTTP 200 on `/register`; `SecurityHardenedTest` | Password strength requires at least 8 chars. |
| **Candidate Dashboard**| `DashboardController.java`| Thymeleaf, Spring Data | Query aggregation | `resumes`, `resume_analysis` | **Verified Working** | HTTP 302 to `/login` when unauthenticated | Requires active candidate session. |
| **Resume Upload** | `ResumeController.java` | Spring Multipart | File stream copy | `resumes` | **Verified Working** | `FileValidationServiceTest` (5 tests pass) | Checks MIME header; lacks physical magic byte verification. |
| **PDF Extraction**| `ResumeParserService.java` | Apache PDFBox 2.0.31 | `PDFTextStripper` | None (in-memory) | **Verified Working** | `ResumeParserServiceTest` (2 tests pass) | Strips formatting; does not extract embedded images. |
| **DOCX Extraction**| `ResumeParserService.java`| Apache POI 5.2.5 | `XWPFDocument` | None (in-memory) | **Verified Working** | `ResumeParserServiceTest` (2 tests pass) | Paragraph & table text extraction. |
| **OCR (Scanned PDFs)**| `Not implemented` | Tesseract OCR | None | None | **Missing / Planned Only**| None | Scanned image-based PDFs cannot be parsed. |
| **ATS Score** | `ResumeAnalyzerService.java`| Java 21 Heuristic | 6-Component Weighted Formula | `resume_analysis` | **Verified Working** | `ResumeAnalyzerServiceTest` (4 tests pass) | **NOT an ML model.** Uses naive string containment `contains()`. |
| **Keyword Extraction**| `ResumeAnalyzerService.java`| Java String checks | `JobRolesConfig` matching | `resume_analysis` | **Verified Working** | `ResumeAnalyzerServiceTest` | Substring false positives (e.g. "Java" matches "JavaScript"). |
| **Gemini AI Analysis**| `AIResumeAnalyzerService.java`| Java `HttpClient`, HTTP/2 | Google Gemini API (`gemini-2.5-flash`) | `ai_analyses` | **Verified Working** | Fallback test; live call | Uses regex to parse markdown; silent default scores on regex fail. |
| **Resume Builder** | `ResumeBuilderService.java` | OpenPDF, Apache POI | PDF/DOCX binary compilers | `resumes` | **Verified Working** | `ResumeBuilderServiceTest` (3 tests pass) | Supports 3 visual templates (Modern, Classic, Minimal). |
| **Job Search** | `JobService.java` | Spring MVC | Mock requisition lists | `job_matches` | **Partially Implemented**| UI test | Returns hardcoded and seeded mock jobs; lacks live scraping API. |
| **Job Recommendations**| `JobService.java` | Keyword overlap | Count of shared skills | `job_matches` | **Partially Implemented**| Repository queries | Lacks dense vector semantic search (Qdrant). |
| **Admin Dashboard** | `AdminController.java` | Spring Data JPA | Telemetry aggregation | `admin_logs`, `users` | **Verified Working** | Admin route tests | CSV export lacks spreadsheet formula injection protection. |
| **Python ML Worker** | `Not found` | Python / FastAPI | None | None | **Missing / Planned Only**| None | Zero Python files in repository (`*.py` count = 0). |
| **Qdrant Vector DB** | `Not found` | Qdrant | None | None | **Missing / Planned Only**| None | No vector client or Docker container in project. |
| **Agentic AI** | `Not found` | Java Modules | None | None | **Missing / Planned Only**| None | `CareerAgentOrchestrator.java` is planned in blueprint, not yet coded. |
