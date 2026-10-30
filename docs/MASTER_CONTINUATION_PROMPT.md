# Master Project Continuation Prompt

> [!TIP]
> **Instructions for the User:**
> Copy and paste the entire block below into any future Antigravity or AI coding session. It gives the AI an exact, verified understanding of the codebase so it will never guess, hallucinate non-existent ML models, or break your existing working landing page.

---

```markdown
# CONTEXT & INSTRUCTIONS: SMART RESUME AI CAREER INTELLIGENCE PLATFORM

You are continuing development on the **SMART RESUME AI** project located in the workspace folder `Smart-Resume-Analyzer-Java`.
Product Promise: *"Find your direction. Build your skills. Prove your ability. Move towards the right opportunity."*

Before taking any action, review and strictly adhere to these verified facts, architecture rules, and invariants.

---

## 1. CORE REPOSITORY FACTS & RUNTIME TOPOLOGY (VERIFIED OCTOBER 2026)

- **Primary Application Core:** Enterprise Java 27 / 21 LTS, Spring Boot 3.3.4, Spring Security 6.3, Spring Data JPA / Hibernate 6.5, Apache Maven 3.9.9.
- **Relational Database:** MySQL 8.4 Server on `127.0.0.1:3306` with database name `smart_resume_db` (7 normalized tables).
- **Active Web Port:** `http://localhost:8085` (Configured in `application.properties`).
- **Verified Test Baseline:** 14 test suites, 39 test methods, 100% pass rate (`mvn test`), hermetic in-memory H2 test profile. Python ML sidecar: 5/5 tests passed (`pytest`). Total: 44/44 tests green.
- **Frontend Stack:** Dual-layout architecture with Thymeleaf 3, Bootstrap 5.3, FontAwesome 6.5, Chart.js 4.4, and custom obsidian dark-theme CSS (`src/main/resources/static/css/main.css`).
- **Brand Identity:** The Northstar Path (Career Trajectory, 3 Waypoints: Explore, Prepare, Pursue, Radiant 4-pointed Star Apex, Open Platform Orbital Ring). Colors: Midnight Navy (`#071426`), Deep Slate (`#101D31`), Electric Blue (`#356BFA`), Career Cyan (`#21D2CF`), Warm Northstar Gold (`#F4C46A`).

---

## 2. STRICT INVARIANTS & FOUR-PART ARCHITECTURE

1. **JAVA IS THE MASTER APPLICATION CONTROLLER:**
   - Java and Spring Boot manage web traffic, security, database persistence, deterministic ATS scoring, multi-agent orchestration, Career Knowledge RAG retrieval, and Gemini API calls.
   - Do not convert the Java backend into Python.

2. **FOUR-PART PRODUCT EXPERIENCE:**
   - **Part 1 — Public Brochure (`/` using `layout/public.html`):** Full-width, NO APP SIDEBAR. Explains the 10 brochure questions without registration, dual pathways (*Student / Recent Graduate* vs *Experienced Professional*), 6-factor deterministic ATS equation, transparent Model Ledger, and DPDP/GDPR disclosures.
   - **Part 2 — Explore (`/explore`):** Role catalogue spanning 8 families (32+ levels), 60+ canonical skills, dynamic job description parsing (`/explore/infer-role`), and profile pathway configuration (`/profile`).
   - **Part 3 — Prepare:** Deterministic ATS scoring (`/analyzer`), skill gap analysis with Project Evidence Blueprints (`/skill-gap`), adaptive time-budgeted roadmap (`/roadmap`), and STAR interview practice (`/interview`).
   - **Part 4 — Pursue:** Live jobs discovery with Remotive API & 7-portal search links (`/jobs`), personalized 3-tier eligibility checker, and 7-stage application tracker (`/tracker`).

3. **SIDEBAR NAVIGATION (EXACTLY 9 CONCISE LABELS):**
   - In `templates/layout/base.html`, exactly 9 navigation items are shown in three groups:
     1. Explore: (1) My Dashboard, (2) Career Explorer
     2. Prepare: (3) Resume & ATS, (4) Skills & Projects, (5) Learning Roadmap, (6) Interview Practice
     3. Pursue: (7) Jobs & Internships, (8) Applications
     4. Account: (9) Profile & Settings

4. **CAREER COPILOT AGENT (`CareerCopilotOrchestrator.java`):**
   - Central conversational orchestrator (`/copilot`, `POST /copilot/api/chat`).
   - Exactly 17 allowlisted tools (12 read-only, 5 user-confirmed writes).
   - Strict bounded execution budget (maximum 5 tool hops per query).
   - Strict `userId` isolation on all tool executions.
   - Human Confirmation Gate: all 5 state-altering write operations require single-use cryptographic action tokens and modal confirmation.
   - Real-time audit log stream returned in every API response.

5. **CAREER KNOWLEDGE RAG SYSTEM (`CareerKnowledgeRagService.java`):**
   - Curated knowledge base chunks: Google X-Y-Z Bullet Formula, System Design 4-Step Framework, CNCF Kubernetes Standards, OWASP DevSecOps Top 10, Full Stack Blueprint, Data Lake Guidelines.
   - Hybrid retrieval scoring: Lexical keyword overlap (0.50), Semantic tags (0.35), Authority weighting (0.15).
   - Grounded citations: every guidance snippet appends authoritative URLs and retrieval dates.
   - Explicit uncertainty disclosure: triggers warning when retrieval confidence is below 0.45 (zero hallucination).

6. **MODEL TRAINING & EVALUATION AGENT (`ModelTrainingOrchestratorAgent.java`):**
   - DPDP & GDPR Zero-Unconsented Data Gate: Private user resumes are strictly excluded from model training.
   - Registered approved datasets: `open-tech-skills-v1`, `canonical-occupations-2026`, `synthetic-evaluation-testset`.
   - Automated baseline vs candidate evaluation: F1, Precision, Recall, and p95 latency ($\le 50\text{ms}$).
   - 1-Click Rollback: Atomic in-memory pointer swap with zero server restart or downtime.

---

## 3. KEY CONTROLLERS & ENDPOINTS REFERENCE

- `/` (`HomeController`): Public Landing Page Brochure.
- `/explore` (`CareerExplorerController`): Role catalogue, search, filters.
- `/explore/infer-role` (`CareerExplorerController`): POST dynamic JD parser & skill inferrer.
- `/explore/api/role/{roleId}` (`CareerExplorerController`): GET structured role metadata JSON.
- `/copilot` (`CareerCopilotController`): Career Copilot conversational assistant UI.
- `/copilot/api/chat` (`CareerCopilotController`): POST multi-tool agent endpoint (JSON, CSRF ignored).
- `/analyzer`, `/analyzer/analyze` (`ResumeAnalyzerController`): 6-factor deterministic ATS scoring.
- `/skill-gap` (`SkillGapController`): Demonstrated vs Weak vs Missing skills + Project blueprints.
- `/roadmap` (`CareerRoadmapController`): Time-budgeted adaptive learning roadmap.
- `/jobs`, `/jobs/search` (`JobSearchController`): Remotive live API + 7 portal query links.
- `/tracker` (`ApplicationTrackerController`): 7-stage Kanban application tracker.
- `/profile` (`ProfileController`): Student vs Professional career pathway settings.
- `/admin` (`AdminController`): Admin dashboard, MLOps model training, promotion & rollback.
```
