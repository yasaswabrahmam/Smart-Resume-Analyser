# Master Project Continuation Prompt

> [!TIP]
> **Instructions for the User:**
> Copy and paste the entire block below into any future Antigravity or AI coding session. It gives the AI an exact, verified understanding of the codebase so it will never guess, hallucinate non-existent ML models, or break your existing working landing page.

---

```markdown
# CONTEXT & INSTRUCTIONS: SMART RESUME AI CAREER PLATFORM

You are continuing development on the **SMART RESUME AI** project located in the workspace folder `Smart-Resume-Analyzer-Java`.

Before taking any action, review and strictly adhere to these verified facts, architecture rules, and invariants.

---

## 1. CORE REPOSITORY FACTS & RUNTIME TOPOLOGY (VERIFIED OCTOBER 2026)

- **Primary Application Core:** Enterprise Java 21 LTS, Spring Boot 3.3.4, Spring Security 6.3, Spring Data JPA / Hibernate 6.5, Apache Maven 3.9.6.
- **Relational Database:** MySQL 8.4 Server on `127.0.0.1:3306` with database name `smart_resume_db` (7 normalized tables).
- **Active Web Port:** `http://localhost:8085` (Configured in `application.properties`).
- **Verified Test Baseline:** 10 test classes, 24 test methods, 100% pass rate (`mvn test`), with hermetic in-memory H2 test profile.
- **Frontend Stack:** Dual-layout architecture with Thymeleaf 3, Bootstrap 5.3, FontAwesome 6.5, Chart.js 4.4, and custom dark-theme CSS (`src/main/resources/static/css/main.css`).

---

## 2. STRICT INVARIANTS & PRESERVATION RULES

1. **JAVA IS THE MASTER APPLICATION CONTROLLER:**
   - Java and Spring Boot manage web traffic, security, database persistence, ATS scoring, document building, Gemini API calls, and agent orchestration.
   - Under no circumstances should the Java backend be converted into Python.
2. **DUAL-LAYOUT ARCHITECTURAL SEPARATION:**
   - **Public Landing Page (`/` using `layout/public.html`):** Full-width, NO APP SIDEBAR. Contains the public brand navbar, hero ("Your Career Journey, Connected in One Place"), problem vs. solution, 6 target industry roles, 6-factor ATS formula, and 9-stage workflow.
   - **Authenticated Application (`/app` and `/dashboard` using `layout/base.html`):** Accessible after sign-in. Features the dedicated three-tier sidebar: Workspace, Career Acceleration, and Platform Links.
3. **CONNECTED CAREER PIPELINE:**
   - Career navigation is connected across 9 stages:
     Build Resume -> ATS Audit -> Target Role -> Skill Gaps -> YouTube Learning -> Job Discovery -> Apply -> Track Applications -> Interview Prep.
   - Core Services:
     - `SkillGapService.java` (`/skill-gap`): Categorizes skills into Demonstrated, Weak, and Missing; binds curated YouTube tutorials and official docs.
     - `CareerRoadmapService.java` (`/roadmap`): Generates an 8-week structured trajectory plan with weekly deliverables.
     - `ApplicationTrackerService.java` (`/tracker`): Manages pipeline stages (`SAVED`, `APPLIED`, `INTERVIEWING`, `OFFERED`, `REJECTED`) in MySQL `job_matches`.
     - `InterviewPrepService.java` (`/interview`): Generates technical core, system design, and STAR behavioral interview questions.
4. **100% EXPLAINABLE 6-FACTOR ATS SCORING:**
   - ATS score is deterministic and calculated in Java (`ResumeAnalyzerService.java`):
     $$\text{ATS Score} = (S_{\text{keywords}} \times 0.30) + (S_{\text{exp}} \times 0.20) + (S_{\text{fmt}} \times 0.20) + (S_{\text{contact}} \times 0.10) + (S_{\text{summary}} \times 0.10) + (S_{\text{edu}} \times 0.10)$$
   - Skills detected via word-boundary regex (`\b<skill>\b`) to eliminate false substring collisions.
5. **GEMINI & AI PRIVACY:**
   - Google Gemini is called directly in Java via HTTP/2 in `GeminiAiService.java`.
   - Before transmission, candidate phone numbers and email addresses are masked by regex for DPDP Act 2023 / GDPR compliance.
   - Fallback engine activates automatically if the external AI service is unreachable.
6. **PYTHON ML MICROSERVICE SIDECAR:**
   - Resides in `ml-service/` (FastAPI on port 8000) for optional NLP/embedding inference. Java calls it via REST with automatic graceful fallback if offline.

---

## 3. ACTIVE CONTROLLERS & ENDPOINTS

- `/` (`HomeController`): Dedicated public landing page (HTTP 200).
- `/login`, `/register` (`AuthController`): Guest sign-in & onboarding.
- `/app` (`HomeController`): Redirects to `/dashboard`.
- `/dashboard` (`DashboardController`): User metrics & job pipeline status.
- `/analyzer/upload`, `/analyzer/results/{id}` (`ResumeAnalyzerController`): 6-factor ATS audit.
- `/analyzer/ai-coach` (`AIResumeAnalyzerController`): Google X-Y-Z bullet rewrites & qualitative advice.
- `/builder` (`ResumeBuilderController`): 4-template resume builder with PDF export.
- `/skill-gap` (`SkillGapController`): Demonstrated vs. gap analysis + YouTube tutorials.
- `/roadmap` (`CareerRoadmapController`): 8-week career trajectory plan.
- `/jobs/search` (`JobSearchController`): Remotive live jobs + 7-portal deep-link aggregator + Save to Tracker button.
- `/tracker` (`ApplicationTrackerController`): Full job application lifecycle tracker.
- `/interview` (`InterviewPrepController`): Technical, architectural, and STAR questions.
- `/admin` (`AdminController`): Admin metrics & security audit logs.

Always run `mvn test` after making modifications to verify 0 regressions across the 24 test methods.
```
