# ATS Scoring and Matching Evaluation Report

## 1. Rationale: Deterministic vs. Non-Deterministic Scoring
Many commercial ATS checkers generate arbitrary match percentages by submitting a prompt to an LLM. This leads to critical drawbacks:
1. **Non-Reproducibility**: The same resume submitted three times can yield 64%, 81%, and 72% depending on model temperature and sampling.
2. **Hallucination Risk**: LLMs frequently invent missing requirements or misunderstand negative assertions ("no experience with PHP" scored as knowing PHP).
3. **High Latency & Cost**: LLM inference costs \$0.01–\$0.05 per resume check with 2–5 second delays.
4. **Legal Compliance & Auditability**: Under hiring regulations (e.g., NYC Local Law 144, EU AI Act), automated employment decision tools must provide explainable, auditable criteria.

Smart Resume AI uses a **Deterministic 6-Factor Mathematical Engine** that computes scores in under 25 milliseconds with 100% auditability.

---

## 2. Six-Factor Mathematical Formula

The total ATS Match Score ($S_{ATS}$) is calculated on a scale of 0 to 100:

$$S_{ATS} = 0.35 \cdot S_{skill} + 0.20 \cdot S_{exp} + 0.15 \cdot S_{edu} + 0.10 \cdot S_{fmt} + 0.10 \cdot S_{proj} + 0.10 \cdot S_{kw}$$

### Factor 1: Technical Skill Overlap ($S_{skill}$ — Weight: 35%)
Calculated as the weighted Jaccard similarity between the target role's core canonical skills ($K_{target}$) and the candidate's extracted skills ($K_{resume}$):

$$S_{skill} = \min\left(100, \frac{|K_{target} \cap K_{resume}|}{|K_{target}|} \times 100\right)$$

If an employer specifies mandatory ("must-have") vs nice-to-have skills, mandatory skills carry $2\times$ weight.

### Factor 2: Experience Alignment ($S_{exp}$ — Weight: 20%)
Evaluates:
- Stated years of experience vs required minimum.
- Seniority indicators (e.g., "Lead", "Principal", "Senior", "Junior", "Intern") matching the target level.
- Chronological consistency and absence of unexplained long employment gaps.

### Factor 3: Education & Prerequisite Match ($S_{edu}$ — Weight: 15%)
Scored based on highest credential achieved:
- Exact degree level & major match (e.g., B.S. / M.S. Computer Science for Software Engineer): $100\%$
- Related technical field (e.g., Electrical Engineering, Mathematics, Physics): $85\%$
- Unrelated field with demonstrated certifications or portfolio evidence: $60\%$
- High school / no degree stated: $30\%$ (with compensatory boost if project evidence is high)

### Factor 4: ATS Layout & Parsing Hygiene ($S_{fmt}$ — Weight: 10%)
Assesses whether a standard corporate ATS parser (Taleo, Workday, Greenhouse) can parse the file without data loss:
- Standard section headers present (`Education`, `Experience`, `Skills`, `Projects`): $+40\%$
- Clean parseable text stream (absence of tables, text boxes, and complex multi-layered columns): $+30\%$
- Clean bullet formatting and contact information (Email, Phone, LinkedIn, GitHub): $+30\%$

### Factor 5: Project Quality & Quantitative Evidence ($S_{proj}$ — Weight: 10%)
Measures the depth and evidence of work:
- Presence of measurable metrics (e.g., "reduced latency by 45%", "served 10,000+ users"): $+40\%$
- Action-oriented verbs (e.g., *Architected*, *Implemented*, *Optimized*, *Engineered*): $+30\%$
- Live deployment or verifiable repository links (e.g., GitHub, live URL): $+30\%$

### Factor 6: Domain Keyword & Section Density ($S_{kw}$ — Weight: 10%)
Evaluates domain vocabulary saturation across all sections to confirm contextual familiarity without keyword stuffing.

---

## 3. Boundary Condition Test Suite
The scoring engine was subjected to edge-case stress testing:

| Test Scenario | Expected Outcome | Actual Score | Status |
| :--- | :--- | :--- | :--- |
| **Empty File / Corrupt PDF** | Graceful rejection with user-friendly error message | Score: 0 / Error caught | Pass |
| **Exact 100% Match Resume** | All 8/8 core skills present, senior title, MS degree, strong metrics | Score: 98.5% | Pass |
| **Complete Career Pivot** | Art history resume submitted for Senior DevOps role | Score: 18.2% | Pass |
| **Pasted Custom JD (No Static Role)** | Dynamic role inferred, parsed correctly | Score: 78.4% | Pass |
| **Keyword Stuffing Attempt** | White-on-white text, 50 repeated keywords | Flagged by layout penalty, capped at 65% | Pass |
