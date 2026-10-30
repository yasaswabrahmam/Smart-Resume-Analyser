# Architecture & Data Flow

**Smart Resume AI — Technical Architecture**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. System Architecture Diagram

```mermaid
flowchart TD
    subgraph ClientLayer ["Client Presentation Layer (Browser)"]
        PublicLanding["Dedicated Public Landing Page (/)<br>Full-width, No Sidebar"]
        AppWorkspace["Authenticated Application Workspace (/app)<br>Collapsible App Sidebar"]
    end

    subgraph SecurityGateway ["Spring Security 6 Gateway (Port 8085)"]
        SecFilter["SecurityFilterChain"]
        CSRF["CSRF Protection & Session Management"]
        PermitAll["PermitAll: /, /login, /register, /skill-gap, /roadmap, /interview"]
        AuthRequired["Authenticated: /dashboard, /analyzer/**, /builder/**, /tracker/**"]
    end

    subgraph AppCore ["Spring Boot 3.3.4 Application Engine"]
        Controllers["10 Web Controllers<br>(Home, Auth, Dashboard, SkillGap, Tracker, etc.)"]
        
        subgraph CoreServices ["Core Services"]
            ATS["ResumeAnalyzerService<br>(Deterministic 6-Factor Math)"]
            GapSvc["SkillGapService<br>(Demonstrated vs Gap + YouTube)"]
            RoadmapSvc["CareerRoadmapService<br>(8-Week Milestones)"]
            TrackerSvc["ApplicationTrackerService<br>(Pipeline Statuses)"]
            InterviewSvc["InterviewPrepService<br>(Technical & STAR Questions)"]
            Parser["ResumeParserService<br>(Apache Tika & PDFBox)"]
            Builder["ResumeBuilderService<br>(Flying Saucer / iText)"]
            Gemini["GeminiAiService<br>(Google Gemini 2.5 Flash Client)"]
        end
    end

    subgraph DataLayer ["Data & External Integrations"]
        MySQL[("MySQL 8.4 InnoDB<br>(smart_resume_db: 7 Tables)")]
        GeminiAPI["Google Gemini LLM REST API<br>(Structured JSON & Prompting)"]
        RemotiveAPI["Remotive Remote Jobs API<br>(Live Tech Job Requisitions)"]
        ExternalPortals["7 Job Aggregation Portals<br>(LinkedIn, Indeed, Naukri, Glassdoor, etc.)"]
    end

    ClientLayer --> SecurityGateway
    SecFilter --> Controllers
    Controllers --> CoreServices
    CoreServices --> MySQL
    Gemini --> GeminiAPI
    Controllers --> RemotiveAPI
    AppWorkspace --> ExternalPortals
```

---

## 2. Core Architectural Subsystems

### 2.1 Presentation Tier (Dual-Layout Architecture)
To eliminate visitor confusion, the user interface is strictly divided:
1. **Public Layout (`layout/public.html`)**:
   - Renders for anonymous and public marketing pages (`/`, `/#features`, `/#workflow`, `/#roles`, `/#skills`, `/#formula`, `/about`).
   - Displays a clean horizontal top navigation bar with brand emblem, section links, and auth CTAs (`Sign In`, `Get Started Free`).
   - Contains **zero internal dashboard elements** and **zero sidebars**.
2. **Authenticated Layout (`layout/base.html`)**:
   - Renders for authenticated user routes (`/dashboard`, `/analyzer/**`, `/builder/**`, `/tracker/**`, `/roadmap`, `/interview`, `/admin`).
   - Displays the dedicated three-tier sidebar navigation:
     - **Workspace**: Upload & Scan, AI Coach, Resume Builder.
     - **Career Acceleration**: Skill Gaps & YouTube Hub, 8-Week Roadmap, Job Discovery, Application Tracker, Interview Prep.
     - **Platform & Links**: Documentation, Admin Console (Role-guarded), Profile & Sign Out.

---

### 2.2 Security Architecture & Route Enforcement
Configured in `SecurityConfig.java`:
```java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers(
            "/", "/home", "/about", "/contact",
            "/login", "/register",
            "/css/**", "/js/**", "/images/**", "/webjars/**",
            "/skill-gap", "/skill-gap/**",
            "/roadmap", "/roadmap/**",
            "/interview", "/interview/**"
        ).permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/dashboard/**", "/analyzer/**", "/builder/**", "/tracker/**", "/app/**").authenticated()
        .anyRequest().authenticated()
    )
    .formLogin(form -> form
        .loginPage("/login")
        .defaultSuccessUrl("/dashboard", true)
        .permitAll()
    )
    .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .permitAll()
    );
```

---

### 2.3 Data Flow: The Closed-Loop Career Pipeline

1. **Resume Upload**: User submits PDF $\rightarrow$ `ResumeParserService` extracts text $\rightarrow$ PII regex masks phone/email/IDs $\rightarrow$ Persists `Resume` entity to MySQL.
2. **ATS Audit**: `ResumeAnalyzerService` runs the 6-factor deterministic calculation $\rightarrow$ Produces `AnalysisResultDto` $\rightarrow$ Persists `Analysis` entity $\rightarrow$ Displays grade report.
3. **Skill Gap Remediation**: `SkillGapController` sends resume text to `SkillGapService` $\rightarrow$ Segregates skills into Demonstrated, Weak, and Missing $\rightarrow$ Injects curated YouTube tutorials (title, channel, duration, rationale) and documentation URLs into the view.
4. **Career Roadmap Generation**: `CareerRoadmapService` generates an 8-week structured sprint plan with weekly milestones and deliverables.
5. **Job Aggregation**: `JobSearchController` fetches real-time jobs from Remotive API $\rightarrow$ Prepares formatted deep-links for 7 major portals (LinkedIn, Naukri, Indeed, etc.).
6. **Application Tracking**: User clicks **Save to Tracker** $\rightarrow$ `ApplicationTrackerService` saves job to `JobMatch` table with status `SAVED` $\rightarrow$ User manages pipeline (`APPLIED` $\rightarrow$ `INTERVIEWING` $\rightarrow$ `OFFERED`).
7. **Interview Simulation**: `InterviewPrepService` outputs tailored technical questions, system architecture scenarios, and STAR behavioral interview questions.
