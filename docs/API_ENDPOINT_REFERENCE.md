# API Endpoint Reference Manual

## Project: Smart AI Resume Analyzer
**Server Port:** 8085 | **Protocol:** HTTP/1.1  
**Audit Date:** October 30, 2026  
**Status:** **Verified Complete Endpoint Reference**

---

## 1. Implemented Web & REST Endpoints

| Method | URL Path | Controller | Purpose | Request Structure | Response Structure | Auth Required | Related Service Methods |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | `HomeController` | Render landing page | None | HTML View (`index.html`) | None (Public) | None |
| `GET` | `/home` | `HomeController` | Alternate landing page | None | HTML View (`index.html`) | None (Public) | None |
| `GET` | `/about` | `HomeController` | About platform view | None | HTML View (`about.html`) | None (Public) | None |
| `GET` | `/login` | `AuthController` | Display login form | None | HTML View (`auth/login.html`) | None (Public) | None |
| `GET` | `/register` | `AuthController` | Display register form | None | HTML View (`auth/register.html`)| None (Public) | None |
| `POST` | `/register` | `AuthController` | Register candidate | `UserRegistrationDto` (Form) | Redirect `/login?success` | None (Public) | `UserService.registerUser()` |
| `GET` | `/dashboard` | `DashboardController` | Candidate dashboard | None | HTML View (`dashboard/index`) | `ROLE_USER` | `ResumeRepository.findByUserId()` |
| `GET` | `/profile` | `DashboardController` | Candidate profile view| None | HTML View (`dashboard/profile`)| `ROLE_USER` | `UserRepository.findByEmail()` |
| `GET` | `/resume/upload` | `ResumeController` | Resume upload form | None | HTML View (`resume/upload.html`)| `ROLE_USER` | None |
| `POST` | `/resume/upload` | `ResumeController` | Ingest & analyze resume| `MultipartFile`, `jobRole` | Redirect `/resume/view/{id}` | `ROLE_USER` | `ResumeParserService`, `ResumeAnalyzerService`, `AIResumeAnalyzerService` |
| `GET` | `/resume/view/{id}` | `ResumeController` | View analysis report | Path Variable `id` | HTML View (`resume/results.html`)| `ROLE_USER` | `ResumeAnalysisRepository.findByResumeId()` |
| `GET` | `/resume/history` | `ResumeController` | Resume upload history | None | HTML View (`resume/history.html`)| `ROLE_USER` | `ResumeRepository.findByUserId()` |
| `POST` | `/resume/delete/{id}`| `ResumeController`| Delete resume record | Path Variable `id` | Redirect `/resume/history` | `ROLE_USER` | `ResumeRepository.deleteById()` |
| `GET` | `/builder` | `ResumeBuilderController` | Resume builder form | None | HTML View (`builder/form.html`) | None (Public) | None |
| `POST` | `/builder/save` | `ResumeBuilderController` | Save builder state | `ResumeBuilderDto` (Form) | Redirect `/builder/preview/{id}` | None (Public) | `ResumeBuilderService.saveResume()` |
| `GET` | `/builder/export/pdf/{id}` | `ResumeBuilderController` | Download resume PDF | Path Variable `id` | `application/pdf` binary stream | None (Public) | `ResumeBuilderService.generatePdf()` |
| `GET` | `/builder/export/docx/{id}`| `ResumeBuilderController` | Download resume Word | Path Variable `id` | `application/vnd...` binary stream| None (Public) | `ResumeBuilderService.generateDocx()`|
| `GET` | `/jobs` | `JobController` | Job board listings | Query param `q` (Optional) | HTML View (`jobs/list.html`) | None (Public) | `JobService.searchJobs()` |
| `GET` | `/admin` | `AdminController` | Admin dashboard | None | HTML View (`admin/dashboard`) | `ROLE_ADMIN` | `AdminService.getMetrics()` |
| `GET` | `/admin/users` | `AdminController` | User management list | None | HTML View (`admin/users.html`) | `ROLE_ADMIN` | `UserRepository.findAll()` |
| `POST` | `/admin/users/toggle/{id}`| `AdminController`| Enable/suspend user | Path Variable `id` | Redirect `/admin/users` | `ROLE_ADMIN` | `AdminService.toggleUser()` |
| `GET` | `/admin/export-csv` | `AdminController` | Export users to CSV | None | `text/csv` stream | `ROLE_ADMIN` | `AdminService.generateCsv()` |
| `GET` | `/feedback` | `FeedbackController` | Platform review form | None | HTML View (`feedback.html`) | None (Public) | None |
| `POST` | `/feedback/submit` | `FeedbackController` | Submit feedback | `FeedbackDto` (Form) | Redirect `/feedback?success` | None (Public) | `FeedbackService.saveFeedback()` |

---

## 2. Proposed & Planned Endpoints (Not Implemented in Current Source)

* `POST /api/v1/ml/extract-entities`: Proposed for Python FastAPI service (Phase 4).
* `POST /api/v1/ml/semantic-score`: Proposed for Python FastAPI service (Phase 4).
* `POST /api/v1/ml/embed`: Proposed for Python FastAPI service (Phase 4).
* `POST /api/v1/rag/query`: Proposed for Qdrant Vector search (Phase 5).
