# Skill Gap & Learning Recommendation Design

**Smart Resume AI — Remediation & YouTube Pedagogical Engine**  
*Document Version: 2.0.0*  
*Generated: October 2026*

---

## 1. Pedagogical Architecture: Closing the Gap

Most resume checkers end their interaction once a score is computed. If an applicant discovers they lack "Kubernetes" or "Spring Cloud", they are left on their own.

**Smart Resume AI treats the ATS audit not as an end point, but as the starting gate for career advancement.**

```
[ Resume Parsed ] ──▶ [ Evaluated Against Role ] ──▶ [ Categorized into 3 Buckets ]
                                                              │
                     ┌────────────────────────────────────────┼────────────────────────┐
                     ▼                                        ▼                        ▼
           [ Demonstrated Skills ]                 [ Weakly Evidenced ]        [ Missing Skills ]
           • Verified with context                 • Keyword-only mention      • Absent from text
           • Highlighted in Green                  • Needs quantified metric   • Red alert badge
                                                              │                        │
                                                              └───────────┬────────────┘
                                                                          ▼
                                                          [ Curated Remediation Hub ]
                                                          • Targeted YouTube Tutorials
                                                          • Official Documentation Guides
                                                          • Portfolio Project Blueprints
```

---

## 2. Skill Classification Engine (`SkillGapService.java`)

The `SkillGapService` analyzes resume text against verified requirements for 6 target tech roles:
1. **Full Stack Java Developer**
2. **Python AI & ML Engineer**
3. **Cloud DevOps & SRE Architect**
4. **Data Engineer & Analytics Specialist**
5. **Frontend React & UI Engineer**
6. **Mobile App Engineer (Flutter/Android)**

### Three-Tier Skill Partitioning:
- **Demonstrated Skills (Green)**: The skill is detected AND appears within a context window featuring active action verbs or project descriptions.
- **Weakly Evidenced Skills (Yellow)**: The skill is present in the resume (e.g. in a generic "Skills List" at the bottom), but has zero supporting metrics, project bullets, or quantified achievements.
- **Missing Skills (Red)**: The skill is standard industry criteria for the chosen role, but does not appear anywhere in the resume text.

---

## 3. Curated YouTube Learning Catalog

Rather than sending users to random YouTube search results where videos may be outdated or promotional, `SkillGapService.java` maintains a verified pedagogical database of premier computer science and software engineering educators:

| Technology | Curated Video Title | Channel | Duration | Pedagogical Rationale |
|---|---|---|---|---|
| **Spring Boot** | *Spring Boot 3 Full Course — 2024* | Amigoscode | 4h 15m | Production-ready microservices, security, and JPA architecture. |
| **Microservices** | *Microservices with Spring Boot & Spring Cloud* | freeCodeCamp.org | 5h 30m | Distributed tracing, Eureka discovery, API gateways, and resilience. |
| **Docker** | *Docker Tutorial for Beginners* | TechWorld with Nana | 2h 45m | Container fundamentals, Dockerfiles, multi-stage builds, and Compose. |
| **Kubernetes** | *Kubernetes Course for Beginners* | freeCodeCamp.org | 4h 10m | Pods, Deployments, Services, Ingress, and cluster orchestration. |
| **FastAPI** | *FastAPI Course for Beginners* | freeCodeCamp.org | 3h 20m | High-performance async Python APIs, Pydantic validation, OpenAPI. |
| **PyTorch** | *PyTorch for Deep Learning Bootcamp* | Daniel Bourke | 25h 00m | Tensors, neural networks, computer vision, and NLP model training. |
| **RAG** | *Retrieval Augmented Generation (RAG) Explained* | IBM Technology | 15m | Vector databases, embeddings, chunking strategies, and generation. |
| **AWS** | *AWS Certified Cloud Practitioner Course* | freeCodeCamp.org | 13h 00m | Core cloud services, VPCs, IAM security, EC2, S3, and RDS. |
| **Terraform** | *Terraform Course — Automate your Infrastructure* | Anton Putra | 2h 15m | Infrastructure as Code (IaC), state management, and cloud automation. |
| **React** | *React Full Course for Beginners* | Dave Gray | 8h 30m | Hooks, state management, components, and modern full-stack workflows. |
| **MySQL** | *MySQL Database — Full Course* | freeCodeCamp.org | 3h 10m | Relational schemas, indexing strategies, normalization, transactions. |

---

## 4. 8-Week Structured Roadmap (`CareerRoadmapService.java`)

To eliminate overwhelm, skills are structured into an actionable **8-Week Trajectory** divided into 4 bi-weekly sprints:
- **Weeks 1–2: Foundational Mastery & Core Frameworks**: Syntax, core libraries, and fundamental patterns.
- **Weeks 3–4: Enterprise Integration & Persistence**: Databases, ORMs, caching, and REST API design.
- **Weeks 5–6: Productionization, Containers & Cloud**: Dockerizing services, CI/CD pipelines, and cloud deployment.
- **Weeks 7–8: Portfolio Capstone & Interview Readiness**: Developing an end-to-end GitHub capstone project and practicing technical interview scenarios.
