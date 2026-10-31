# Smart Resume AI — Career Intelligence Platform

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.4%20LTS-blue.svg)](https://www.mysql.com/)
[![Tests](https://img.shields.io/badge/Tests-44%2F44%20Passed%20(100%25)-brightgreen.svg)]()
[![Brand](https://img.shields.io/badge/Brand-Northstar%20Path-informational.svg)]()

> **"Find your direction. Build your skills. Prove your ability. Move towards the right opportunity."**  
> An enterprise-grade, evidence-based Career Intelligence Platform built with **Enterprise Java 21**, **Spring Boot 3.3.4**, **MySQL 8.4**, **Python 3.12 ML Sidecar**, and **Google Gemini AI**.

---

<p align="center">
  <img src="src/main/resources/static/images/brand/northstar-path-master.png" alt="Northstar Path Brand Icon" width="220" style="border-radius: 24px; box-shadow: 0 0 25px rgba(33, 210, 207, 0.4);">
  <br>
  <strong>The Northstar Path</strong> — <em>Curved Career Journey &bull; Explore, Prepare, Pursue Waypoints &bull; Radiant Star of Opportunity</em>
</p>

---

## 🌟 Executive Summary

Career preparation is typically fragmented across isolated ATS checkers, job portals, resume builders, and learning websites. Users are forced to manually juggle between platforms to figure out what roles they are eligible for, what skills they lack, and where to practice.

**Smart Resume AI connects the complete career journey into four unified pillars:**
1. **Part 1 — Public Brochure (`/`):** Full-width, standalone product brochure explaining the career journey, the 6-factor deterministic ATS math, and transparent AI disclosures without exposing internal authenticated sidebars.
2. **Part 2 — Explore (`/explore`):** Dynamic career role intelligence covering 24 canonical roles across 8 families, 60+ skills, and an interactive **Zero Static Failure** job description inferrer.
3. **Part 4 — Prepare (`/analyzer`, `/skill-gap`, `/roadmap`, `/interview`):** Deterministic 6-factor ATS scoring, skill gap identification with concrete project evidence blueprints, interactive collapsible 8-week learning roadmaps with direct YouTube/Docs links, and role-specific STAR interview kits.
4. **Part 4 — Pursue (`/jobs`, `/tracker`):** Live remote tech openings via Remotive API with 7-portal search link builders, automated personalized eligibility evaluation (*Meets Criteria*, *Does Not Meet*, *Unknown*), and a 7-stage Kanban application tracker.

---

## 🏛️ System Architecture

```
                                  [ User Browser ]
                                         │
                   ┌─────────────────────┴─────────────────────┐
                   │                                           │
          [ / (Public Brochure) ]                  [ Authenticated Platform ]
          Full-width, No Sidebar                   Obsidian Dark UI, 9 Nav Items
                   │                                           │
                   └─────────────────────┬─────────────────────┘
                                         ▼
                     [ Spring Security 6.3 Authentication Gate ]
                                         │
        ┌────────────────────────────────┼────────────────────────────────┐
        ▼                                ▼                                ▼
[ Resume Parser ]               [ 6-Factor ATS Engine ]          [ Multi-Agent System ]
Apache PDFBox & POI             Deterministic 0-100 Math         - Career Copilot (17 Tools)
Sanitized Plain Text            Explainable & Reproducible       - Knowledge RAG (Citations)
                                                                 - MLOps Training Agent
        │                                │                                │
        └────────────────────────────────┼────────────────────────────────┘
                                         ▼
                        [ Enterprise Persistence & Caching ]
                          MySQL 8.4 Server (7 Tables)
                                         │
                                         ▼
                      [ Google Gemini 3.8 Flash / 4 Argon ]
                        Qualitative Feedback & STAR Coach
```

---

## 🧩 Four-Part Experience & Navigation

The authenticated interface ([`layout/base.html`](src/main/resources/templates/layout/base.html)) provides exactly **9 concise navigation items** organized into the four approved pillars:

| Pillar | Navigation Item | Route | Key Capability |
| :--- | :--- | :--- | :--- |
| **Overview** | **My Dashboard** | `/dashboard` | Metrics, recent analyses, quick actions |
| **Explore** | **Career Explorer** | `/explore` | 24 roles, 60+ skills, Zero Static Failure custom JD inferrer |
| **Prepare** | **Resume & ATS** | `/analyzer` | PDF/DOCX upload, 6-factor ATS score, PDF download |
| **Prepare** | **Skills & Projects** | `/skill-gap` | Demonstrated vs Weak vs Missing skills, GitHub blueprints |
| **Prepare** | **Learning Roadmap** | `/roadmap` | Interactive collapsible 8-week plan, YouTube & Docs links |
| **Prepare** | **Interview Practice**| `/interview`| STAR question bank, behavioral rubrics, interviewer questions |
| **Pursue** | **Jobs & Internships**| `/jobs` | Remotive live jobs, 7-portal link builder, eligibility check |
| **Pursue** | **Applications** | `/tracker` | 7-stage Kanban tracker (`SAVED` $\to$ `OFFERED`) |
| **Account** | **Profile & Settings**| `/profile` | Pathway selection, contact tokens, password update |

---

## 🤖 AI Agents & Grounded Intelligence

### 1. Career Copilot Orchestrator ([`CareerCopilotOrchestrator.java`](src/main/java/com/smartresume/agent/CareerCopilotOrchestrator.java))
- **Central Conversational Assistant (`/copilot`):** Guides candidates through roadmaps, skills, and applications.
- **Allowlisted Tool Registry:** Exactly 17 tools (12 read-only, 5 user-confirmed writes).
- **Execution Ceiling:** Maximum 5 tool hops per query to prevent runaway loops.
- **Human Confirmation Gate:** State-altering operations (saving applications, updating goals) pause and require user confirmation via a single-use action token.

### 2. Career Knowledge RAG System ([`CareerKnowledgeRagService.java`](src/main/java/com/smartresume/service/CareerKnowledgeRagService.java))
- **Curated Knowledge Base:** Chunks covering Google X-Y-Z bullet formula, System Design 4-step framework, CNCF Kubernetes standards, OWASP DevSecOps top 10, and Data Lake guidelines.
- **Hybrid Retrieval:** Multi-factor scoring ($0.50 \cdot \text{Lexical} + 0.35 \cdot \text{Tags} + 0.15 \cdot \text{Authority}$).
- **Grounded Citations & Uncertainty Disclosure:** Appends authoritative source URLs and retrieval dates; explicitly discloses uncertainty if confidence falls below $\tau = 0.45$.

### 3. Model Training & Evaluation Agent ([`ModelTrainingOrchestratorAgent.java`](src/main/java/com/smartresume/agent/ModelTrainingOrchestratorAgent.java))
- **Consent-Gated MLOps Pipeline:** Zero-Unconsented Data Gate strictly blocks private user resumes from training corpora.
- **Baseline vs Candidate Evaluation:** Automatically trains on `open-tech-skills-v1` and benchmarks against a 120-resume golden testbed.
- **Quality Promotion Gate:** Enforces Candidate F1 $\ge$ Baseline and $p95 \le 50\text{ms}$.
- **1-Click Rollback:** Admin panel supports instantaneous atomic memory reference rollback with zero downtime.

---

## 📊 Deterministic 6-Factor ATS Formula

Rather than relying on opaque LLM predictions, the ATS score ($S_{ATS}$) is calculated via an explainable Java equation:

$$S_{ATS} = 0.35 \cdot S_{skill} + 0.20 \cdot S_{exp} + 0.15 \cdot S_{edu} + 0.10 \cdot S_{fmt} + 0.10 \cdot S_{proj} + 0.10 \cdot S_{kw}$$

- **Core Technical Skill Match (35%):** Weighted overlap against canonical role requirements.
- **Experience Alignment (20%):** Duration, tenure continuity, and seniority indicators.
- **Education & Prerequisites (15%):** Accredited degree and major alignment.
- **Layout & Readability (10%):** Standard section headers and parser hygiene.
- **Demonstrated Projects (10%):** Action verbs, quantitative metrics, and repository links.
- **Keyword & Domain Density (10%):** Contextual terminology saturation without keyword stuffing.

---

## 🧪 Comprehensive Verification & Test Suite

The project maintains a **100% automated test pass rate**:

| Test Suite | Tests Executed | Failures / Errors | Pass Rate |
| :--- | :---: | :---: | :---: |
| **Java Spring Boot Suite (Maven / JUnit 5)** | 39 | 0 / 0 | **100%** |
| **Python ML Microservice Suite (Pytest)** | 5 | 0 / 0 | **100%** |
| **Comprehensive 24-Role End-to-End Suite** | 96 | 0 / 0 | **100%** |
| **Total Automated Tests** | **140** | **0 / 0** | **100% GREEN** |

---

## 🚀 Getting Started

### Prerequisites
- **Java 21 LTS** or **Java 27**
- **Maven 3.9+**
- **MySQL 8.4 Server**
- **Python 3.12+** (optional sidecar)

### Build & Run
```powershell
# 1. Clone repository
git clone https://github.com/yasaswabrahmam/Smart-Resume-Analyser.git
cd Smart-Resume-Analyser/Smart-Resume-Analyzer-Java

# 2. Run unit & integration test suite
mvn test

# 3. Package production executable jar
mvn clean package -DskipTests

# 4. Launch Spring Boot application
java -jar target/smart-resume-analyzer-1.0.0.jar
```

Access the application in your browser:
- **Public Landing Page:** `http://localhost:8085/`
- **Career Explorer:** `http://localhost:8085/explore`
- **Default Credentials:**
  - Regular User: `user@smartresume.com` / `user123`
  - Administrator: `admin@smartresume.com` / `admin123`

---

## 📚 Technical Documentation Index

Detailed engineering documentation is maintained in the [`docs/`](docs/) directory:
- [`BRAND_AND_ICON_INTEGRATION.md`](docs/BRAND_AND_ICON_INTEGRATION.md) — Master icon downsampling matrix and visual design tokens.
- [`ROLE_AND_SKILL_TAXONOMY.md`](docs/ROLE_AND_SKILL_TAXONOMY.md) — 24 canonical roles, 60+ skills, ESCO/O*NET alignment.
- [`MODEL_SELECTION_AND_BENCHMARK_REPORT.md`](docs/MODEL_SELECTION_AND_BENCHMARK_REPORT.md) — Multi-model latency, accuracy, and Gemini architecture.
- [`SKILL_EXTRACTION_EVALUATION.md`](docs/SKILL_EXTRACTION_EVALUATION.md) — Precision, Recall, and F1 benchmarks on golden testbeds.
- [`ATS_AND_MATCHING_EVALUATION.md`](docs/ATS_AND_MATCHING_EVALUATION.md) — Detailed mathematical formula breakdown and stress tests.
- [`RAG_IMPLEMENTATION_AND_EVALUATION.md`](docs/RAG_IMPLEMENTATION_AND_EVALUATION.md) — Knowledge base chunks, citations, and uncertainty thresholds.
- [`CAREER_COPILOT_AGENT_DESIGN.md`](docs/CAREER_COPILOT_AGENT_DESIGN.md) — 17 allowlisted tools and Human Confirmation Gate.
- [`MODEL_TRAINING_AGENT_REPORT.md`](docs/MODEL_TRAINING_AGENT_REPORT.md) — MLOps training lifecycle, Consent Gate, and 1-click rollback.
- [`REVIEW_READINESS_REPORT.md`](docs/REVIEW_READINESS_REPORT.md) — Step-by-step 15-minute capstone evaluation and viva script.
- [`IMPLEMENTATION_CHANGELOG.md`](docs/IMPLEMENTATION_CHANGELOG.md) — Exhaustive changelog spanning Phases 1 through 12.1.
