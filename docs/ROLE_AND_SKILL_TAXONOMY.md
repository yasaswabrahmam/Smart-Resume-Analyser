# Role and Skill Taxonomy — Canonical Intelligence System

## 1. Architecture Overview
Smart Resume AI implements a multi-tier Career Role and Canonical Skill Taxonomy aligned with international standards:
- **ESCO** (European Skills, Competences, Qualifications and Occupations)
- **O*NET** (Occupational Information Network, U.S. Department of Labor)

The taxonomy is codified in [`JobRolesConfig.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/JobRolesConfig.java) and exposed through the Career Explorer ([`CareerExplorerController.java`](file:///c:/Users/yasas/OneDrive/Documents/Desktop/D3js%20program/AI%20Smart%20Resume%20Analyzer.java/Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/controller/CareerExplorerController.java)).

---

## 2. Supported Role Families & Levels (32+ Roles)

### 1. Software Engineering & Systems
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **Full Stack Developer** | Entry to Mid | Java, JavaScript, React, Spring Boot, SQL, HTML5, CSS3, REST APIs | Multi-tenant SaaS with authentication and relational data persistence |
| **Backend Engineer** | Entry to Mid | Java, Spring Boot, Microservices, PostgreSQL, Redis, Docker, REST APIs | Event-driven microservices architecture with Kafka / Redis caching |
| **Frontend Engineer** | Entry to Mid | JavaScript, TypeScript, React, Next.js, HTML5, CSS3, Tailwind CSS | High-performance dashboard with responsive UI and state management |
| **Mobile App Developer** | Entry to Mid | Kotlin, Swift, Flutter, Android, iOS, REST APIs, Git | Offline-first mobile application with biometric login and local sync |

### 2. AI, Machine Learning & Data Engineering
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **Machine Learning Engineer** | Entry to Senior | Python, PyTorch, TensorFlow, Scikit-Learn, MLOps, Docker, Pandas | End-to-end ML pipeline with model serving via FastAPI and automated drift monitoring |
| **Data Engineer** | Mid-Level | Python, SQL, Apache Spark, Kafka, Airflow, Snowflake, AWS | Distributed ETL data lake pipeline processing real-time streaming telemetry |
| **Data Scientist** | Entry to Mid | Python, R, SQL, Pandas, NumPy, Statistics, Machine Learning | Predictive analytics model with exploratory data analysis and executive reporting |
| **AI Research Scientist** | Senior / Lead | Python, Deep Learning, Transformers, PyTorch, NLP, Computer Vision | Fine-tuned LLM or multimodal vision-language model evaluated against standard benchmarks |

### 3. Cloud & Infrastructure / DevOps
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **DevOps & Cloud Engineer** | Entry to Mid | AWS, Docker, Kubernetes, Terraform, Linux, CI/CD, Git, Bash | GitOps multi-environment deployment using Terraform and Kubernetes Helm charts |
| **Cloud Solutions Architect** | Senior / Lead | AWS, Azure, GCP, Cloud Architecture, Microservices, Kubernetes | Enterprise cloud migration plan with high-availability multi-region redundancy |
| **Site Reliability Engineer (SRE)** | Mid to Senior | Linux, Kubernetes, Prometheus, Grafana, Python, Go, Incident Response | Automated SLO/SLI observability dashboard with self-healing alert webhooks |

### 4. Cybersecurity & Information Security
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **Cybersecurity Analyst** | Entry-Level | Network Security, SIEM, Wireshark, Linux, Python, Incident Response | Threat hunting lab simulating log triage and mitigation in Splunk/ELK |
| **Security Engineer** | Mid-Level | OWASP, Penetration Testing, Python, Cryptography, Linux, Vulnerability Management | Automated DevSecOps pipeline with SAST/DAST scanning and secrets detection |
| **Cloud Security Specialist** | Senior | AWS Security, IAM, Terraform, Compliance (SOC2/ISO), Kubernetes Security | Zero-trust IAM policy architecture with automated drift remediation |

### 5. Product, Project & Consulting
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **Technical Product Manager** | Mid to Senior | Product Roadmapping, Agile/Scrum, User Stories, SQL, System Architecture | Comprehensive Product Requirements Document (PRD) with user journey maps and metrics |
| **IT Business Analyst** | Entry to Mid | Requirements Gathering, SQL, BPMN, UML, Agile, Data Analysis | End-to-end business process re-engineering spec with BPMN diagrams and wireframes |
| **Scrum Master / Agile Coach** | Mid-Level | Agile, Scrum, Kanban, JIRA, Facilitation, Servant Leadership | Sprint retrospective playbook with team velocity improvement framework |

### 6. Design & Human-Computer Interaction
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **UI/UX Designer** | Entry to Mid | Figma, Wireframing, User Research, Prototyping, Design Systems | Complete cross-platform mobile and web design system with interactive Figma prototype |
| **Product Designer** | Mid to Senior | Figma, User Research, Design Systems, Usability Testing, Prototyping | Full product discovery case study validating user pain points through usability testing |

### 7. Financial Technology & Quantitative Analysis
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **Fintech Software Engineer** | Mid-Level | Java, C++, Python, SQL, REST APIs, Microservices, Financial Protocols | High-throughput payment transaction ledger with double-entry bookkeeping |
| **Quantitative Analyst (Quant)** | Senior | Python, C++, Statistics, Stochastic Calculus, NumPy, Pandas | Backtested algorithmic portfolio optimization strategy with Sharpe ratio analysis |

### 8. Health Informatics & BioTech
| Role Title | Level | Core Skills | Target Portfolio Project |
| :--- | :--- | :--- | :--- |
| **Health Informatics Specialist** | Entry to Mid | Python, SQL, HL7, FHIR, Healthcare Compliance (HIPAA), Data Analysis | FHIR-compliant electronic health record (EHR) data extraction and reporting module |

---

## 3. Canonical Skill Vocabulary (60+ Skills)
The canonical vocabulary prevents keyword fragmentation (e.g., treating `reactjs`, `React.js`, and `React` identically):

- **Languages**: `Java`, `Python`, `JavaScript`, `TypeScript`, `C++`, `C#`, `Go`, `Rust`, `SQL`, `Bash`, `Kotlin`, `Swift`, `R`, `HTML5`, `CSS3`
- **Frameworks & Libraries**: `Spring Boot`, `React`, `Node.js`, `Angular`, `Vue.js`, `Next.js`, `Django`, `FastAPI`, `Flask`, `Express`, `PyTorch`, `TensorFlow`, `Scikit-Learn`, `Pandas`, `NumPy`
- **Cloud & DevOps**: `AWS`, `Azure`, `GCP`, `Docker`, `Kubernetes`, `Terraform`, `CI/CD`, `Linux`, `Prometheus`, `Grafana`, `Git`, `Helm`
- **Databases & Storage**: `PostgreSQL`, `MySQL`, `MongoDB`, `Redis`, `Elasticsearch`, `Kafka`, `Snowflake`
- **Architecture & Security**: `REST APIs`, `Microservices`, `GraphQL`, `System Architecture`, `Network Security`, `SIEM`, `OWASP`, `Penetration Testing`, `Cryptography`, `IAM`
- **Product & Design**: `Agile/Scrum`, `Figma`, `Wireframing`, `User Research`, `Design Systems`, `Product Roadmapping`

---

## 4. Dynamic Job Description Inferrer (Zero Static Failure)
When a user encounters a niche, hybrid, or unlisted job opening, the platform does not fail. 

The Dynamic Inferrer (`JobRolesConfig.inferRoleFromJobDescription`) executes:
1. **Title Extraction / Fallback**: Uses the supplied target title or infers a canonical name from keywords.
2. **Canonical Skill Matching**: Matches the job description text against the 60+ canonical skills using boundary-safe regular expressions.
3. **Domain Classification**: Computes overlap scores across all 8 role families to assign the appropriate category.
4. **Dynamic Role Registration**: Generates a runtime `RoleDefinition` with recommended project benchmarks, education prerequisites, and typical interview questions.
5. **Interactive UI Flash**: Dispatches the user to the Career Explorer with a success banner displaying the extracted competencies.

---

## 5. REST API Specifications
- **Endpoint**: `GET /explore/api/role/{roleId}`
- **Response Format**: `application/json`
- **Status Codes**:
  - `200 OK`: Returns structured JSON metadata for the specified role.
  - `404 Not Found`: Returned if the role ID is unknown.


---

## 6. Official Taxonomy References & Administrative Governance

1. **Official Occupational Foundations**:
   - **ESCO API**: Aligned with the European Skills, Competences, Qualifications and Occupations taxonomy ([ESCO Web Service API](https://esco.ec.europa.eu/en/use-esco/use-esco-services-api/esco-web-service-api)).
   - **O*NET Database**: Occupational descriptors cross-referenced with U.S. Department of Labor occupational profiles ([O*NET Database](https://www.onetcenter.org/database.html)).
   - **AICTE & National Career Service (India)**: Technical terminology aligned with India-specific requirements and university internship structures ([AICTE Internship Portal](https://internship.aicte-india.org/)).
2. **Administrative Role Management**:
   - Authorized administrators can view, activate, deactivate, or register new custom role definitions at runtime via `JobRolesConfig.setRoleActive()` and `JobRolesConfig.addCustomRole()` without modifying source code or restarting the application.
