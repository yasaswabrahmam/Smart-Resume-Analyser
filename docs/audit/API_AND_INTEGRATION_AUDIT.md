# API, Endpoint and External Integration Forensic Audit

## Project: Smart AI Resume Analyzer
**Version:** 1.0.0-SNAPSHOT  
**Framework:** Spring Boot 3.3.4 (Tomcat on port 8085)  
**Audit Date:** October 30, 2026  
**Status:** **Endpoint Inventory & Verification**

---

## 1. Internal Application Endpoints (Spring MVC & REST)

### 1.1 Public & Authentication Endpoints
| HTTP Method | Route / Path | Controller | Purpose | Auth Required | CSRF Protected | Runtime Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | `HomeController` | Render preserved landing page | No | N/A | **200 OK** |
| `GET` | `/home` | `HomeController` | Alternate landing page route | No | N/A | **200 OK** |
| `GET` | `/about` | `HomeController` | About and architecture overview | No | N/A | **200 OK** |
| `GET` | `/login` | `AuthController` | Display login form | No | N/A | **200 OK** |
| `GET` | `/register` | `AuthController` | Display registration form | No | N/A | **200 OK** |
| `POST` | `/register` | `AuthController` | Process registration payload | No | **Yes** | **Active** |
| `GET` | `/logout` | `SecurityConfig` | Invalidate session & clear cookies | Authenticated | **Yes** | **Active** |

### 1.2 Candidate Resume & Analysis Endpoints
| HTTP Method | Route / Path | Controller | Purpose | Auth Required | Request / Response Payload | Runtime Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/dashboard` | `DashboardController` | Render candidate metrics overview | Yes (`ROLE_USER`) | View: `dashboard/index` | **Active (302 if unauth)** |
| `GET` | `/resume/upload` | `ResumeController` | Render file upload drag-and-drop | Yes (`ROLE_USER`) | View: `resume/upload` | **Active (302 if unauth)** |
| `POST` | `/resume/upload` | `ResumeController` | Upload PDF/DOCX & trigger analysis| Yes (`ROLE_USER`) | `MultipartFile`, `jobRole` -> Redirect | **Active** |
| `GET` | `/resume/view/{id}`| `ResumeController` | View detailed ATS & Gemini scores | Yes (`ROLE_USER`) | Model: `ResumeAnalysisDto` | **Active** |
| `GET` | `/resume/history` | `ResumeController` | Tabular history of user uploads | Yes (`ROLE_USER`) | Model: `List<Resume>` | **Active** |
| `POST` | `/resume/delete/{id}`| `ResumeController`| Delete uploaded resume & analyses | Yes (`ROLE_USER`) | Path param `id` -> Redirect | **Active** |

### 1.3 Resume Builder & Export Endpoints
| HTTP Method | Route / Path | Controller | Purpose | Auth Required | Binary Output Stream | Runtime Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/builder` | `ResumeBuilderController` | Render dynamic multi-step form | No (Public) | View: `builder/form` | **200 OK** |
| `POST` | `/builder/save` | `ResumeBuilderController` | Save structured builder profile | Optional | Form DTO -> Redirect | **Active** |
| `GET` | `/builder/preview/{id}`| `ResumeBuilderController`| Preview formatted resume | Optional | View: `builder/preview` | **Active** |
| `GET` | `/builder/export/pdf/{id}`| `ResumeBuilderController`| Download compiled PDF binary | Optional | `application/pdf` (OpenPDF) | **Active** |
| `GET` | `/builder/export/docx/{id}`| `ResumeBuilderController`| Download compiled Word binary | Optional | `application/vnd.openxmlformats...` | **Active** |

### 1.4 Job Board & Administrative Endpoints
| HTTP Method | Route / Path | Controller | Purpose | Auth Required | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/jobs` | `JobController` | Display searchable job listings | No (Public) | **200 OK** |
| `GET` | `/jobs/search` | `JobController` | Query jobs by title or skill | No (Public) | **Active** |
| `GET` | `/admin` | `AdminController` | Telemetry KPI dashboard | Yes (`ROLE_ADMIN`) | **Active (302 to login)** |
| `GET` | `/admin/users` | `AdminController` | User management list | Yes (`ROLE_ADMIN`) | **Active** |
| `POST`| `/admin/users/toggle/{id}`| `AdminController`| Enable / disable user account | Yes (`ROLE_ADMIN`) | **Active** |
| `GET` | `/admin/export-csv` | `AdminController` | Export candidate list to CSV | Yes (`ROLE_ADMIN`) | **Active** |
| `GET` | `/feedback` | `FeedbackController` | Candidate review form | Optional | **Active** |
| `POST`| `/feedback/submit` | `FeedbackController` | Persist user experience rating | Optional | **Active** |

---

## 2. External Third-Party API Integrations

### 2.1 Google Gemini AI (`generateContent`)
* **Provider:** Google AI Studio / Generative Language API
* **Target URI:** `https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={API_KEY}`
* **Active Model:** `gemini-2.5-flash` (configured in `application.properties`)
* **Transport:** Native Java 11 `HttpClient` over HTTP/2 with JSON body
* **Rate Limits:** Free tier allows 15 RPM (Requests Per Minute) and 1M TPM (Tokens Per Minute)
* **Status:** **Active & Verified in Source Code**

### 2.2 Job Board Scraper (`JobScraperService`)
* **Technology:** Selenium WebDriver `4.20.0` & WebDriverManager `5.8.0`
* **Status:** **Demo / Mocked Infrastructure**
* **Finding:** While Selenium classes are imported in `pom.xml`, live external scraping of LinkedIn or Indeed is not active in production runtime to prevent anti-scraping blocks and IP bans. The UI returns curated/seeded job requisitions.

### 2.3 Vector Database (`Qdrant`)
* **Status:** **Planned Only** (No network calls or client dependencies exist in current source code).
