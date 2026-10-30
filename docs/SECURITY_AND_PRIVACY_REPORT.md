# Security, Privacy, and Data Governance Report

## 1. Compliance Architecture (DPDP & GDPR Alignment)
Smart Resume AI is architected from first principles around candidate data sovereignty, complying with the **Digital Personal Data Protection (DPDP) Act 2023** and **General Data Protection Regulation (GDPR)**.

### A. The Zero-Unconsented Data Principle
- **No Training on Private Resumes**: Candidate resumes uploaded for parsing, ATS analysis, or improvement are strictly isolated and never fed into machine learning training datasets.
- **Explicit Consent Gate**: The automated MLOps orchestrator ([`ModelTrainingOrchestratorAgent.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/agent/ModelTrainingOrchestratorAgent.java)) blocks any dataset not explicitly certified as synthetic, public domain, or accompanied by documented candidate opt-in consent.

### B. Right to Erasure & Data Minimization
- Candidates have full control to permanently delete uploaded resume files, extracted profiles, and application tracking records.
- Document uploads are stored with UUID file paths rather than original user filenames, preventing path traversal attacks.

### C. PII Masking & Sanitization
When text is submitted to external LLMs (e.g., Google Gemini for qualitative review), Personally Identifiable Information (PII) is automatically masked:
- Email addresses are tokenized to `[EMAIL_REDACTED]`.
- Phone numbers are replaced with `[PHONE_REDACTED]`.
- Physical mailing addresses are stripped before outbound API dispatch.

---

## 2. Spring Security Defense-in-Depth Architecture

Configured in [`SecurityConfig.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/SecurityConfig.java):

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers(
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/api/**"),
            new AntPathRequestMatcher("/copilot/api/**")
        ))
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin())
            .contentTypeOptions(contentType -> {})
            .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
        )
        .sessionManagement(session -> session
            .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/analyzer/view/**", "/ai-analyzer/download-pdf/**", "/tracker/**", "/dashboard/**", "/app/**", "/profile/**").authenticated()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()
        )
...
```

| Security Defense | Implementation | Threat Mitigated |
| :--- | :--- | :--- |
| **Password Hashing** | `BCryptPasswordEncoder` (strength 10) | Credential theft, rainbow table attacks |
| **Session Fixation** | `sessionFixation().migrateSession()` | Session hijacking after login |
| **CSRF Protection** | Spring Security token validation | Cross-site request forgery |
| **Clickjacking Defense** | `X-Frame-Options: SAMEORIGIN` | UI redressing / iframe clickjacking |
| **MIME Sniffing Defense** | `X-Content-Type-Options: nosniff` | Drive-by executable payload execution |
| **Referrer Policy** | `strict-origin-when-cross-origin` | Credential leakage in referrer headers |

---

## 3. Human Confirmation Gate for Autonomous Agents
To prevent autonomous agent privilege escalation or unauthorized database mutations:
1. All 5 state-altering actions (`updateCareerProfile`, `saveJobApplication`, `updateApplicationStatus`, `enrollInLearningRoadmap`, `triggerModelRetrain`) cannot execute autonomously.
2. The agent generates a cryptographic one-time `pendingActionToken`.
3. The browser prompts the user via a modal dialog detailing the exact action and parameters.
4. The backend verifies the token and validates user ownership before executing any SQL statement.
