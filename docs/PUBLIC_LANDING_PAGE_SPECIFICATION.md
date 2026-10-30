# Public Landing Page Specification

**Smart Resume AI — Experience Architecture**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Objective and UI/UX Architecture

The primary UI change in version 2.0.0 is the **strict architectural separation between the public landing page and the authenticated application**.

### Problem with Previous Layout
Previously, the root URL (`/`) used the application layout (`layout/base.html`), which forcibly rendered the application sidebar (`aside#appSidebar`) containing `Dashboard`, `Resume Analyzer`, `AI Analyzer`, `Resume Builder`, `Job Search`, etc. This gave first-time visitors an overwhelming and confusing experience, merging marketing presentation with internal app navigation.

### The Solution: Two Dedicated Layouts
1. **Public Layout (`src/main/resources/templates/layout/public.html`)**:
   - **No App Sidebar**. Full viewport width ($100\%$).
   - Top horizontal navigation bar with brand emblem, section anchor links (`#features`, `#workflow`, `#roles`, `#skills`, `#formula`, `/about`), and guest CTA pills (`Sign In`, `Get Started Free`).
   - Sticky header with backdrop-filter glassmorphism.
   - Comprehensive footer with ecosystem links, legal notices, and compliance statements.
2. **Authenticated Layout (`src/main/resources/templates/layout/base.html`)**:
   - Active only when logged in or inside internal application routes (`/dashboard`, `/analyzer/**`, `/builder/**`, `/tracker/**`, etc.).
   - Sidebar partitioned into **Workspace**, **Career Acceleration**, and **Platform & Links**.
   - Collapsible on mobile viewports.

---

## 2. Public Landing Page Structure (`index.html`)

The public landing page is divided into 7 distinct, cohesive sections:

### Section 1: Hero Section
- **Badge**: 3D Animated Emerald Emblemed Badge: `Next-Generation Career Intelligence`.
- **Headline**: "Your Career Journey, Connected in One Place." with emerald-to-cyan gradient text.
- **Lead Paragraph**: Explains the core problem (fragmentation across resume builders, ATS checkers, job sites, and YouTube) and the connected solution.
- **Primary CTAs**:
  - `Get Started Free` (`/register`) — Emerald green pill with hover elevation.
  - `Sign In` (`/login`) — Subtle dark button with border highlight.
  - `Explore Complete Workflow` (`/#workflow`) — Smooth scrolling anchor.
- **Trust Strip**: Highlights DPDP/GDPR PII masking, 100% explainable ATS formula, curated YouTube learning, 7 live job boards, and application tracker.

### Section 2: Problem vs. Connected Solution
- **The Problem**: 3 red-tinted alert cards highlighting:
  1. *Fragmented Experience*: Lost time switching between disconnected websites.
  2. *Opaque ATS Black Boxes*: Arbitrary scores without mathematical explanation.
  3. *No Actionable Skill Bridge*: Missing skills detected with zero learning resources.
- **The Solution Banner**: Visual 9-stage progression ribbon showing the unified pipeline from resume creation to interview success.

### Section 3: Core Platform Capabilities (Grid of 6 Cards)
1. **Explainable ATS Scoring**: Keyword regex matching, format density, and section grading.
2. **AI Career Coach**: Gemini 2.5 flash reviews, Google X-Y-Z bullet rewrites, and cover letters.
3. **4-Style Resume Builder**: Modern, Classic, Minimalist, Creative with Flying Saucer PDF export.
4. **Skill Gaps & YouTube Hub**: Demonstrations vs gaps + direct YouTube recommendations.
5. **7-Portal Job Aggregator**: Pre-formatted search queries for LinkedIn, Indeed, Naukri, Glassdoor, etc.
6. **Application Tracker & Interview Prep**: Stage tracking (Applied/Interview/Offered) + technical & STAR behavioral questions.

### Section 4: Target Job Roles & Real-World Sample Job Descriptions
Provides interactive role cards that benchmark resumes against real industry requisitions:
- **Full Stack Java Developer**: Java 21, Spring Boot, Microservices, React, Docker, MySQL.
- **Python AI & ML Engineer**: Python, FastAPI, PyTorch, Scikit-learn, spaCy, RAG.
- **Cloud DevOps & SRE Architect**: AWS, Terraform, Kubernetes, Docker, CI/CD, Linux.
Each card features an active button: `Benchmark This Role` (directs to login/scan).

### Section 5: The Explainable 6-Factor ATS Formula
Displays the mathematical equation and transparent breakdown:
$$\text{ATS Score} = (S_{\text{keywords}} \times 0.30) + (S_{\text{exp}} \times 0.20) + (S_{\text{fmt}} \times 0.20) + (S_{\text{contact}} \times 0.10) + (S_{\text{summary}} \times 0.10) + (S_{\text{edu}} \times 0.10)$$
Explains each coefficient ($30\%$ keywords, $20\%$ experience, $20\%$ format, $10\%$ contact, $10\%$ summary, $10\%$ education).

### Section 6: Connected 9-Step Candidate Journey
Numbered card grid describing each phase of candidate progression:
1. Discover & Register
2. Upload or Build Resume
3. Algorithmic ATS Audit
4. Identify Skill Gaps
5. Stream YouTube & Courses
6. Search 7 Job Portals
7. Apply on Official Links
8. Track Applications
9. Ace the Interview

### Section 7: Final Conversion Call to Action
High-contrast CTA banner driving user registration and immediate resume audit.

---

## 3. Responsive Design & Accessibility

- **Mobile Viewport ($< 768\text{px}$)**:
  - Navbar collapses cleanly; brand and primary CTA remain accessible.
  - Cards stack into single-column layouts with finger-friendly tap targets ($\ge 44\text{px}$).
- **Color System**:
  - Background: Obsidian `#0B0F19` with subtle card fill `#141A23`.
  - Accent Primary: Emerald `#10B981` (growth, success, verification).
  - Accent Secondary: Cyan `#06B6D4` (AI intelligence, discovery).
  - Accent Alert: Red `#EF4444` (skill gaps, problem indicators).
  - Borders: Semi-transparent glass borders `rgba(255, 255, 255, 0.08)`.
- **WCAG 2.1 AA Compliance**: All typography achieves contrast ratios $\ge 4.5:1$ against dark backgrounds.
