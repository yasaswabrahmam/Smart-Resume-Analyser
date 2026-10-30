# Bug Diagnostics, Code Vulnerabilities and Improvements

## Project: Smart AI Resume Analyzer
**Framework:** Spring Boot 3.3.4 | Java 21 LTS  
**Audit Date:** October 30, 2026  
**Status:** **Forensically Confirmed Issues & Non-Destructive Remediation Plans**

---

## 1. Confirmed Bugs & Security Vulnerabilities

### Bug 1: Integration Tests Lack In-Memory Test Isolation
* **Location:** `SmartResumeAnalyzerApplicationTests.java`
* **Root Cause:** The repository does not contain `src/test/resources/application-test.properties`. The integration test boots using `src/main/resources/application.properties`, attempting to connect to `localhost:3306/resume_analyzer`. If MySQL is stopped or has a different password in a CI runner, the entire build fails.
* **Recommended Fix:** Create `src/test/resources/application-test.properties` backed by H2 in-memory DB:
  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL
  spring.datasource.driverClassName=org.h2.Driver
  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
  spring.jpa.hibernate.ddl-auto=create-drop
  ```

---

### Bug 2: Spreadsheet Formula Injection in CSV Export (CWE-1236)
* **Location:** [`AdminController.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/controller/AdminController.java) (`exportCsv()` method)
* **Root Cause:** Candidate names, emails, and roles are written to the CSV stream without sanitization. An attacker registering with a name beginning with `=`, `+`, `-`, or `@` (e.g. `=cmd|'/C calc'!A0`) could execute code when an administrator opens the CSV in Excel.
* **Recommended Fix:** Prepend an apostrophe (`'`) to any cell value that starts with `=`, `+`, `-`, or `@`.

---

### Bug 3: File Upload Validation Relies on HTTP MIME Header (CWE-434)
* **Location:** [`FileValidationService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/FileValidationService.java)
* **Root Cause:** The service validates `multipartFile.getContentType()`. This header is provided by the client and can be trivially spoofed by renaming an executable to `.pdf`.
* **Recommended Fix:** Inspect physical magic bytes on the input stream:
  - Verify PDF begins with `%PDF-` (`0x25 0x50 0x44 0x46`).
  - Verify DOCX begins with ZIP PK header `PK\x03\x04` (`0x50 0x4B 0x03 0x04`).

---

### Bug 4: Silent Failure and Hardcoded Fallbacks in Gemini Markdown Parsing
* **Location:** [`AIResumeAnalyzerService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/AIResumeAnalyzerService.java#L169-L186)
* **Root Cause:** AI responses are parsed using regular expressions (`ATS Score:\s*(\d{1,3})`). If Gemini emits a slightly altered heading (e.g., `**ATS Score:** 84/100`), the regex fails, and the code silently assigns hardcoded scores (`75.0` or `78.0`) without logging an error.
* **Recommended Fix:** Upgrade Gemini request configuration to specify native **Structured JSON Mode** (`response_mime_type: "application/json"`) with a typed Jackson DTO schema (`response_schema`).

---

### Bug 5: Naive String Substring Collisions in Keyword Matching
* **Location:** [`ResumeAnalyzerService.java` L222](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/ResumeAnalyzerService.java#L222)
* **Root Cause:** `lower.contains(skillLower)` matches substrings anywhere in words. Matching `"java"` will falsely match `"javascript"`, `"javadoc"`, or `"javaparser"`.
* **Recommended Fix:** Use word-boundary regular expressions (`\bjava\b`) or tokenized word sets.

---

### Bug 6: Public Route Whitelist Omission in Security Filter
* **Location:** [`SecurityConfig.java` L28](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java#L28)
* **Root Cause:** `/features`, `/pricing`, and `/contact` are not explicitly listed in `.requestMatchers(...).permitAll()`. Clicking those navbar links causes Spring Security to redirect unauthenticated users to `/login`.
* **Recommended Fix:** Add `"/features", "/pricing", "/contact"` to the `permitAll()` list in `SecurityConfig.java`.

---

## 2. Incomplete & Mocked Features

1. **Job Scraping (`JobScraperService.java`):** Contains Selenium 4 imports, but external live scraping is disabled. It serves seeded/mock job listings.
2. **Missing OCR Support:** Scanned image-based PDFs cannot be parsed because Tesseract OCR is not implemented.
3. **Missing Semantic Search:** Requisition matching is based on keyword count overlap rather than dense vector semantic similarity.
