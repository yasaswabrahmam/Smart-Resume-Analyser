# Current Application Overview

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Core Technologies:** Java 21 LTS | Spring Boot 3.3.4 | MySQL 8.4 | Thymeleaf 3  
**Audit Date:** October 30, 2026  
**Status:** **Forensic Architecture Overview of Existing Baseline**

---

## 1. What This Application Does

The **Smart AI Resume Analyzer** is an enterprise-grade Java web application engineered to solve a widespread problem in technical hiring: **automated candidate rejection by Applicant Tracking Systems (ATS)**.

### The Problem It Solves
* **The ATS "Black Hole":** Over 75% of candidate resumes submitted to corporate portals (e.g., Amazon, Google, Fortune 500) are automatically filtered out before a human recruiter ever sees them. Rejections typically occur due to missing domain keywords, unrecognized section headers, or incompatible document formatting.
* **Lack of Actionable Feedback:** Job seekers are rarely told *why* their resume failed automated screening.
* **Commercial Paywalls:** Many commercial resume optimization tools charge subscription fees for basic ATS checks.

### The Platform's Solution
* **Free & Instant ATS Diagnostic:** Candidates upload a resume (`.pdf` or `.docx`) and select their target job title.
* **Deterministic ATS Scoring:** Evaluates contact details, summary length, keyword match, experience chronology, degree credentials, and document formatting on a transparent $[0, 100]$ scale.
* **Contextual Generative AI Feedback:** Google Gemini evaluates qualitative strengths, weakness areas, and course recommendations.
* **Interactive Dynamic Resume Builder:** Allows candidates to create ATS-friendly resumes from scratch and export them directly to `.pdf` and `.docx`.
* **Zero Auto-Rejection Mandate:** The platform acts exclusively as a candidate intelligence and coaching tool.

---

## 2. Real Implementation Stack (What Actually Exists)

```
+-----------------------------------------------------------------------------------+
| TIER                       | IMPLEMENTED TECHNOLOGY (VERIFIED IN CODEBASE)        |
+----------------------------+------------------------------------------------------+
| Primary Language           | Java 21 LTS (OpenJDK / Microsoft Build of OpenJDK)   |
| Backend Framework          | Spring Boot 3.3.4 (Tomcat 10.1.28 embedded servlet)  |
| Security & Identity        | Spring Security 6.3.3 (BCrypt strength 12, CSRF)     |
| Relational Persistence     | Spring Data JPA / Hibernate ORM 6.5.2.Final          |
| Relational Database Engine | MySQL 8.4 Server (Listening on port 3306)            |
| Connection Pool            | HikariCP 5.1.0 (10 active connections)               |
| Document Parsing           | Apache PDFBox 2.0.31 & Apache POI 5.2.5              |
| Binary Export Generation   | OpenPDF 1.3.39 & Apache POI 5.2.5                    |
| LLM Integration            | Google Gemini (gemini-2.5-flash) via Java HttpClient |
| Frontend Templates         | Thymeleaf 3.1.2.RELEASE (15 server-rendered views)   |
| Client UI & Styling        | Bootstrap 5.3.2, FontAwesome 6, Chart.js 4.4         |
| Master Design System       | CSS3 Custom Properties (1,343 lines in main.css)     |
| Build & Dependency Tool    | Apache Maven 3.9.6                                   |
| Automated Test Suites      | JUnit Jupiter 5.10.3, AssertJ 3.25.3, Mockito 5.11.0 |
+-----------------------------------------------------------------------------------+
```

---

## 3. Communication Across Subsystems

```
[Candidate Browser]
       │
       │ HTTP/HTTPS Requests (Port 8085)
       ▼
+───────────────────────────────────────────────────────────────────────────────────+
| SPRING BOOT 3.3.4 BACKEND (JVM PID 29000)                                         |
|                                                                                   |
| 1. SecurityFilter: Intercepts requests, validates CSRF tokens, verifies BCrypt    |
| 2. Controllers: Dispatches requests to Home, Auth, Resume, Builder, Admin         |
| 3. Service Layer: Coordinates parsing, rule calculations, and document assembly   |
| 4. Repositories: Mediates entity transactions via Hibernate 6.5.2                |
+───────────────────────────────────────────────────────────────────────────────────+
       │                                                     │
       │ JDBC / SQL (Port 3306)                              │ Direct HTTPS (HTTP/2)
       ▼                                                     ▼
+──────────────────────────────────────+    +───────────────────────────────────────+
| MYSQL 8.4 DATABASE (PID 7672)        |    | GOOGLE GEMINI CLOUD API               |
| Database: `resume_analyzer`          |    | Endpoint: generativelanguage...       |
| Tables: users, resumes,              |    | Model: gemini-2.5-flash               |
| resume_analysis, ai_analyses,        |    | Payload: Structured Prompt & Resume   |
| job_matches, feedback, admin_logs    |    +───────────────────────────────────────+
+──────────────────────────────────────+
```

---

## 4. Lifecycle: Opening the Web Application

1. **Bootstrap / Startup:**
   - Spring Boot initializes on port `8085`.
   - HikariCP pool establishes 10 connections to MySQL on `localhost:3306`.
   - Hibernate auto-validates and synchronizes table schemas (`ddl-auto=update`).
   - Spring Security registers the security filter chain and configures session fixation protection.
2. **User Navigates to `http://localhost:8085`:**
   - Request hits `HomeController.home()`.
   - Thymeleaf resolves `src/main/resources/templates/index.html` inside `layout/base.html`.
   - Browser renders the preserved dark-mode landing page, animated 3D SVG emblem, and live ATS preview widget.
3. **User Authentication Flow (`/login` or `/register`):**
   - User submits registration payload (`POST /register`).
   - `UserService.registerUser()` hashes password using `BCryptPasswordEncoder(12)`.
   - Record is inserted into `users` table.
   - Upon logging in (`POST /login`), Spring Security verifies credentials against the database, migrates the session ID to prevent session fixation, sets `JSESSIONID` cookie (`HttpOnly`), and redirects the candidate to `/dashboard`.
