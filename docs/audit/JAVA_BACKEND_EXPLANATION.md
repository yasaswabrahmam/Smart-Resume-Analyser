# Java Backend Architecture & Class Reference Manual

## Project: Smart AI Resume Analyzer
**Framework:** Java 21 LTS | Spring Boot 3.3.4 | Spring Security 6.3 | Hibernate 6.5  
**Audit Date:** October 30, 2026  
**Status:** **Comprehensive Class-by-Class Technical Guide**

---

## 1. Controller Layer (`com.smartresume.controller`)

### 1.1 `HomeController.java`
* **File Path:** [`src/main/java/com/smartresume/controller/HomeController.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/controller/HomeController.java)
* **Responsibility:** Serves the preserved landing page, feature showcase, and marketing routes.
* **Key Methods:**
  - `home(Model model)`: Maps to `GET /` and `GET /home`. Injects CSRF token, platform stats, and renders `index.html`.
  - `about()`: Maps to `GET /about`. Renders platform mission.
  - `pricing()`, `features()`, `contact()`: Maps to corresponding marketing paths.
* **Callers:** Spring DispatcherServlet handling anonymous browser traffic.
* **DB / APIs Used:** Read-only access to platform statistics.

---

### 1.2 `AuthController.java`
* **File Path:** [`src/main/java/com/smartresume/controller/AuthController.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/controller/AuthController.java)
* **Responsibility:** User registration and login view dispatching.
* **Key Methods:**
  - `login()`: Maps to `GET /login`. Renders `auth/login.html`.
  - `register(Model model)`: Maps to `GET /register`. Injects empty `UserRegistrationDto`.
  - `handleRegistration(@Valid UserRegistrationDto dto, BindingResult bindingResult)`: Maps to `POST /register`. Validates password matching and invokes `UserService.registerUser()`.
* **Callers:** Unauthenticated users registering or logging in.
* **DB / APIs Used:** `users` table via `UserService`.

---

### 1.3 `ResumeController.java`
* **File Path:** [`src/main/java/com/smartresume/controller/ResumeController.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/controller/ResumeController.java)
* **Responsibility:** Coordinates resume upload, parser triggers, scoring, and analysis display.
* **Key Methods:**
  - `showUpload()`: Maps to `GET /resume/upload`. Renders drag-and-drop form.
  - `handleUpload(MultipartFile file, String targetRole, Principal principal)`: Validates file, invokes `ResumeParserService`, `ResumeAnalyzerService`, `AIResumeAnalyzerService`, persists entities, and redirects to results view.
  - `viewAnalysis(@PathVariable Long id, Principal principal)`: Fetches stored analysis and renders `resume/results.html`.
  - `history(Principal principal)`: Queries all past resumes for the authenticated user.
* **Callers:** Authenticated candidate actions.
* **DB / APIs Used:** Tables `resumes`, `resume_analysis`, `ai_analyses`.

---

### 1.4 `ResumeBuilderController.java`
* **File Path:** [`src/main/java/com/smartresume/controller/ResumeBuilderController.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/controller/ResumeBuilderController.java)
* **Responsibility:** Multi-step dynamic resume builder and binary export streaming.
* **Key Methods:**
  - `builder()`: Maps to `GET /builder`. Renders `builder/form.html`.
  - `preview(@ModelAttribute ResumeBuilderDto dto)`: Renders `builder/preview.html`.
  - `exportPdf(@ModelAttribute ResumeBuilderDto dto, HttpServletResponse response)`: Streams compiled PDF bytes using OpenPDF.
  - `exportDocx(@ModelAttribute ResumeBuilderDto dto, HttpServletResponse response)`: Streams compiled Word document bytes using Apache POI.
* **Callers:** Browser candidate builder.

---

## 2. Service Tier (`com.smartresume.service`)

### 2.1 `ResumeAnalyzerService.java` (ATS Engine)
* **File Path:** [`src/main/java/com/smartresume/service/ResumeAnalyzerService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/ResumeAnalyzerService.java)
* **Responsibility:** Deterministic mathematical ATS scoring engine.
* **Key Methods:**
  - `analyze(String text, String targetRole)`: Evaluates contact info, summary length, keyword match, experience chronology, education degrees, and formatting.
  - `calculateKeywordMatch(String text, List<String> requiredSkills, ResumeAnalysisResult result)`: Performs lowercase containment matching.
  - `checkFormatting(String text, List<String> deductions)`: Checks character thresholds and section heading cases.
* **Inputs/Outputs:** Input: Raw extracted text string + role; Output: `ResumeAnalysisResult` DTO.
* **Caller:** `ResumeController.handleUpload()`.

---

### 2.2 `AIResumeAnalyzerService.java` (Gemini REST Client)
* **File Path:** [`src/main/java/com/smartresume/service/AIResumeAnalyzerService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/AIResumeAnalyzerService.java)
* **Responsibility:** Direct communication with Google Generative Language API and heuristic fallback.
* **Key Methods:**
  - `analyze(...)`: Orchestrates Gemini REST call; falls back to `generateHeuristicAnalysis()` on error.
  - `callGeminiApi(String model, String apiKey, String prompt)`: Executes HTTP/2 POST request using `java.net.http.HttpClient`.
  - `parseAiResponse(String rawText, AIAnalysisResult result)`: Uses regex pattern matchers to extract scores and bullet lists.
* **Inputs/Outputs:** Input: Extracted text, target role, job description; Output: `AIAnalysisResult` DTO.
* **External APIs Used:** Google Generative Language API (`gemini-2.5-flash`).

---

### 2.3 `ResumeParserService.java`
* **File Path:** [`src/main/java/com/smartresume/service/ResumeParserService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/ResumeParserService.java)
* **Responsibility:** Text extraction from binary documents.
* **Key Methods:**
  - `parsePdf(InputStream is)`: Uses Apache PDFBox `PDFTextStripper`.
  - `parseDocx(InputStream is)`: Uses Apache POI `XWPFDocument` to extract paragraphs and tables.

---

### 2.4 `UserService.java` & `CustomUserDetailsService.java`
* **File Paths:**
  - [`src/main/java/com/smartresume/service/UserService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/UserService.java)
  - [`src/main/java/com/smartresume/service/CustomUserDetailsService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/CustomUserDetailsService.java)
* **Responsibility:** Account registration, BCrypt password hashing, Spring Security user principal resolution.

---

## 3. Data Persistence Layer (`com.smartresume.repository` & `model`)

* **7 JPA Entities:** `User`, `Resume`, `ResumeAnalysis`, `AIAnalysis`, `JobMatch`, `Feedback`, `AdminLog`.
* **7 Spring Data JPA Repositories:** Standard interfaces extending `JpaRepository<T, Long>` providing CRUD operations and derived query methods (e.g. `findByUserId()`, `findByEmail()`).
* **Connection Management:** HikariCP connection pool auto-configured via `spring.datasource.*` in `application.properties`.
