# Database Connection, Topology and Data-Flow Forensic Audit

## Subject: Relational Database Inspection, JPA Mapping and Data Flow
**Target Database:** MySQL 8.4 Server on `127.0.0.1:3306`  
**Database Name:** `resume_analyzer`  
**Audit Date:** October 30, 2026  
**Status:** **Forensically Verified Active with 7 Operational Tables**

---

## 1. Database Runtime & Connection Diagnostics

* **Database Engine:** MySQL 8.4 Server (`mysqld.exe`, PID `7672`)
* **Listening Socket:** Port `3306`
* **JDBC Driver:** `com.mysql.cj.jdbc.Driver` (Connector/J `8.3.0`)
* **JDBC URL:** `jdbc:mysql://localhost:3306/resume_analyzer?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
* **Connection Pooling:** HikariCP (`HikariPool-1` with 10 established connections active)
* **ORM / JPA Provider:** Hibernate ORM `6.5.2.Final`
* **Schema Evolution Mechanism:** `spring.jpa.hibernate.ddl-auto=update`
  - *Finding:* No database migration tools (Flyway, Liquibase) are configured in `pom.xml`. Schemas are automatically synchronized from JPA entity annotations upon application startup.

---

## 2. Table Inventory & Verified Aggregate Row Counts

Query executed via `information_schema.tables` against schema `resume_analyzer`:

| Table Name | Associated JPA Entity | Primary Key | Foreign Keys | Table Row Count |
| :--- | :--- | :--- | :--- | :--- |
| `users` | [`User.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/User.java) | `id` (bigint) | None | **2** |
| `resumes` | [`Resume.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/Resume.java) | `id` (bigint) | `user_id` (references `users.id`) | **6** |
| `resume_analysis`| [`ResumeAnalysis.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/ResumeAnalysis.java)| `id` (bigint) | `resume_id` (UNIQUE, references `resumes.id`)| **0** |
| `ai_analyses` | [`AIAnalysis.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/AIAnalysis.java) | `id` (bigint) | `resume_id`, `user_id` | **2** |
| `job_matches` | [`JobMatch.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/JobMatch.java) | `id` (bigint) | `resume_id`, `user_id` | **0** |
| `feedback` | [`Feedback.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/Feedback.java) | `id` (bigint) | `user_id` | **7** |
| `admin_logs` | [`AdminLog.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/model/AdminLog.java) | `id` (bigint) | None | **15** |

*(Compliance Note: In accordance with privacy rules, individual candidate records, personal emails, and password hashes have been omitted. Only aggregate row counts and schema metadata are reported.)*

---

## 3. End-to-End Application Data Flow

```
[Candidate Browser]
        │
        ▼ POST /register
  UserRepository.save(User) ──────────────► INSERT INTO users
        │
        ▼ POST /resume/upload
  ResumeRepository.save(Resume) ──────────► INSERT INTO resumes (raw_text in LONGTEXT)
        │
        ├─► ResumeAnalyzerService ────────► INSERT INTO resume_analysis (ats_score, keyword_score)
        │
        ├─► AIResumeAnalyzerService ──────► INSERT INTO ai_analyses (Gemini feedback, model_used)
        │
        └─► JobService ───────────────────► INSERT INTO job_matches (match_score, job_title)
```

---

## 4. Architectural Strengths & Deficiencies

### Strengths
1. **Candidate Tenant Isolation:** Repository queries consistently scope queries by authenticated `userId` (`findByUserId()`), preventing cross-tenant data visibility.
2. **Document Persistence:** Raw text extracted from resumes is preserved in `resumes.raw_text` (`LONGTEXT`), allowing rapid re-scoring without re-uploading files.

### Identified Deficiencies
1. **Absence of Versioned Migrations:** Relying on Hibernate `ddl-auto=update` can cause column drift across developer environments. Flyway migrations should be introduced in a future phase.
2. **Missing Test Isolation:** The integration test suite (`SmartResumeAnalyzerApplicationTests`) executes against this live MySQL instance instead of an isolated H2 in-memory test database.
