# Code Location Guide

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Framework:** Spring Boot 3.3.4 | Java 21  
**Audit Date:** October 2026  
**Status:** Phase 1 Baseline Architecture Map

---

## 1. Directory Structure Overview

```
Smart-Resume-Analyzer-Java/
├── pom.xml                                      # Maven Project Object Model & Dependencies
├── src/
│   ├── main/
│   │   ├── java/com/smartresume/
│   │   │   ├── SmartResumeAnalyzerApplication.java # Spring Boot Main Entrypoint
│   │   │   ├── config/                          # Security, Web, and Domain Dictionaries
│   │   │   ├── controller/                      # MVC & REST Endpoints
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   ├── model/                           # JPA Hibernate Entities
│   │   │   ├── repository/                      # Spring Data JPA Repositories
│   │   │   └── service/                         # Business Logic & Integrations
│   │   └── resources/
│   │       ├── application.properties           # Server, DB, Hikari, & API Config
│   │       ├── static/
│   │       │   ├── css/main.css                 # Master Design System (1,343 lines)
│   │       │   └── js/                          # Client-side validation & interactivity
│   │       └── templates/                       # Thymeleaf Server-Side Rendered Views
│   └── test/
│       └── java/com/smartresume/                # 8 Test Suites (22 Test Methods)
└── docs/audit/                                  # Phase 1 Technical Audit Reports
```

---

## 2. Java Backend Tiers

### 2.1 Configuration Layer (`com.smartresume.config`)
| Class Name | Exact File Path | Responsibility & Key Methods |
| :--- | :--- | :--- |
| `SecurityConfig.java` | `src/main/java/com/smartresume/config/SecurityConfig.java` | SecurityFilterChain, BCryptPasswordEncoder (strength 12), route permit/authorize rules, session fixation protection, CSRF configuration, custom login/logout handlers. |
| `JobRolesConfig.java` | `src/main/java/com/smartresume/config/JobRolesConfig.java` | Static reference dictionary containing predefined tech keywords, domains (Software Engineer, Data Scientist, DevOps, Frontend, Fullstack), and essential skills. |
| `WebConfig.java` | `src/main/java/com/smartresume/config/WebConfig.java` | WebMvcConfigurer, resource handler mappings for `/static/**`, file upload multipart resolver bindings. |

### 2.2 Controller Layer (`com.smartresume.controller`)
| Controller Name | Exact File Path | Bound Endpoints / Routes | Description |
| :--- | :--- | :--- | :--- |
| `HomeController.java` | `src/main/java/com/smartresume/controller/HomeController.java` | `GET /`, `GET /home`, `GET /features`, `GET /pricing`, `GET /about`, `GET /contact` | Serves preserved high-converting landing page and marketing routes. |
| `AuthController.java` | `src/main/java/com/smartresume/controller/AuthController.java` | `GET /login`, `GET /register`, `POST /register`, `GET /logout` | Handles authentication views, registration payload binding, and validation. |
| `DashboardController.java` | `src/main/java/com/smartresume/controller/DashboardController.java` | `GET /dashboard`, `GET /profile`, `POST /profile/update` | Renders user command center, recent resume activity, and profile stats. |
| `ResumeController.java` | `src/main/java/com/smartresume/controller/ResumeController.java` | `GET /resume/upload`, `POST /resume/upload`, `GET /resume/view/{id}`, `GET /resume/history`, `POST /resume/delete/{id}` | File ingestion, triggering parser, scoring, and rendering detailed analysis results. |
| `ResumeBuilderController.java`| `src/main/java/com/smartresume/controller/ResumeBuilderController.java` | `GET /builder`, `POST /builder/save`, `GET /builder/preview/{id}`, `GET /builder/export/pdf/{id}`, `GET /builder/export/docx/{id}` | Multi-step interactive resume builder and binary export downloads. |
| `JobController.java` | `src/main/java/com/smartresume/controller/JobController.java` | `GET /jobs`, `GET /jobs/search`, `GET /jobs/recommended` | External/internal job board browsing and recommendation listings. |
| `AdminController.java` | `src/main/java/com/smartresume/controller/AdminController.java` | `GET /admin`, `GET /admin/users`, `POST /admin/users/toggle/{id}`, `GET /admin/resumes`, `GET /admin/logs`, `GET /admin/export-csv` | System administration dashboard, user state toggling, and audit log inspection. |
| `FeedbackController.java` | `src/main/java/com/smartresume/controller/FeedbackController.java` | `GET /feedback`, `POST /feedback/submit` | Collects candidate platform experience reviews and ratings. |

