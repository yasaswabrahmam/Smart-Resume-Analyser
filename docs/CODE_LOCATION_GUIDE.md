# Code Location Guide

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Framework:** Spring Boot 3.3.4 | Java 21  
**Audit Date:** October 30, 2026  
**Status:** Verified File Location Reference

---

## 1. Java Source Code Map (`src/main/java/com/smartresume/`)

### Application Entry Point & Config
* `SmartResumeAnalyzerApplication.java` — `src/main/java/com/smartresume/SmartResumeAnalyzerApplication.java`: Main Spring Boot application class (`main()`).
* `SecurityConfig.java` — `src/main/java/com/smartresume/config/SecurityConfig.java`: Spring Security filter chain (`filterChain()`), BCrypt password encoder (`passwordEncoder()`).
* `JobRolesConfig.java` — `src/main/java/com/smartresume/config/JobRolesConfig.java`: Static role definitions (`getRoleByName()`, `getAllRoles()`).
* `WebConfig.java` — `src/main/java/com/smartresume/config/WebConfig.java`: Static resource mappings and web MVC configuration.

### Controllers
* `HomeController.java` — `src/main/java/com/smartresume/controller/HomeController.java`: Landing page (`home()`), marketing pages (`about()`, `pricing()`, `features()`, `contact()`).
* `AuthController.java` — `src/main/java/com/smartresume/controller/AuthController.java`: Login view (`login()`), registration view (`register()`), registration submission (`handleRegistration()`).
* `DashboardController.java` — `src/main/java/com/smartresume/controller/DashboardController.java`: Candidate portal (`dashboard()`), profile management (`profile()`).
* `ResumeController.java` — `src/main/java/com/smartresume/controller/ResumeController.java`: Upload view (`showUpload()`), process upload (`handleUpload()`), view analysis (`viewAnalysis()`), history (`history()`).
* `ResumeBuilderController.java` — `src/main/java/com/smartresume/controller/ResumeBuilderController.java`: Builder form (`builder()`), preview (`preview()`), export PDF (`exportPdf()`), export DOCX (`exportDocx()`).
* `JobController.java` — `src/main/java/com/smartresume/controller/JobController.java`: Job listings (`listJobs()`), search (`searchJobs()`).
* `AdminController.java` — `src/main/java/com/smartresume/controller/AdminController.java`: Admin panel (`dashboard()`), user management (`users()`), CSV export (`exportCsv()`).
* `FeedbackController.java` — `src/main/java/com/smartresume/controller/FeedbackController.java`: Feedback form (`showFeedback()`), submit feedback (`submitFeedback()`).

### Services
* `ResumeAnalyzerService.java` — `src/main/java/com/smartresume/service/ResumeAnalyzerService.java`: ATS mathematical calculation (`analyze()`), keyword matching (`calculateKeywordMatch()`), formatting check (`checkFormatting()`).
* `AIResumeAnalyzerService.java` — `src/main/java/com/smartresume/service/AIResumeAnalyzerService.java`: Gemini REST API client (`analyze()`, `callGeminiApi()`, `buildPrompt()`), response parser (`parseAiResponse()`), offline heuristic analyzer (`generateHeuristicAnalysis()`).
* `ResumeParserService.java` — `src/main/java/com/smartresume/service/ResumeParserService.java`: PDF parser via PDFBox (`parsePdf()`), DOCX parser via Apache POI (`parseDocx()`).
* `FileValidationService.java` — `src/main/java/com/smartresume/service/FileValidationService.java`: MIME type and extension validation (`validateFile()`).
* `ResumeBuilderService.java` — `src/main/java/com/smartresume/service/ResumeBuilderService.java`: OpenPDF export (`generatePdf()`), POI Word export (`generateDocx()`).
* `UserService.java` — `src/main/java/com/smartresume/service/UserService.java`: Registration and credential hashing (`registerUser()`).
* `CustomUserDetailsService.java` — `src/main/java/com/smartresume/service/CustomUserDetailsService.java`: Spring Security user loader (`loadUserByUsername()`).
* `JobService.java` — `src/main/java/com/smartresume/service/JobService.java`: Job search and skill matching (`searchJobs()`).
* `AdminService.java` — `src/main/java/com/smartresume/service/AdminService.java`: Telemetry aggregation (`getMetrics()`).
* `FeedbackService.java` — `src/main/java/com/smartresume/service/FeedbackService.java`: Feedback storage (`saveFeedback()`).

### Repositories & Entities
* `UserRepository.java` / `User.java` — `src/main/java/com/smartresume/repository/UserRepository.java` / `model/User.java`
* `ResumeRepository.java` / `Resume.java` — `src/main/java/com/smartresume/repository/ResumeRepository.java` / `model/Resume.java`
* `ResumeAnalysisRepository.java` / `ResumeAnalysis.java` — `src/main/java/com/smartresume/repository/ResumeAnalysisRepository.java` / `model/ResumeAnalysis.java`
* `AIAnalysisRepository.java` / `AIAnalysis.java` — `src/main/java/com/smartresume/repository/AIAnalysisRepository.java` / `model/AIAnalysis.java`
* `JobMatchRepository.java` / `JobMatch.java` — `src/main/java/com/smartresume/repository/JobMatchRepository.java` / `model/JobMatch.java`
* `FeedbackRepository.java` / `Feedback.java` — `src/main/java/com/smartresume/repository/FeedbackRepository.java` / `model/Feedback.java`
* `AdminLogRepository.java` / `AdminLog.java` — `src/main/java/com/smartresume/repository/AdminLogRepository.java` / `model/AdminLog.java`

---

## 2. Frontend & Resource Map (`src/main/resources/`)

### Static Assets
* CSS: `src/main/resources/static/css/main.css` (1,343 lines, dark theme `#0B0F19`, glassmorphic cards, glowing borders).
* JS: `src/main/resources/static/js/main.js` (UI init), `chart-init.js` (Chart.js radar/gauges), `resume-builder.js` (Dynamic form fields).

### Templates (`src/main/resources/templates/`)
* Preserved Landing Page: `templates/index.html`
* Master Layout Shell: `templates/layout/base.html`
* Fragments: `templates/fragments/brand.html`, `navbar.html`, `footer.html`
* Auth: `templates/auth/login.html`, `register.html`
* Dashboard: `templates/dashboard/index.html`, `profile.html`
* Resume Flow: `templates/resume/upload.html`, `results.html`, `history.html`, `view.html`
* Builder: `templates/builder/form.html`, `preview.html`
* Jobs: `templates/jobs/list.html`
* Admin: `templates/admin/dashboard.html`

### Configuration
* `src/main/resources/application.properties`: Server port 8085, MySQL connection, Gemini model identifier.

---

## 3. Test Suites Map (`src/test/java/com/smartresume/`)

* `FileValidationServiceTest.java`: 5 unit tests for MIME, size, extensions.
* `ResumeAnalyzerServiceTest.java`: 4 unit tests for ATS scoring math and bounds.
* `ResumeAnalyzerEdgeCasesTest.java`: 3 unit tests for unicode, empty strings, oversized inputs.
* `ResumeParserServiceTest.java`: 2 unit tests for PDFBox and POI document extraction.
* `ResumeBuilderServiceTest.java`: 3 unit tests for PDF and DOCX binary exports.
* `ResumeBuilderTemplatesTest.java`: 2 unit tests for template HTML mapping.
* `SecurityHardenedTest.java`: 2 unit tests for BCrypt hashing and route protection.
* `SmartResumeAnalyzerApplicationTests.java`: 1 Spring Boot context integration test.
