# Frontend & Landing Page Architecture Audit

## Project: Smart AI Resume Analyzer
**Audit Date:** October 30, 2026  
**View Engine:** Thymeleaf 3.1.2 | CSS3 Design Tokens | Bootstrap 5.3.2  
**Status:** **100% Preserved & Verified Functional at `http://localhost:8085/`**

---

## 1. Frontend Page Inventory & Controller Mapping

| View Template Path | Bound HTTP Route | Handling Controller Method | Visual & Functional Description |
| :--- | :--- | :--- | :--- |
| `templates/index.html` | `GET /`, `GET /home` | `HomeController.home()` | **Preserved Landing Page.** Hero section, dynamic 3D SVG emblem, live statistics ticker, interactive ATS score preview widget, and CTA buttons. |
| `templates/layout/base.html` | Shared layout | Master layout wrapper | Provides standard HTML `<head>`, meta tags, dark mode tokens (`#0B0F19`), Bootstrap 5, FontAwesome, and layout fragment slots. |
| `templates/fragments/brand.html` | Included fragment | Embedded in layout | High-end 3D animated SVG brand emblem with radial gradient glow. |
| `templates/fragments/navbar.html` | Included fragment | Embedded in layout | Responsive navigation header with conditional authenticated vs. anonymous user links. |
| `templates/fragments/footer.html` | Included fragment | Embedded in layout | Platform disclaimers, links, copyright. |
| `templates/auth/login.html` | `GET /login` | `AuthController.login()` | Glassmorphic centered authentication card with remember-me checkbox. |
| `templates/auth/register.html` | `GET /register` | `AuthController.register()` | Registration card with real-time password strength meter and Jakarta validation display. |
| `templates/dashboard/index.html` | `GET /dashboard` | `DashboardController.dashboard()` | Candidate personal portal displaying total uploads, average ATS score, and recent resumes. |
| `templates/dashboard/profile.html`| `GET /profile` | `DashboardController.profile()` | Candidate career preferences, target roles, contact info. |
| `templates/resume/upload.html` | `GET /resume/upload` | `ResumeController.showUpload()` | Drag-and-drop file upload zone accepting `.pdf` and `.docx` up to 20MB. |
| `templates/resume/results.html` | `GET /resume/view/{id}`| `ResumeController.viewAnalysis()` | Detailed evaluation view: Circular SVG ATS dial, Chart.js radar breakdown, green/red keyword badges, Gemini suggestions. |
| `templates/resume/history.html` | `GET /resume/history` | `ResumeController.history()` | Historical table listing all candidate resume uploads, dates, scores, and actions. |
| `templates/builder/form.html` | `GET /builder` | `ResumeBuilderController.builder()` | Multi-step interactive builder form with dynamic repeaters for experience and skills. |
| `templates/builder/preview.html` | `GET /builder/preview/{id}`| `ResumeBuilderController.preview()` | Live printable preview with instant "Export to PDF" and "Export to Word" buttons. |
| `templates/jobs/list.html` | `GET /jobs` | `JobController.listJobs()` | Job board browsing interface displaying match percentage badges per requisition card. |
| `templates/admin/dashboard.html` | `GET /admin` | `AdminController.dashboard()` | Admin panel showing telemetry metrics, user table with suspension toggles, and CSV export. |

---

## 2. Master Design System (`main.css`)

Located at [`src/main/resources/static/css/main.css`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/resources/static/css/main.css) (1,343 lines):
* **Theme Tokens:**
  - Base Dark: `--bg-primary: #0B0F19;`
  - Elevated Cards: `--bg-secondary: #141A23;`, `--bg-card: #1A2232;`
  - Subtle Borders: `--border-subtle: rgba(255, 255, 255, 0.08);`
  - Accent Palette: Indigo (`#6366F1`), Violet (`#8B5CF6`), Emerald (`#10B981`), Amber (`#F59E0B`), Crimson (`#EF4444`).
* **Animations:** Glowing gradient pulsation keyframes for the 3D brand mark and hover transitions on feature cards.
* **Typography:** Clean pairing of `Poppins` (display headings) and `Inter` (body copy).

---

## 3. Forensic Investigation: Is the Landing Page Missing?

* **Finding:** **The landing page is NOT missing.** It is active, returns HTTP `200 OK`, and renders completely at `http://localhost:8085/`.
* **Root Cause of Apparent Absence:**
  In [`SecurityConfig.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java#L28), clicking auxiliary top-level navbar links (`/features`, `/pricing`, `/contact`) causes an HTTP `302 Found` redirect to `/login` because those routes were omitted from the public `permitAll()` list. An unauthenticated user clicking those links is immediately bounced to `/login`, creating the impression that the landing page had vanished.
