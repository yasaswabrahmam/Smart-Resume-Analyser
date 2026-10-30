# Google Gemini & LLM Integration Forensic Audit

## Subject: Generative AI Implementation, Prompt Architecture and Fallbacks
**Audit Date:** October 30, 2026  
**Auditor:** Technical Forensic Auditor  
**Status:** **Direct Java REST Client Verified**

---

## 1. Technical Configuration & Model Identifier

* **Configured Model:** `gemini-2.5-flash` (Defined in `application.properties`: `gemini.model=${GEMINI_MODEL:gemini-2.5-flash}`).
* **Candidate Fallback Ladder:**
  1. `gemini-2.5-flash` (Primary production candidate)
  2. `gemini-1.5-flash` (Stable fast secondary candidate)
  3. `gemini-1.5-pro` (High-reasoning candidate)
  4. `gemini-pro` (Legacy candidate)
  5. Built-in Local Heuristic Engine (Offline fallback)
* **API Endpoint:** `https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={API_KEY}`
* **Client Implementation:** [`AIResumeAnalyzerService.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/service/AIResumeAnalyzerService.java) using native Java 11 `HttpClient` over HTTP/2 with a 15-second connect timeout.

---

## 2. What Data is Sent and What Gemini Returns

### Data Sent to Gemini
1. **Target Role:** E.g., `"Software Engineer"`.
2. **Job Description:** Optional candidate-provided requisition text.
3. **Resume Text:** Plain text extracted from uploaded PDF/DOCX.
*Both inputs are sandboxed within `=== BEGIN UNTRUSTED DATA ===` boundary tags to defend against prompt injection.*

### What Gemini Returns
Gemini returns a markdown document structured with specific headings:
* `## Overall Assessment` (Structure, readability)
* `## Professional Profile Analysis` (Career narrative)
* `## Skills Analysis` (Current vs. Missing skills)
* `## Key Strengths` (5 bullet points)
* `## Areas for Improvement` (5 actionable bullet points)
* `## ATS Optimization Assessment` (ATS Score estimate: `[XX]/100`)
* `## Recommended Courses` (5 certifications)

---

## 3. Response Validation, Fallbacks & Advanced LLM Features

* **Response Validation:** Currently parsed using regular expressions (`Pattern.compile("ATS Score:\\s*(\\d{1,3})")`). If Gemini deviates from expected markdown, the system silently sets default scores (`75.0`).
* **Offline Fallback Engine:** If API keys expire or network drops, `generateHeuristicAnalysis()` generates structured analysis based on character counts and section headings, ensuring zero downtime during demonstrations.
* **Structured JSON Outputs (`response_schema`):** **Not currently implemented.** Output is free-form markdown; upgrading to native JSON mode is scheduled for **Phase 3**.
* **Function Calling / Tool Calling:** **Not currently implemented.** Scheduled for **Phase 6** with Java multi-agent modules.
* **Role Distinction:** Gemini does **not** calculate the official ATS score; it provides qualitative career coaching to accompany Java's deterministic mathematical score.
