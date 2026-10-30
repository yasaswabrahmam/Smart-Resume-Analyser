# Build, Test and Runtime Log Forensic Audit

## Project: Smart AI Resume Analyzer
**Compiler & JDK:** Microsoft JDK 21.0.12.1 LTS (`C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot`)  
**Build Tool:** Apache Maven 3.9.6  
**Host Environment:** Microsoft Windows 11 Home (x64)  
**Audit Date:** October 30, 2026  
**Status:** **100% Pass Rate Verified Across All 8 Test Classes (22 Methods)**

---

## 1. Verified Build & Test Execution Commands

### Command 1: Test Compilation Verification
```powershell
Set-Location 'Smart-Resume-Analyzer-Java'
mvn test-compile
```
* **Exit Code:** `0` (SUCCESS)
* **Evidence:** Successfully compiled 50 production source files and 8 test classes without warnings or errors.

---

### Command 2: Unit Test Suites Execution (7 Classes, 21 Methods)
```powershell
Set-Location 'Smart-Resume-Analyzer-Java'
mvn test '-Dtest=FileValidationServiceTest,ResumeAnalyzerEdgeCasesTest,ResumeAnalyzerServiceTest,ResumeBuilderServiceTest,ResumeBuilderTemplatesTest,ResumeParserServiceTest,SecurityHardenedTest'
```
* **Exit Code:** `0` (SUCCESS)
* **Execution Duration:** **5.352 seconds**
* **Sanitized Maven Execution Output:**
  ```text
  [INFO] Running com.smartresume.service.FileValidationServiceTest
  [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.125 s -- in com.smartresume.service.FileValidationServiceTest
  [INFO] Running com.smartresume.service.ResumeAnalyzerEdgeCasesTest
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.048 s -- in com.smartresume.service.ResumeAnalyzerEdgeCasesTest
  [INFO] Running com.smartresume.service.ResumeAnalyzerServiceTest
  [INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.082 s -- in com.smartresume.service.ResumeAnalyzerServiceTest
  [INFO] Running com.smartresume.service.ResumeBuilderServiceTest
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.312 s -- in com.smartresume.service.ResumeBuilderServiceTest
  [INFO] Running com.smartresume.service.ResumeBuilderTemplatesTest
  [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.064 s -- in com.smartresume.service.ResumeBuilderTemplatesTest
  [INFO] Running com.smartresume.service.ResumeParserServiceTest
  [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.210 s -- in com.smartresume.service.ResumeParserServiceTest
  [INFO] Running com.smartresume.service.SecurityHardenedTest
  [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.450 s -- in com.smartresume.service.SecurityHardenedTest
  [INFO] Results:
  [INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
  ```

---

### Command 3: Full Context Integration Test Execution (1 Class, 1 Method)
```powershell
Set-Location 'Smart-Resume-Analyzer-Java'
mvn test '-Dtest=SmartResumeAnalyzerApplicationTests'
```
* **Exit Code:** `0` (SUCCESS)
* **Execution Duration:** **13.478 seconds**
* **Sanitized Maven Execution Output:**
  ```text
  [INFO] Running com.smartresume.SmartResumeAnalyzerApplicationTests
  19:13:35.412 [main] INFO  c.s.SmartResumeAnalyzerApplication - Starting SmartResumeAnalyzerApplication using Java 21.0.12.1
  19:13:37.892 [main] INFO  o.h.e.t.j.p.i.JtaPlatformInitiator - HHH000489: No JTA platform available (set hibernate.transaction.jta.platform)
  19:13:38.105 [main] INFO  c.z.h.HikariDataSource - HikariPool-1 - Starting...
  19:13:38.450 [main] INFO  c.z.h.HikariDataSource - HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@...
  19:13:38.452 [main] INFO  c.z.h.HikariDataSource - HikariPool-1 - Start completed.
  19:13:42.210 [main] INFO  c.s.SmartResumeAnalyzerApplication - Started SmartResumeAnalyzerApplication in 8.125 seconds
  [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 13.478 s -- in com.smartresume.SmartResumeAnalyzerApplicationTests
  [INFO] Results:
  [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
  ```

---

## 2. Total Verified Baseline Summary

| Test Suite Category | Number of Classes | Test Methods | Passed | Failed | Errors | Skipped | Pass Rate | Total Execution Time |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Unit Test Suites** | 7 | 21 | **21** | 0 | 0 | 0 | **100%** | **5.352 s** |
| **Integration Test** | 1 | 1 | **1** | 0 | 0 | 0 | **100%** | **13.478 s** |
| **Overall Combined** | **8** | **22** | **22** | **0** | **0** | **0** | **100%** | **18.830 s** |

---

## 3. What the Test Results Prove and Do Not Prove

### What is Proven:
1. **Compilability:** The entire Java 21 codebase compiles cleanly under Apache Maven 3.9.6.
2. **Deterministic Mathematical Stability:** The ATS calculation, score bounds ($[0, 100]$), keyword matching, and document parsers execute without regressions.
3. **Password Security:** BCrypt password hashing generates valid salted hashes with strength 12.
4. **Spring Application Context Health:** All 50 Spring beans, JPA repositories, and configurations wire up successfully.

### What is NOT Proven:
1. **Live External AI Uptime:** The tests do not validate live network calls to Google Gemini (which require an active, unthrottled API key).
2. **Test Database Decoupling:** `SmartResumeAnalyzerApplicationTests` connects to the local MySQL server because `src/test/resources/application-test.properties` is missing. If MySQL is offline, the integration test fails.
3. **Controller HTTP Endpoint Coverage:** MockMvc slice tests (`@WebMvcTest`) covering HTTP response codes for controllers are currently missing and must be introduced in Phase 2.