### 2.3 Service Layer (`com.smartresume.service`)
| Service Name | Exact File Path | Core Functionality |
| :--- | :--- | :--- |
| `ResumeAnalyzerService.java` | `src/main/java/com/smartresume/service/ResumeAnalyzerService.java` | Deterministic ATS mathematical scoring calculation (formula with 6 weighted sub-components: contact, summary, keywords, experience, education, formatting). |
| `AIResumeAnalyzerService.java` | `src/main/java/com/smartresume/service/AIResumeAnalyzerService.java` | Google Gemini API client integration (`generateContent`), fallback heuristic AI generator, structured analysis extraction. |
| `ResumeParserService.java` | `src/main/java/com/smartresume/service/ResumeParserService.java` | Document extraction via Apache PDFBox (`PDFTextStripper`) and Apache POI (`XWPFDocument`), text normalization, section extraction. |
| `FileValidationService.java` | `src/main/java/com/smartresume/service/FileValidationService.java` | Whitelist MIME validation, file size limits (10MB), magic byte verification for PDF and DOCX, path traversal sanitization. |
| `ResumeBuilderService.java` | `src/main/java/com/smartresume/service/ResumeBuilderService.java` | Resume assembly, OpenPDF PDF document compilation, Apache POI `.docx` document generation. |
| `UserService.java` | `src/main/java/com/smartresume/service/UserService.java` | Registration, credential hashing via BCrypt, role assignment (`ROLE_USER`), profile management. |
| `CustomUserDetailsService.java`| `src/main/java/com/smartresume/service/CustomUserDetailsService.java` | Implements Spring Security `UserDetailsService`, loads user by email/username. |
| `JobService.java` | `src/main/java/com/smartresume/service/JobService.java` | Skill-to-job matching, job filtering, mock/scraped job listing dispatch. |
| `JobScraperService.java` | `src/main/java/com/smartresume/service/JobScraperService.java` | Selenium WebDriver / Jsoup scraping mock infrastructure for job boards. |
| `AdminService.java` | `src/main/java/com/smartresume/service/AdminService.java` | Aggregates global metrics (user count, resume count, average ATS score), user moderation, CSV export generation. |
| `FeedbackService.java` | `src/main/java/com/smartresume/service/FeedbackService.java` | Persists feedback and computes NPS / rating distributions. |

### 2.4 Data Access & Persistence (`com.smartresume.model` & `com.smartresume.repository`)
| Entity Model | Repository Interface | Database Table | Purpose |
| :--- | :--- | :--- | :--- |
| `User.java` | `UserRepository.java` | `users` | Candidate accounts, hashed passwords, roles (`ROLE_USER`, `ROLE_ADMIN`), enabled status. |
| `Resume.java` | `ResumeRepository.java` | `resumes` | Uploaded resume metadata, raw extracted text, parsed section JSON, storage paths. |
| `ResumeAnalysis.java` | `ResumeAnalysisRepository.java` | `resume_analyses` | ATS scoring breakdown, keyword matches, missing skills, improvement action items. |
| `AIAnalysis.java` | `AIAnalysisRepository.java` | `ai_analyses` | Gemini LLM generated feedback, tone appraisal, executive summary, recommended certifications. |
| `JobMatch.java` | `JobMatchRepository.java` | `job_matches` | Matched job requisitions, match percentage, company metadata, external application link. |
| `Feedback.java` | `FeedbackRepository.java` | `feedbacks` | User rating (1–5 stars), subjective comments, timestamp. |
| `AdminLog.java` | `AdminLogRepository.java` | `admin_logs` | Security & administrative audit trail (actions, actor ID, client IP, timestamp). |

### 2.5 Data Transfer Objects (`com.smartresume.dto`)
- `UserRegistrationDto.java`: Validated registration form payload (`fullName`, `email`, `password`, `confirmPassword`).
- `ResumeAnalysisDto.java`: Aggregated view model for rendering ATS score dial, sub-scores, radar charts, and suggestions.
- `AIAnalysisDto.java`: Gemini LLM response container (strengths, weaknesses, career roadmap, role-readiness score).
- `ResumeBuilderDto.java`: Comprehensive multi-field builder form (personal info, experiences, education, skills, projects, certifications).
- `JobSearchDto.java`: Job search filter criteria (query, location, role, min ATS match).
- `FeedbackDto.java`: Rating and feedback text submission model.

---

## 3. Frontend Tier & Templates

