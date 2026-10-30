# Gemini & LLM Integration Report

**Smart Resume AI — Generative AI & Prompt Engineering Architecture**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Role of Large Language Models in the Platform

In strict compliance with our architectural principles:
- **Java is the primary application language**: All business logic, authentication, persistence, and deterministic scoring reside in Java.
- **LLMs are never used for numeric ATS scores**: Scores are computed via the explainable 6-factor deterministic equation to prevent hallucinations.
- **LLMs provide qualitative career enhancement**: Google Gemini 2.5 is utilized for tasks where natural language generation shines:
  1. *Qualitative Resume Review*: Detailed constructive critiques of tone, clarity, and visual impact.
  2. *Google X-Y-Z Bullet Point Rewrites*: Transforming passive statements (*"Responsible for maintaining database"*) into quantified achievements (*"Optimized MySQL query execution times by 38% for 500K daily active users using compound indexing"*).
  3. *Tailored Cover Letter Drafting*: Synthesizing resume qualifications with specific company requirements.
  4. *Situational Interview Scenarios*: Generating context-aware technical and STAR behavioral interview questions.

---

## 2. Technical Integration via Java (`GeminiAiService.java`)

### 2.1 Communication Protocol
- **Endpoint**: `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={API_KEY}`
- **Protocol**: HTTP/1.1 REST using Spring `RestClient` with JSON payloads.
- **Model**: `gemini-2.5-flash` (Optimized for low latency, high throughput, and high reasoning fidelity).
- **Timeouts**: Connect timeout: $5,000\text{ms}$; Read timeout: $15,000\text{ms}$.

### 2.2 Privacy & PII Sanitization
Before any resume text is sent to the Gemini API endpoint:
1. `FileValidationService.java` applies regex filters to redact:
   - Email addresses $\rightarrow$ `[REDACTED_EMAIL]`
   - Phone numbers $\rightarrow$ `[REDACTED_PHONE]`
   - Social security / Aadhaar / national ID numbers $\rightarrow$ `[REDACTED_ID]`
2. Only sanitized technical skills, employment responsibilities, and project descriptions are transmitted to the LLM.

---

## 3. Prompt Engineering Specifications

### 3.1 Qualitative AI Review Prompt
```
System Prompt:
You are an expert Executive Technical Recruiter and Career Coach. 
Analyze the candidate's resume against the target role: {targetRole}.

Provide structured, actionable advice covering:
1. Executive Impression (First 6-second scan).
2. Bullet Point Enhancements using Google's X-Y-Z Formula (Accomplished [X] as measured by [Y], by doing [Z]).
3. Critical Gaps to Address before applying.
4. Suggested Interview talking points.

Maintain a constructive, professional, and encouraging tone.
```

### 3.2 Resilient Heuristic Fallback
If the external Gemini API is unreachable, if network connectivity is severed, or if the API quota is exhausted:
1. `GeminiAiService.java` intercepts the `RestClientException`.
2. It logs a warning without throwing an uncaught 500 error to the user.
3. It seamlessly activates an internal **Deterministic Rule-Based Fallback Engine** that generates structured feedback and Google X-Y-Z examples based on detected keywords.
4. The user receives uninterrupted, high-quality guidance with zero downtime.
