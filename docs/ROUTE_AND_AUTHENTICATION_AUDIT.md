# Route & Authentication Audit

**Smart Resume AI — Access Control Matrix**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Route Inventory & Authorization Rules

The following table comprehensively documents every active URL route in the application, its responsible controller, HTTP method, authorization level, and rendered layout:

| Route Path | Controller | Method | Authorization Level | Layout Used | Description |
|---|---|---|---|---|---|
| `/` | `HomeController` | `GET` | `permitAll()` | `layout/public.html` (NO sidebar) | Dedicated public landing page with hero, roles, and formula. |
| `/home` | `HomeController` | `GET` | `permitAll()` | `layout/public.html` | Alternate alias for public landing page. |
| `/about` | `HomeController` | `GET` | `permitAll()` | `layout/public.html` | About the platform and architectural principles. |
| `/login` | `AuthController` | `GET` | `permitAll()` | `layout/auth.html` | User sign-in page. |
| `/register` | `AuthController` | `GET`, `POST` | `permitAll()` | `layout/auth.html` | User registration & target role setup. |
| `/app` | `HomeController` | `GET` | `authenticated()` | N/A (Redirect) | Shortcut redirecting authenticated users to `/dashboard`. |
| `/dashboard` | `DashboardController` | `GET` | `authenticated()` | `layout/base.html` (WITH sidebar) | Authenticated user hub displaying ATS scores and application tracker statistics. |
| `/analyzer/upload` | `ResumeAnalyzerController` | `GET`, `POST` | `authenticated()` | `layout/base.html` | Drag-and-drop resume upload and parser. |
| `/analyzer/results/{id}` | `ResumeAnalyzerController` | `GET` | `authenticated()` | `layout/base.html` | Algorithmic 6-factor ATS score audit results. |
| `/analyzer/ai-coach` | `AIResumeAnalyzerController` | `GET`, `POST` | `authenticated()` | `layout/base.html` | Qualitative AI review and Google X-Y-Z bullet rewrites. |
| `/builder` | `ResumeBuilderController` | `GET`, `POST` | `authenticated()` | `layout/base.html` | 4-style resume builder with live preview. |
| `/builder/export/pdf` | `ResumeBuilderController` | `POST` | `authenticated()` | Binary Stream | Flying Saucer PDF binary download. |
| `/skill-gap` | `SkillGapController` | `GET` | `permitAll()` | `layout/base.html` / standalone | Interactive skill gap analyzer and YouTube learning hub. |
| `/skill-gap/analyze` | `SkillGapController` | `POST` | `permitAll()` | `layout/base.html` / standalone | Evaluates custom resume text against selected target role. |
| `/roadmap` | `CareerRoadmapController` | `GET` | `permitAll()` | `layout/base.html` / standalone | 8-week structured career trajectory roadmap. |
| `/jobs/search` | `JobSearchController` | `GET` | `permitAll()` | `layout/base.html` | Remotive live jobs + 7-portal deep-link aggregator. |
| `/tracker` | `ApplicationTrackerController` | `GET` | `authenticated()` | `layout/base.html` | Pipeline tracking board for job applications. |
| `/tracker/save` | `ApplicationTrackerController` | `POST` | `authenticated()` | N/A (Redirect) | Saves a discovered job directly into user's tracker. |
| `/tracker/update-status` | `ApplicationTrackerController` | `POST` | `authenticated()` | N/A (Redirect) | Advances stage (`APPLIED`, `INTERVIEWING`, `OFFERED`). |
| `/tracker/delete` | `ApplicationTrackerController` | `POST` | `authenticated()` | N/A (Redirect) | Deletes a tracked job from pipeline. |
| `/interview` | `InterviewPrepService` | `GET` | `permitAll()` | `layout/base.html` / standalone | Technical, architectural, and STAR behavioral interview questions. |
| `/admin` | `AdminController` | `GET` | `hasRole('ADMIN')` | `layout/base.html` | Administrator system dashboard and metrics. |
| `/admin/users` | `AdminController` | `GET` | `hasRole('ADMIN')` | `layout/base.html` | User account administration. |
| `/logout` | Spring Security | `POST` | `permitAll()` | N/A (Redirect) | Clears session cookie and redirects to `/login?logout`. |

---

## 2. Authentication Flow & Session Management

1. **Unauthenticated Visitor**:
   - Accesses `/` $\rightarrow$ Rendered with `layout/public.html` (no sidebar).
   - Attempts to access `/dashboard` or `/tracker` $\rightarrow$ HTTP 302 redirected to `/login`.
2. **Form Authentication**:
   - Post to `/login` with `username` and `password`.
   - `UserService.java` implements Spring Security `UserDetailsService`.
   - Passwords encoded using standard `BCryptPasswordEncoder`.
   - On successful authentication, user is redirected to `/dashboard`.
3. **Authenticated Session**:
   - Accesses `/` $\rightarrow$ Renders `layout/public.html` with a prominent `Go to Dashboard` CTA in place of login/register.
   - Accesses `/app` or `/dashboard` $\rightarrow$ Renders `layout/base.html` with full authenticated sidebar navigation.
4. **CSRF Protection**:
   - Enabled by default across all state-mutating endpoints (`POST`, `PUT`, `DELETE`).
   - Thymeleaf automatically injects `_csrf` hidden tokens into all forms.