### 3.1 Design System & Static Assets
- **Main CSS:** `src/main/resources/static/css/main.css` (1,343 lines)
  - Color Palette: Primary dark background (`#0B0F19`), card surface (`#141A23`, `#1A2232`), borders (`rgba(255, 255, 255, 0.08)`), accents (Indigo `#6366F1`, Violet `#8B5CF6`, Emerald `#10B981`, Amber `#F59E0B`, Crimson `#EF4444`).
  - Typography: Google Fonts `Inter` (body copy) and `Poppins` (headings/metrics).
  - Modern UI elements: Glassmorphic backdrops, glow effects, radial gradient hero backgrounds, responsive flex/grid layouts.
- **Client JavaScript:**
  - `src/main/resources/static/js/main.js`: Dark mode initialization, navbar scroll listeners, alert auto-dismissal, tooltips.
  - `src/main/resources/static/js/chart-init.js`: Chart.js radar charts and circular score gauges for ATS breakdown.
  - `src/main/resources/static/js/resume-builder.js`: Dynamic form field add/remove repeater for experience and skill tags.

### 3.2 Thymeleaf Template Mapping
| Template File Path | Route | Purpose & Visual Elements |
| :--- | :--- | :--- |
| `templates/index.html` | `/`, `/home` | **CRITICAL PRESERVED LANDING PAGE**. Hero section with animated gradient badges, feature cards, live stats ticker, interactive ATS preview widget, CTA buttons. |
| `templates/layout/base.html` | Shared layout | Master wrapper providing HTML `<head>`, meta tags, Bootstrap 5.3, FontAwesome 6, custom CSS/JS imports, and standard layout fragment slots. |
| `templates/fragments/brand.html` | Header/Footer | 3D-styled animated SVG emblem and brand typography ("SmartResume AI"). |
| `templates/fragments/navbar.html` | Shared nav | Responsive top navigation with authenticated vs. anonymous conditional links. |
| `templates/fragments/footer.html` | Shared footer | Links, platform disclaimer, copyright, and social links. |
| `templates/auth/login.html` | `/login` | Centered glassmorphic card for credential authentication with remember-me. |
| `templates/auth/register.html` | `/register` | User signup form with real-time password strength indicator and validation. |
| `templates/dashboard/index.html` | `/dashboard` | User dashboard displaying recent uploads, ATS score progression, and quick upload CTA. |
| `templates/dashboard/profile.html`| `/profile` | Candidate profile details, career preferences, target job roles. |
| `templates/resume/upload.html` | `/resume/upload` | Drag-and-drop file upload zone supporting `.pdf` and `.docx` up to 10MB. |
| `templates/resume/results.html` | `/resume/view/{id}`| Analysis view: Circular ATS gauge, keyword breakdown, missing skills tags, Gemini recommendations. |
| `templates/resume/history.html` | `/resume/history`| Historical table of uploaded resumes with date, ATS score, target role, and action buttons. |
| `templates/builder/form.html` | `/builder` | Multi-step resume builder form (experience, education, skills, projects). |
| `templates/builder/preview.html` | `/builder/preview/{id}`| Clean printable HTML preview with export to PDF / DOCX buttons. |
| `templates/jobs/list.html` | `/jobs` | Job search results grid with ATS alignment score badge per job card. |
| `templates/admin/dashboard.html` | `/admin` | Admin dashboard with KPI metrics, user management table, audit logs, and CSV download. |

---

## 4. Test Suites

All tests are located under `src/test/java/com/smartresume/`:
1. `FileValidationServiceTest.java`: 5 unit tests validating MIME types, extensions, size thresholds, and corrupt inputs.
2. `ResumeAnalyzerServiceTest.java`: 4 unit tests validating the ATS formula and mathematical sub-score bounds.
3. `ResumeAnalyzerEdgeCasesTest.java`: 3 unit tests testing empty strings, special characters, and oversized resumes.
4. `ResumeParserServiceTest.java`: 2 unit tests verifying Apache PDFBox and POI extraction fidelity.
5. `ResumeBuilderServiceTest.java`: 3 unit tests verifying OpenPDF and POI export generation.
6. `ResumeBuilderTemplatesTest.java`: 2 unit tests validating builder template schema mapping.
7. `SecurityHardenedTest.java`: 2 unit tests verifying password hashing and unauthenticated route protection.
8. `SmartResumeAnalyzerApplicationTests.java`: 1 integration test verifying Spring Boot context loading.

---

## 5. Configuration & Build Manifests

- **Maven POM:** `pom.xml` (Spring Boot Starter 3.3.4, Java 21 compiler source/target, Jackson, PDFBox, POI, OpenPDF, Selenium).
- **Application Configuration:** `src/main/resources/application.properties` (Server port `8085`, MySQL datasource credentials, HikariCP pool configuration, Gemini API key placeholder, upload multipart limits).
