# ATS Scoring Explanation

**Smart Resume AI — Algorithmic Integrity & Mathematical Specification**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Architectural Philosophy: Anti-Hallucination Determinism

A primary design principle of Smart Resume AI is **100% Explainable ATS Scoring**. 

Many modern commercial tools pass a raw resume to a large language model and ask for an arbitrary score. This introduces critical flaws:
1. **Hallucination & Inconsistency**: The same resume can score 65 on one run and 82 on the next.
2. **Unexplainable Penalties**: The applicant cannot see which specific calculation caused a deduction.
3. **Black-Box Biases**: Inherent stochastic variances disadvantage candidates without technical justification.

To eliminate these issues, **Smart Resume AI enforces a deterministic, mathematically transparent equation implemented entirely in Java (`ResumeAnalyzerService.java`)**. LLMs are utilized strictly for qualitative advice and wording improvements, never for core scoring.

---

## 2. The 6-Factor ATS Scoring Formula

$$\text{ATS Score} = (S_{\text{keywords}} \times 0.30) + (S_{\text{exp}} \times 0.20) + (S_{\text{fmt}} \times 0.20) + (S_{\text{contact}} \times 0.10) + (S_{\text{summary}} \times 0.10) + (S_{\text{edu}} \times 0.10)$$

Where:
- $\text{ATS Score} \in [0, 100]$
- Each sub-score $S_i \in [0, 100]$
- Weights sum exactly to $1.00$ ($30\% + 20\% + 20\% + 10\% + 10\% + 10\% = 100\%$)

---

## 3. Factor Breakdown & Sub-Score Calculations

### 3.1 Keyword Match ($S_{\text{keywords}}$ — Weight: $0.30$)
Evaluates the overlap between required job skills and the candidate's resume text.
- **Word-Boundary Regex Enforcement**:
  $$\text{Pattern: } \texttt{\b" + Pattern.quote(skill) + "\b}$$
  This prevents false-positive substring collisions (e.g., prevents "Java" from matching inside "JavaScript", or "Go" matching inside "Google").
- **Calculation**:
  $$S_{\text{keywords}} = \min\left(100, \frac{|\text{Matched Skills}|}{|\text{Target Skills}|} \times 100\right)$$
  If no target job description is provided, a standard baseline of 20 foundational technical skills is used.

### 3.2 Work Experience & Impact ($S_{\text{exp}}$ — Weight: $0.20$)
Evaluates the quality, structure, and action-orientation of employment history.
- Scans for standard experience headers (`Experience`, `Work History`, `Employment`, `Projects`).
- Analyzes bullet point density (optimum: 3 to 6 bullet points per role).
- Detects Google X-Y-Z formula verbs (`Engineered`, `Architected`, `Optimized`, `Spearheaded`, `Reduced`, `Increased`).
- Detects quantified metrics (percentages $\%$, dollar amounts $\$$, numeric throughput).
- **Calculation**:
  $$S_{\text{exp}} = \text{Base}(40) + \text{Verbs}(25) + \text{Metrics}(20) + \text{Tenure}(15) \le 100$$

### 3.3 Formatting Density ($S_{\text{fmt}}$ — Weight: $0.20$)
Evaluates the machine readability and structural cleanliness of the extracted text.
- **Word Count Density**:
  - $< 300$ words: Under-developed (penalty).
  - $450 - 1,200$ words: Optimum ATS reading density ($100$ points).
  - $> 1,800$ words: Overly verbose (minor deduction).
- **Structural Integrity**: Deducts points for unparseable unicode replacement characters (`\uFFFD`), broken fonts, or non-standard characters resulting from corrupted PDF layers.

### 3.4 Contact Information ($S_{\text{contact}}$ — Weight: $0.10$)
Verifies essential recruiter outreach channels using strict regex validation:
- Valid Email Address ($+30$ points)
- Valid Telephone Number ($+30$ points)
- LinkedIn Profile URL ($+20$ points)
- GitHub / Portfolio URL ($+20$ points)
- Total: $100$ points max.

### 3.5 Professional Summary ($S_{\text{summary}}$ — Weight: $0.10$)
Verifies the presence of a targeted career summary or profile statement:
- Detects headers (`Summary`, `Professional Profile`, `About Me`, `Career Objective`).
- Validates sentence length ($30 - 120$ words).
- If present and focused: $100$ points; if absent: $0$ points.

### 3.6 Education Verification ($S_{\text{edu}}$ — Weight: $0.10$)
Evaluates academic qualifications and degree token recognition:
- Detects degrees (`B.Tech`, `B.E.`, `B.S.`, `M.S.`, `Bachelor`, `Master`, `PhD`).
- Identifies graduation year patterns and accredited institution tokens.
- If verified: $100$ points; partial: $50$ points; absent: $0$ points.

---

## 4. Score Classification Tiers

| ATS Score Range | Tier Classification | Color Coding | Recruiter Likelihood |
|---|---|---|---|
| **$85 - 100$** | Exceptional (Top Tier) | Emerald Green (`#10B981`) | Very High — Automatic interview screening pass |
| **$70 - 84$** | Competitive (Strong) | Cyan / Teal (`#06B6D4`) | High — Competitive for mid/senior requisitions |
| **$50 - 69$** | Needs Improvement (Moderate) | Amber / Orange (`#F59E0B`) | Moderate — Missing critical keywords or metrics |
| **$0 - 49$** | Critical Gaps (Weak) | Rose Red (`#EF4444`) | Low — High probability of automated ATS rejection |
