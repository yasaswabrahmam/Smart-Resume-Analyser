package com.smartresume.config;

import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class JobRolesConfig {

    public static class RoleDefinition {
        private String roleName;
        private String category;
        private String careerLevel; // e.g. "Graduate / Entry-Level", "Mid-Level", "Senior / Lead"
        private String description;
        private List<String> aliases;
        private List<String> coreSkills;
        private List<String> preferredSkills;
        private List<String> tools;
        private List<String> responsibilities;
        private String projectEvidence;
        private List<String> certifications;
        private List<String> prerequisites;
        private List<String> relatedRoles;

        // Legacy compatibility fields
        private List<String> requiredSkills;
        private List<String> sections;
        private List<String> recommendedTechnicalSkills;
        private List<String> recommendedSoftSkills;

        public RoleDefinition(String roleName, String category, String careerLevel, String description,
                              List<String> aliases, List<String> coreSkills, List<String> preferredSkills,
                              List<String> tools, List<String> responsibilities, String projectEvidence,
                              List<String> certifications, List<String> prerequisites, List<String> relatedRoles) {
            this.roleName = roleName;
            this.category = category;
            this.careerLevel = careerLevel;
            this.description = description;
            this.aliases = aliases != null ? aliases : Collections.emptyList();
            this.coreSkills = coreSkills != null ? coreSkills : Collections.emptyList();
            this.preferredSkills = preferredSkills != null ? preferredSkills : Collections.emptyList();
            this.tools = tools != null ? tools : Collections.emptyList();
            this.responsibilities = responsibilities != null ? responsibilities : Collections.emptyList();
            this.projectEvidence = projectEvidence;
            this.certifications = certifications != null ? certifications : Collections.emptyList();
            this.prerequisites = prerequisites != null ? prerequisites : Collections.emptyList();
            this.relatedRoles = relatedRoles != null ? relatedRoles : Collections.emptyList();

            // Set legacy compatibility fields
            this.requiredSkills = this.coreSkills;
            this.sections = List.of("Technical Skills", "Projects", "Work Experience", "Education");
            this.recommendedTechnicalSkills = this.tools;
            this.recommendedSoftSkills = List.of("Communication", "Problem-solving", "Team collaboration");
        }

        // Backward compatibility constructor
        public RoleDefinition(String roleName, String category, String description,
                              List<String> requiredSkills, List<String> sections,
                              List<String> recommendedTechnicalSkills, List<String> recommendedSoftSkills) {
            this(roleName, category, "Entry to Mid-Level", description,
                    List.of(roleName), requiredSkills, Collections.emptyList(),
                    recommendedTechnicalSkills, List.of("Design and maintain scalable software modules", "Collaborate across cross-functional engineering squads"),
                    "Deploy a complete GitHub project demonstrating core technologies with clear README and unit tests.",
                    Collections.emptyList(), List.of("Object-Oriented Programming", "Data Structures & Algorithms"),
                    Collections.emptyList());
        }

        public String getRoleName() { return roleName; }
        public String getCategory() { return category; }
        public String getCareerLevel() { return careerLevel; }
        public String getDescription() { return description; }
        public List<String> getAliases() { return aliases; }
        public List<String> getCoreSkills() { return coreSkills; }
        public List<String> getPreferredSkills() { return preferredSkills; }
        public List<String> getTools() { return tools; }
        public List<String> getResponsibilities() { return responsibilities; }
        public String getProjectEvidence() { return projectEvidence; }
        public List<String> getCertifications() { return certifications; }
        public List<String> getPrerequisites() { return prerequisites; }
        public List<String> getRelatedRoles() { return relatedRoles; }

        public List<String> getRequiredSkills() { return requiredSkills != null ? requiredSkills : coreSkills; }
        public List<String> getSections() { return sections; }
        public List<String> getRecommendedTechnicalSkills() { return recommendedTechnicalSkills != null ? recommendedTechnicalSkills : tools; }
        public List<String> getRecommendedSoftSkills() { return recommendedSoftSkills; }
    }

    private static final Map<String, Map<String, RoleDefinition>> JOB_ROLES = new LinkedHashMap<>();
    private static final Map<String, RoleDefinition> ROLES_BY_NAME = new LinkedHashMap<>();

    static {
        // =========================================================================
        // 1. Software and Application Development
        // =========================================================================
        Map<String, RoleDefinition> softwareRoles = new LinkedHashMap<>();

        addRole(softwareRoles, new RoleDefinition(
                "Full Stack Developer", "Software and Application Development", "Entry to Mid-Level",
                "Architect and deliver responsive user interfaces and resilient backend microservices.",
                List.of("Full Stack Engineer", "Web Application Developer", "Software Engineer"),
                List.of("Java", "JavaScript", "React", "Spring Boot", "SQL", "HTML5", "CSS3", "REST APIs"),
                List.of("Docker", "TypeScript", "GraphQL", "Tailwind CSS", "Redis"),
                List.of("Git", "Postman", "Maven", "VS Code", "IntelliJ IDEA"),
                List.of("Implement domain-driven RESTful APIs and UI workflows", "Optimize database query execution and caching", "Build modular, reusable React components"),
                "Full-stack e-commerce or booking platform with JWT auth, payment gateway integration, and Dockerized deployment.",
                List.of("Oracle Certified Java Developer", "AWS Certified Developer"),
                List.of("OOP Principles", "Data Structures & Algorithms", "Basic Relational Database Concepts"),
                List.of("Backend Developer", "Frontend Developer", "DevOps Engineer")
        ));

        addRole(softwareRoles, new RoleDefinition(
                "Backend Developer", "Software and Application Development", "Graduate to Mid-Level",
                "Design scalable server-side systems, business logic, transactions, and persistence layers.",
                List.of("Backend Engineer", "Server-Side Developer", "API Engineer"),
                List.of("Java", "Python", "Spring Boot", "MySQL", "REST APIs", "Microservices", "Hibernate"),
                List.of("Kafka", "Redis", "Docker", "PostgreSQL", "Elasticsearch"),
                List.of("Git", "IntelliJ IDEA", "Postman", "Docker Desktop"),
                List.of("Architect scalable distributed services and API contracts", "Ensure database normalization and transaction reliability", "Implement unit and integration testing suites"),
                "High-throughput microservices architecture with Redis caching, Kafka asynchronous messaging, and MySQL clustering.",
                List.of("Spring Professional Certification", "AWS Certified Solutions Architect Associate"),
                List.of("Core Java / Python Syntax", "Relational Database Normalization", "HTTP Protocol"),
                List.of("Full Stack Developer", "Cloud Architect", "Site Reliability Engineer")
        ));

        addRole(softwareRoles, new RoleDefinition(
                "Frontend Developer", "Software and Application Development", "Internship / Entry-Level",
                "Craft modern, responsive web user experiences and interactive client-side applications.",
                List.of("Frontend Engineer", "UI Developer", "React Developer"),
                List.of("JavaScript", "TypeScript", "React", "HTML5", "CSS3", "Responsive Design"),
                List.of("Next.js", "Tailwind CSS", "Redux", "Webpack / Vite", "Jest"),
                List.of("VS Code", "Chrome DevTools", "Figma", "npm / yarn"),
                List.of("Translate design mockups into pixel-perfect responsive web pages", "Optimize core web vitals and bundle load times", "Manage client state and API integration"),
                "Interactive SaaS dashboard with dynamic charts, dark-mode toggle, responsive mobile views, and Lighthouse score > 95.",
                List.of("Meta Frontend Developer Professional Certificate"),
                List.of("HTML Semantics", "Modern CSS Flexbox/Grid", "JavaScript ES6+"),
                List.of("Full Stack Developer", "UI/UX Designer")
        ));

        JOB_ROLES.put("Software and Application Development", softwareRoles);

        // =========================================================================
        // 2. AI, Machine Learning and Data
        // =========================================================================
        Map<String, RoleDefinition> aiDataRoles = new LinkedHashMap<>();

        addRole(aiDataRoles, new RoleDefinition(
                "Python AI & ML Engineer", "AI, Machine Learning and Data", "Graduate to Mid-Level",
                "Build NLP pipelines, fine-tune transformer models, and orchestrate RAG inference microservices.",
                List.of("Machine Learning Engineer", "AI Developer", "NLP Engineer"),
                List.of("Python", "FastAPI", "PyTorch", "Scikit-learn", "spaCy", "Sentence-Transformers", "RAG"),
                List.of("HuggingFace", "LangChain", "Qdrant", "Docker", "MLflow"),
                List.of("Jupyter Notebooks", "PyCharm", "Git", "Google Colab"),
                List.of("Fine-tune embedding and classification models on domain datasets", "Expose model inference endpoints via high-speed asynchronous REST APIs", "Evaluate model precision, recall, F1-scores, and inference latency"),
                "Retrieval-Augmented Generation (RAG) knowledge engine using vector embeddings, hybrid cosine search, and grounded LLM output.",
                List.of("TensorFlow Developer Certificate", "DeepLearning.AI Specialization"),
                List.of("Linear Algebra", "Calculus & Probability", "Python OOP & Vectorization"),
                List.of("Data Scientist", "Data Analyst", "Backend Developer")
        ));

        addRole(aiDataRoles, new RoleDefinition(
                "Data Scientist", "AI, Machine Learning and Data", "Mid-Level",
                "Analyze complex datasets, extract predictive signals, and formulate statistical hypothesis tests.",
                List.of("Applied Scientist", "Quantitative Analyst", "Research Scientist"),
                List.of("Python", "R", "SQL", "Statistics", "Machine Learning", "Pandas", "NumPy"),
                List.of("Deep Learning", "Tableau", "Apache Spark", "BigQuery"),
                List.of("Jupyter", "Git", "Anaconda", "SQL Workbench"),
                List.of("Perform exploratory data analysis and feature engineering", "Train predictive regressors and classifiers with cross-validation", "Present actionable insights to executive business stakeholders"),
                "Customer churn predictive model with automated feature engineering, ROC-AUC validation, and business uplift analysis.",
                List.of("IBM Data Science Professional Certificate"),
                List.of("Probability & Inferential Statistics", "Data Wrangling", "SQL Joins"),
                List.of("Data Analyst", "Python AI & ML Engineer")
        ));

        addRole(aiDataRoles, new RoleDefinition(
                "Data Analyst", "AI, Machine Learning and Data", "Internship / Entry-Level",
                "Transform raw business data into actionable dashboards, KPIs, and operational reports.",
                List.of("Business Intelligence Analyst", "Reporting Analyst", "BI Developer"),
                List.of("SQL", "Excel", "Python", "Data Visualization", "Tableau", "Power BI"),
                List.of("ETL Pipelines", "Statistics", "Snowflake", "DAX"),
                List.of("Power BI Desktop", "Excel Pivot Tables", "PostgreSQL"),
                List.of("Author complex SQL aggregations, window functions, and views", "Build interactive executive dashboards tracking key performance metrics", "Identify operational bottlenecks through trend analysis"),
                "End-to-end sales analytics dashboard in Power BI backed by automated SQL ETL scripts and statistical cohorts.",
                List.of("Microsoft Certified: Power BI Data Analyst Associate"),
                List.of("Basic SQL", "Spreadsheet Modeling", "Basic Descriptive Statistics"),
                List.of("Data Scientist", "Product Manager")
        ));

        JOB_ROLES.put("AI, Machine Learning and Data", aiDataRoles);

        // =========================================================================
        // 3. Cloud, DevOps and Infrastructure
        // =========================================================================
        Map<String, RoleDefinition> cloudRoles = new LinkedHashMap<>();

        addRole(cloudRoles, new RoleDefinition(
                "Cloud DevOps Engineer", "Cloud, DevOps and Infrastructure", "Graduate to Mid-Level",
                "Automate multi-region cloud environments with Terraform, orchestrate Kubernetes, and build zero-downtime CI/CD.",
                List.of("DevOps Engineer", "Cloud Engineer", "Infrastructure Engineer"),
                List.of("AWS", "Docker", "Kubernetes", "CI/CD", "Terraform", "Linux", "Bash"),
                List.of("Prometheus", "Grafana", "Ansible", "Helm", "GitLab CI"),
                List.of("AWS Console", "Terminal / Bash", "Docker CLI", "kubectl"),
                List.of("Automate infrastructure provisioning using Infrastructure-as-Code", "Construct automated build, test, and release CI/CD workflows", "Monitor cluster health, uptime, and resource saturation"),
                "Production Kubernetes cluster provisioned via Terraform with Prometheus monitoring and automated GitHub Actions deployment.",
                List.of("AWS Certified Solutions Architect", "CKA (Certified Kubernetes Administrator)"),
                List.of("Linux Operating System Fundamentals", "Computer Networking (TCP/IP, DNS)", "Git Version Control"),
                List.of("Site Reliability Engineer", "Cloud Architect")
        ));

        addRole(cloudRoles, new RoleDefinition(
                "Site Reliability Engineer", "Cloud, DevOps and Infrastructure", "Mid to Senior Level",
                "Ensure production system reliability, minimize mean time to recovery (MTTR), and automate incident response.",
                List.of("SRE", "Reliability Engineer", "Production Engineer"),
                List.of("Linux", "Python", "Go", "Kubernetes", "Monitoring", "Incident Response", "Networking"),
                List.of("Chaos Engineering", "Distributed Tracing", "OpenTelemetry", "SLO / SLI Management"),
                List.of("Grafana", "Datadog", "PagerDuty", "Jaeger"),
                List.of("Define and track Service Level Objectives (SLOs) and Error Budgets", "Conduct blameless post-mortems and root-cause analyses", "Automate manual operational toil with scripting"),
                "High-availability multi-region active-passive failover system with automated DNS routing and health checks.",
                List.of("Google Cloud Certified Professional Cloud DevOps Engineer"),
                List.of("Distributed Systems Concepts", "OS Internals & Kernel Metrics", "Shell Scripting"),
                List.of("Cloud DevOps Engineer", "Backend Developer")
        ));

        JOB_ROLES.put("Cloud, DevOps and Infrastructure", cloudRoles);

        // =========================================================================
        // 4. Cybersecurity
        // =========================================================================
        Map<String, RoleDefinition> cyberRoles = new LinkedHashMap<>();

        addRole(cyberRoles, new RoleDefinition(
                "Security Analyst", "Cybersecurity", "Entry to Mid-Level",
                "Monitor security operations center (SOC) alerts, analyze threat vectors, and protect enterprise networks.",
                List.of("SOC Analyst", "Information Security Specialist", "Cyber Defense Analyst"),
                List.of("Network Security", "Threat Detection", "SIEM Tools", "Incident Response", "Vulnerability Assessment"),
                List.of("Wireshark", "Splunk", "Firewalls", "Identity & Access Management (IAM)", "NIST Framework"),
                List.of("Splunk SIEM", "Wireshark", "Nmap", "Metasploit"),
                List.of("Triage and investigate real-time security alerts from SIEM platforms", "Perform vulnerability scanning and prioritize patching advisories", "Enforce compliance with security policies and regulatory baselines"),
                "Enterprise SOC analysis lab documenting alert triage, network packet analysis with Wireshark, and incident response playbook.",
                List.of("CompTIA Security+", "Certified Ethical Hacker (CEH)"),
                List.of("TCP/IP Networking", "Operating System Security (Windows/Linux)", "Common Cryptographic Protocols"),
                List.of("Penetration Tester", "Cloud Security Specialist")
        ));

        addRole(cyberRoles, new RoleDefinition(
                "Penetration Tester", "Cybersecurity", "Mid-Level",
                "Probe web applications and networks to identify exploitable security vulnerabilities before adversaries do.",
                List.of("Ethical Hacker", "Red Teamer", "Offensive Security Engineer"),
                List.of("Ethical Hacking", "OWASP Top 10", "Burp Suite", "Network Pentesting", "Python", "Bash"),
                List.of("Reverse Engineering", "Privilege Escalation", "Exploit Development", "Web Security"),
                List.of("Kali Linux", "Burp Suite Professional", "Metasploit", "Nmap"),
                List.of("Execute authorized penetration tests across web applications and APIs", "Document technical findings with reproducible proof-of-concept steps", "Collaborate with development teams to remediate identified flaws"),
                "Documented penetration testing audit on a deliberately vulnerable web application detailing OWASP remediation.",
                List.of("OSCP (Offensive Security Certified Professional)", "eJPT"),
                List.of("Web Application Architecture (HTTP, Cookies, Sessions)", "Scripting in Python/Bash"),
                List.of("Security Analyst", "Backend Developer")
        ));

        JOB_ROLES.put("Cybersecurity", cyberRoles);

        // =========================================================================
        // 5. Testing and Quality Engineering
        // =========================================================================
        Map<String, RoleDefinition> qaRoles = new LinkedHashMap<>();

        addRole(qaRoles, new RoleDefinition(
                "QA Automation Engineer", "Testing and Quality Engineering", "Graduate to Mid-Level",
                "Architect robust automated end-to-end and API testing suites for continuous delivery pipelines.",
                List.of("SDET", "Software Development Engineer in Test", "Test Automation Engineer"),
                List.of("Java", "Python", "Selenium", "JUnit", "TestNG", "API Testing", "Postman", "Git"),
                List.of("Cypress", "Playwright", "RestAssured", "CI/CD Integration", "Performance Testing (JMeter)"),
                List.of("Eclipse / IntelliJ", "Postman", "Jenkins", "Selenium WebDriver"),
                List.of("Design and maintain scalable Page Object Model test automation frameworks", "Automate regression suites for web applications and microservices APIs", "Integrate automated test runs into CI/CD build gates"),
                "Production-grade test automation framework in Java with Selenium, RestAssured API tests, and Allure HTML reporting.",
                List.of("ISTQB Certified Tester", "Selenium Automation Specialist"),
                List.of("Core Programming Language (Java or Python)", "Software Testing Life Cycle (STLC)"),
                List.of("Full Stack Developer", "Backend Developer")
        ));

        JOB_ROLES.put("Testing and Quality Engineering", qaRoles);

        // =========================================================================
        // 6. Product and Business Analysis
        // =========================================================================
        Map<String, RoleDefinition> productRoles = new LinkedHashMap<>();

        addRole(productRoles, new RoleDefinition(
                "Product Manager", "Product and Business Analysis", "Mid to Senior Level",
                "Define product strategy, lead customer discovery, prioritize feature roadmaps, and align cross-functional squads.",
                List.of("Associate Product Manager (APM)", "Technical Product Manager", "Product Owner"),
                List.of("Product Strategy", "User Stories", "Agile & Scrum", "Roadmapping", "Market Research", "Wireframing"),
                List.of("Jira", "Mixpanel", "SQL", "A/B Testing", "Financial Modeling"),
                List.of("Jira Software", "Figma", "Confluence", "Notion"),
                List.of("Synthesize customer feedback and telemetry into prioritized product backlogs", "Lead sprint planning and backlog refinement with engineering squads", "Define and monitor North Star metrics and conversion funnels"),
                "Comprehensive Product Requirement Document (PRD) with user journey maps, wireframes, and Go-To-Market plan.",
                List.of("Certified Scrum Product Owner (CSPO)", "Pragmatic Institute Certified"),
                List.of("User Empathy", "Agile Software Methodologies", "Basic Business Economics"),
                List.of("Data Analyst", "UI/UX Designer")
        ));

        JOB_ROLES.put("Product and Business Analysis", productRoles);

        // =========================================================================
        // 7. UI/UX and Design
        // =========================================================================
        Map<String, RoleDefinition> designRoles = new LinkedHashMap<>();

        addRole(designRoles, new RoleDefinition(
                "UI/UX Product Designer", "UI/UX and Design", "Graduate to Mid-Level",
                "Conduct user research, design interactive prototypes, and establish scalable design systems.",
                List.of("Product Designer", "User Experience Designer", "Interaction Designer"),
                List.of("Figma", "User Research", "Wireframing", "Prototyping", "Design Systems", "Usability Testing"),
                List.of("Adobe XD", "Information Architecture", "HTML/CSS Basics", "Micro-interactions"),
                List.of("Figma", "FigJam", "Miro", "Lottie"),
                List.of("Lead generative user research interviews and synthesize qualitative empathy maps", "Build interactive clickable high-fidelity prototypes in Figma", "Maintain consistent design tokens and atomic component libraries"),
                "End-to-end design case study showcasing user research, low-fidelity wireframes, high-fidelity prototypes, and design system.",
                List.of("Google UX Design Professional Certificate"),
                List.of("Visual Hierarchy", "Typography & Color Theory", "Human-Centered Design Principles"),
                List.of("Frontend Developer", "Product Manager")
        ));

        JOB_ROLES.put("UI/UX and Design", designRoles);

        // =========================================================================
        // 8. Mobile and Embedded Development
        // =========================================================================
        Map<String, RoleDefinition> mobileRoles = new LinkedHashMap<>();

        addRole(mobileRoles, new RoleDefinition(
                "Mobile App Engineer", "Mobile and Embedded Development", "Graduate to Mid-Level",
                "Develop high-performance natively compiled mobile applications across Android and iOS platforms.",
                List.of("Android Developer", "Flutter Developer", "iOS Developer"),
                List.of("Flutter", "Dart", "Kotlin", "Android SDK", "REST APIs", "Mobile UI/UX"),
                List.of("Swift", "Firebase", "State Management (Bloc / Provider)", "SQLite / Room", "CI/CD for Mobile"),
                List.of("Android Studio", "Xcode", "VS Code", "Firebase Console"),
                List.of("Engineer cross-platform mobile apps using Flutter and Dart", "Integrate device sensors, push notifications, and offline data sync", "Optimize app rendering frame rates and memory footprint"),
                "Cross-platform mobile application published on GitHub with offline caching, state management, and biometric auth.",
                List.of("Google Associate Android Developer Certification"),
                List.of("Object-Oriented Programming", "Mobile Lifecycle Concepts", "Asynchronous Programming"),
                List.of("Frontend Developer", "Full Stack Developer")
        ));

        JOB_ROLES.put("Mobile and Embedded Development", mobileRoles);
    }

    private static void addRole(Map<String, RoleDefinition> categoryMap, RoleDefinition role) {
        categoryMap.put(role.getRoleName(), role);
        ROLES_BY_NAME.put(role.getRoleName(), role);
    }

    public static Map<String, Map<String, RoleDefinition>> getJobRoles() {
        return JOB_ROLES;
    }

    public static RoleDefinition getRoleByName(String roleName) {
        return ROLES_BY_NAME.get(roleName);
    }

    public static List<String> getAllRoleNames() {
        return new ArrayList<>(ROLES_BY_NAME.keySet());
    }

    public static Set<String> getCategories() {
        return JOB_ROLES.keySet();
    }

    public static List<String> getRolesForCategory(String category) {
        Map<String, RoleDefinition> roles = JOB_ROLES.get(category);
        if (roles != null) {
            return new ArrayList<>(roles.keySet());
        }
        return Collections.emptyList();
    }

    public static List<RoleDefinition> getAllRoles() {
        return new ArrayList<>(ROLES_BY_NAME.values());
    }
}
