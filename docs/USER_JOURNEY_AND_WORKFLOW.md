# User Journey & Connected Career Workflow

**Smart Resume AI — Experience Walkthrough**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Overview: The 9-Stage Connected Journey

Smart Resume AI transforms resume evaluation from a disconnected one-off score into a continuous 9-stage career acceleration loop:

```
[1. Discover & Register]
         │
         ▼
[2. Upload / Build Resume]
         │
         ▼
[3. Algorithmic ATS Audit]
         │
         ▼
[4. Identify Skill Gaps]
         │
         ▼
[5. Stream YouTube & Courses]
         │
         ▼
[6. Search 7 Job Portals]
         │
         ▼
[7. Apply on Official Links]
         │
         ▼
[8. Track Applications]
         │
         ▼
[9. Interview Preparation]
```

---

## 2. Detailed Step-by-Step Walkthrough

### Stage 1: Discovery & Registration
1. **Public Visitor**: Arrives at `http://localhost:8085/`.
2. **Landing Experience**:
   - Sees the full-width landing page (no sidebar).
   - Reads the core value proposition: *"Your Career Journey, Connected in One Place."*
   - Explores target roles (Java Developer, Python ML Engineer, Cloud DevOps).
   - Inspects the transparent 6-factor ATS formula.
3. **Registration**:
   - Visitor clicks `Get Started Free` (`/register`).
   - Enters full name, email, password, and selects target career role.
   - Upon registration, authenticated session is established, and user is routed to `/dashboard`.

---

### Stage 2: Resume Ingestion & Creation
Users have two entry paths into the platform:
- **Path A: Upload Existing Resume (`/analyzer/upload`)**:
  - Drag-and-drop PDF or DOCX file (up to $10\text{MB}$).
  - Tika / PDFBox extracts plain text.
  - Client-side and server-side DPDP regex masks PII (email, phone, national IDs).
- **Path B: Interactive Resume Builder (`/builder`)**:
  - User fills out personal info, summary, work experiences, education, and technical skills.
  - Chooses from 4 ATS-compliant templates: Modern, Classic, Minimalist, Creative.
  - Real-time live preview.
  - Generates downloadable PDF via Flying Saucer / iText.

---

### Stage 3: Algorithmic ATS Scoring & Analysis
1. User provides or selects a target Job Description.
2. `ResumeAnalyzerService.java` executes the deterministic 6-factor algorithm:
   - **Keyword Match ($30\%$)**: Word-boundary regex matching prevents false substring hits (e.g. `Java` vs `JavaScript`).
   - **Work Experience ($20\%$)**: Analyzes tenure, bullet point counts, and Google X-Y-Z active verb presence.
   - **Formatting Density ($20\%$)**: Checks word count (450–1,200), readable headers, and formatting artifacts.
   - **Contact Completeness ($10\%$)**: Verifies email, phone number, LinkedIn, and GitHub/Portfolio URLs.
   - **Professional Summary ($10\%$)**: Validates presence of targeted summary statement.
   - **Education ($10\%$)**: Verifies degree level and accreditation tokens.
3. Results Page (`/analyzer/results/{id}`):
   - Overall ATS score meter ($0-100$).
   - Categorized skills: Matched vs Missing.
   - Section-by-section audit grades with specific improvement advice.
   - Prominent **Next Steps Banner**: Links directly to *Skill Gaps & YouTube Hub* and *8-Week Roadmap*.

---

### Stage 4: Skill Gap Categorization
User clicks into `/skill-gap`:
1. System analyzes the resume text against the target job role.
2. Skills are partitioned into three explicit buckets:
   - **Demonstrated Skills (Green)**: Verified with concrete context and project deliverables.
   - **Weakly Evidenced Skills (Yellow)**: Listed as a keyword or isolated mention without quantifiable metrics or impact.
   - **Missing Skills (Red)**: Core industry expectations for the target role that are completely absent.
3. Quantified metrics show: *Demonstrated ($X\%$), Weak ($Y\%$), Missing ($Z\%$)*.

---

### Stage 5: YouTube Learning Hub & Course Recommendations
Directly on the `/skill-gap` and `/roadmap` views:
1. For every missing or weak skill, the system binds curated learning resources:
   - **YouTube Video Tutorials**: Curated video title, channel name (e.g. *freeCodeCamp*, *Amigoscode*, *Fireship*), duration, and pedagogical rationale.
   - **Official Documentation**: Direct link to official guides (e.g., Spring Boot Docs, PyTorch Tutorials).
   - **Portfolio Blueprint**: Specific mini-project instructions showing how to implement this skill on GitHub to prove competency to recruiters.
2. User can launch the **8-Week Career Roadmap (`/roadmap`)**, which organizes learning deliverables across 4 bi-weekly sprints.

---

### Stage 6: 7-Portal Job Discovery
User navigates to `/jobs/search`:
1. Target job title and location are pre-filled based on the user's analyzed profile.
2. System displays two tiers of job discovery:
   - **Live API Integration**: Real-time remote opportunities fetched from the Remotive API with tags, salary indications, and company profiles.
   - **7 Pre-Configured Portal Aggregator**: Instant 1-click formatted queries that search:
     1. LinkedIn Jobs
     2. Indeed
     3. Naukri.com
     4. Glassdoor
     5. Google Jobs
     6. Foundit (Monster)
     7. Internshala

---

### Stage 7: Direct Employer Application
1. User clicks **Apply on Official Site** for any listed job.
2. Application opens directly in a new tab on the authentic employer/recruiter portal.
3. Zero scraping or fake job redirects.

---

### Stage 8: Unified Application Tracker
1. From the job search page, the user clicks **Save to Tracker** on any interesting position.
2. Job is persisted in MySQL (`job_matches` table) linked to the user account with initial status `SAVED`.
3. User opens `/tracker`:
   - Pipeline metrics: Total Saved, Applied, Interviewing, Offered, Rejected.
   - Status transitions: 1-click dropdown to advance application to `APPLIED`, `INTERVIEWING`, `OFFERED`, or `REJECTED`.
   - Notes field to record interview dates, contact person, or salary offers.
4. Summary metrics automatically feed into the main authenticated `/dashboard`.

---

### Stage 9: Role-Specific Interview Preparation
1. User navigates to `/interview`.
2. Selects target role (e.g. *Full Stack Java Developer* or *Python AI & ML Engineer*).
3. System loads structured interview questions across three critical dimensions:
   - **Core Technical Questions**: In-depth conceptual inquiries with evaluation criteria and ideal answers.
   - **System Design & Architecture**: Scalability and architectural scenario questions.
   - **STAR Behavioral Questions**: Situation-Task-Action-Result format scenarios testing soft skills and leadership.
4. User studies question guides to maximize interview conversion rates.
