# Python script to generate comprehensive JobRolesConfig.java covering all requested role families and roles

code = '''package com.smartresume.config;

import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class JobRolesConfig {

    public static class RoleDefinition {
        private String roleId;
        private String roleName;
        private String category;
        private String domainIndustry;
        private String careerLevel;
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
        private String marketContext;
        private String taxonomyVersion;
        private boolean active = true;

        // Legacy compatibility fields
        private List<String> requiredSkills;
        private List<String> sections;
        private List<String> recommendedTechnicalSkills;
        private List<String> recommendedSoftSkills;

        public RoleDefinition(String roleId, String roleName, String category, String domainIndustry,
                              String careerLevel, String description, List<String> aliases,
                              List<String> coreSkills, List<String> preferredSkills, List<String> tools,
                              List<String> responsibilities, String projectEvidence,
                              List<String> certifications, List<String> prerequisites, List<String> relatedRoles,
                              String marketContext) {
            this.roleId = roleId != null ? roleId : roleName.toLowerCase().replaceAll("[^a-z0-9]+", "-");
            this.roleName = roleName;
            this.category = category;
            this.domainIndustry = domainIndustry != null ? domainIndustry : "Information Technology";
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
            this.marketContext = marketContext != null ? marketContext : "High demand across Indian tech hubs (Bangalore, Hyderabad, Pune) and global remote teams.";
            this.taxonomyVersion = "v2.5.0-esco-onet-aligned";
            this.active = true;

            // Legacy compatibility
            this.requiredSkills = this.coreSkills;
            this.sections = List.of("Technical Skills", "Projects", "Work Experience", "Education");
            this.recommendedTechnicalSkills = this.tools;
            this.recommendedSoftSkills = List.of("Communication", "Analytical Problem-Solving", "Agile Collaboration");
        }

        // Backward compatibility constructor
        public RoleDefinition(String roleName, String category, String careerLevel, String description,
                              List<String> aliases, List<String> coreSkills, List<String> preferredSkills,
                              List<String> tools, List<String> responsibilities, String projectEvidence,
                              List<String> certifications, List<String> prerequisites, List<String> relatedRoles) {
            this(roleName.toLowerCase().replaceAll("[^a-z0-9]+", "-"), roleName, category, "Information Technology",
                 careerLevel, description, aliases, coreSkills, preferredSkills, tools, responsibilities,
                 projectEvidence, certifications, prerequisites, relatedRoles, null);
        }

        // Legacy 7-parameter constructor
        public RoleDefinition(String roleName, String category, String description,
                              List<String> requiredSkills, List<String> sections,
                              List<String> recommendedTechnicalSkills, List<String> recommendedSoftSkills) {
            this(roleName, category, "Entry to Mid-Level", description,
                 List.of(), requiredSkills, List.of(), recommendedTechnicalSkills,
                 List.of("Design and maintain scalable software modules", "Participate in agile sprints"),
                 "Demonstrable portfolio application with documented architecture.",
                 List.of(), List.of("Computer Science Fundamentals"), List.of());
        }

        public String getRoleId() { return roleId; }
        public String getRoleName() { return roleName; }
        public String getCategory() { return category; }
        public String getDomainIndustry() { return domainIndustry; }
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
        public String getMarketContext() { return marketContext; }
        public String getTaxonomyVersion() { return taxonomyVersion; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }

        public List<String> getRequiredSkills() { return requiredSkills != null ? requiredSkills : coreSkills; }
        public List<String> getSections() { return sections; }
        public List<String> getRecommendedTechnicalSkills() { return recommendedTechnicalSkills != null ? recommendedTechnicalSkills : tools; }
        public List<String> getRecommendedSoftSkills() { return recommendedSoftSkills; }
    }

    private static final Map<String, Map<String, RoleDefinition>> JOB_ROLES = new LinkedHashMap<>();
    private static final Map<String, RoleDefinition> ROLES_BY_NAME = new LinkedHashMap<>();
    private static final Map<String, RoleDefinition> ROLES_BY_ID = new LinkedHashMap<>();

    // Master canonical skill vocabulary for dynamic extraction
    public static final List<String> CANONICAL_SKILLS = List.of(
            "Java", "Spring Boot", "Python", "Django", "FastAPI", "C++", "C", "C#", ".NET",
            "JavaScript", "TypeScript", "React", "Angular", "Vue.js", "Node.js", "Express",
            "HTML5", "CSS3", "Tailwind CSS", "Bootstrap", "SQL", "MySQL", "PostgreSQL", "MongoDB",
            "Redis", "Kafka", "RabbitMQ", "REST APIs", "GraphQL", "gRPC", "Microservices",
            "Docker", "Kubernetes", "AWS", "Azure", "Google Cloud", "GCP", "Terraform", "Ansible",
            "Linux", "Bash", "CI/CD", "Git", "GitHub Actions", "Jenkins", "Helm",
            "Machine Learning", "Deep Learning", "PyTorch", "TensorFlow", "Scikit-Learn", "spaCy",
            "NLP", "Computer Vision", "OpenCV", "HuggingFace", "Transformers", "LLM", "Prompt Engineering",
            "RAG", "Vector Databases", "Qdrant", "ChromaDB", "LangChain", "LlamaIndex",
            "Pandas", "NumPy", "Spark", "Hadoop", "Airflow", "Data Modeling", "ETL", "Snowflake",
            "Power BI", "Tableau", "Excel", "Data Analysis", "Statistics",
            "Cybersecurity", "Network Security", "Penetration Testing", "Ethical Hacking", "SOC",
            "SIEM", "Splunk", "Wireshark", "Vulnerability Assessment", "OWASP", "Cryptography",
            "UI Design", "UX Design", "Figma", "User Research", "Wireframing", "Prototyping", "Design Systems",
            "Agile", "Scrum", "Jira", "Product Roadmapping", "User Stories", "Stakeholder Management",
            "QA Testing", "Selenium", "Cypress", "JUnit", "Postman", "Test Automation", "Performance Testing",
            "Android SDK", "Kotlin", "Swift", "Flutter", "Dart", "iOS Development", "Mobile UI",
            "Embedded C", "RTOS", "Microcontrollers", "ARM", "Firmware", "I2C", "SPI", "UART", "VLSI", "Verilog"
    );

    static {
        // =========================================================================
        // 1. Software Engineering
        // =========================================================================
        Map<String, RoleDefinition> softwareRoles = new LinkedHashMap<>();

        addRole(softwareRoles, new RoleDefinition(
                "full-stack-developer", "Full Stack Developer", "Software Engineering", "Enterprise Software & Web",
                "Entry to Mid-Level", "Architect and deliver responsive user interfaces and resilient backend microservices.",
                List.of("Full Stack Engineer", "Web Application Developer", "Software Engineer"),
                List.of("Java", "JavaScript", "React", "Spring Boot", "SQL", "HTML5", "CSS3", "REST APIs"),
                List.of("Docker", "TypeScript", "GraphQL", "Tailwind CSS", "Redis"),
                List.of("Git", "Postman", "Maven", "VS Code", "IntelliJ IDEA"),
                List.of("Implement domain-driven RESTful APIs and UI workflows", "Optimize database query execution and caching", "Build modular, reusable React components"),
                "Full-stack e-commerce or booking platform with JWT auth, payment gateway integration, and Dockerized deployment.",
                List.of("Oracle Certified Java Developer", "AWS Certified Developer"),
                List.of("OOP Principles", "Data Structures & Algorithms", "Basic Relational Database Concepts"),
                List.of("Backend Developer", "Frontend Developer", "DevOps Engineer"),
                "High demand across Indian IT hubs (Bangalore, Hyderabad, Pune, NCR) and global remote roles."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "backend-developer", "Backend Developer", "Software Engineering", "Cloud & Enterprise Platforms",
                "Junior to Senior", "Engineer scalable server-side systems, data storage pipelines, and distributed APIs.",
                List.of("Backend Engineer", "Server-Side Developer", "Java Engineer"),
                List.of("Java", "Spring Boot", "SQL", "PostgreSQL", "REST APIs", "Microservices", "Docker"),
                List.of("Kafka", "Redis", "Kubernetes", "gRPC", "AWS", "NoSQL"),
                List.of("Maven", "Git", "Postman", "Docker Desktop", "IntelliJ IDEA"),
                List.of("Design fault-tolerant backend architectures", "Optimize database indexing and cache invalidation", "Write comprehensive unit and integration test suites"),
                "High-throughput transactional API service handling concurrency, caching with Redis, and Kafka event streaming.",
                List.of("AWS Certified Solutions Architect", "Spring Professional Certification"),
                List.of("Object-Oriented Programming", "Relational Database Normalization", "Distributed Systems Concepts"),
                List.of("Full Stack Developer", "DevOps Engineer", "Solutions Architect"),
                "Extremely high hiring volume in product startups and enterprise MNCs."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "frontend-developer", "Frontend Developer", "Software Engineering", "Digital Products & Web",
                "Entry to Mid-Level", "Create accessible, high-performance, and visually responsive web experiences.",
                List.of("Frontend Engineer", "UI Developer", "React Developer", "Client-Side Developer"),
                List.of("JavaScript", "TypeScript", "React", "HTML5", "CSS3", "REST APIs", "Git"),
                List.of("Next.js", "Redux", "Tailwind CSS", "GraphQL", "Jest / Cypress"),
                List.of("VS Code", "Chrome DevTools", "Figma", "npm / yarn", "Vite"),
                List.of("Develop interactive UI components adhering to WCAG accessibility", "Manage complex client-side state and caching", "Collaborate closely with UI/UX designers"),
                "Complex analytics dashboard featuring interactive Chart.js/D3 visualizations, dark-mode theming, and responsive layout.",
                List.of("Meta Front-End Developer Certificate"),
                List.of("Modern JavaScript (ES6+)", "DOM Manipulation", "CSS Flexbox & Grid"),
                List.of("Full Stack Developer", "UI Designer", "Mobile Application Developer"),
                "High demand across SaaS, fintech, and digital product organizations."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "software-engineer", "Software Engineer / Software Developer", "Software Engineering", "General Engineering",
                "Graduate / Entry-Level", "Build, test, and maintain robust software modules across the entire software development lifecycle.",
                List.of("Software Developer", "Associate Software Engineer", "Core Developer"),
                List.of("Java", "Python", "Data Structures", "Algorithms", "SQL", "Git", "OOP"),
                List.of("Linux", "Docker", "Unit Testing", "REST APIs"),
                List.of("Git", "IntelliJ IDEA", "VS Code", "Linux Terminal"),
                List.of("Write clean, maintainable, and well-tested code", "Collaborate in daily standups and sprint planning", "Troubleshoot software defects and debug production issues"),
                "Algorithmically optimized data processing tool or web application with unit test suite covering >85% code branches.",
                List.of("Certified Associate in Software Development"),
                List.of("Data Structures & Algorithms", "Object-Oriented Design", "Computer Architecture"),
                List.of("Backend Developer", "Full Stack Developer", "QA Automation Engineer"),
                "Universal foundational role across campus placements and off-campus fresher drives."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "python-developer", "Python Developer", "Software Engineering", "Automation, Web & Scripting",
                "Entry to Mid-Level", "Develop backend services, automated workflows, and data pipelines using Python.",
                List.of("Python Engineer", "Django Developer", "FastAPI Developer"),
                List.of("Python", "FastAPI", "Django", "SQL", "PostgreSQL", "REST APIs", "Git"),
                List.of("Docker", "Pandas", "Celery", "Redis", "Pytest"),
                List.of("PyCharm", "VS Code", "Postman", "Git", "Virtualenv"),
                List.of("Build performant asynchronous REST APIs using FastAPI", "Implement background workers and cron tasks with Celery", "Integrate external third-party APIs and services"),
                "Production-ready FastAPI microservice with Pydantic validation, JWT security, and Dockerized deployment.",
                List.of("Python Institute Certified Associate Python Programmer (PCAP)"),
                List.of("Python Fundamentals", "Object-Oriented Programming", "Basic Database Queries"),
                List.of("Data Engineer", "Backend Developer", "Machine Learning Engineer"),
                "Very high demand in AI-driven startups and backend infrastructure teams."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "cpp-developer", "C/C++ Systems Developer", "Software Engineering", "Systems & High-Performance Computing",
                "Junior to Senior", "Build high-throughput, low-latency systems, game engines, and performance-critical software.",
                List.of("C++ Software Engineer", "Systems Programmer", "C++ Developer"),
                List.of("C++", "C", "Data Structures", "Algorithms", "Multi-Threading", "Memory Management", "Linux"),
                List.of("STL", "Boost", "CMake", "GDB", "Socket Programming", "Docker"),
                List.of("GCC / Clang", "GDB", "Valgrind", "CMake", "VS Code"),
                List.of("Implement low-latency algorithms with zero-copy memory management", "Debug memory leaks and concurrency race conditions", "Optimize CPU cache locality and computational bottlenecks"),
                "High-performance multi-threaded network server or order-matching simulation engine in modern C++ (C++17/20).",
                List.of("C++ Institute Certified Associate Programmer"),
                List.of("Pointers & Memory Model", "Data Structures", "Operating System Fundamentals"),
                List.of("Embedded Software Engineer", "Firmware Engineer", "Game Developer"),
                "Strong demand in fintech trading firms, automotive electronics, and core systems engineering."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "embedded-software-engineer", "Embedded Software Engineer", "Software Engineering", "Embedded Systems & IoT",
                "Graduate to Senior", "Write low-level firmware and embedded software operating on bare-metal and RTOS platforms.",
                List.of("Embedded Engineer", "IoT Developer", "Firmware Engineer"),
                List.of("Embedded C", "C++", "Microcontrollers (ARM / ESP32)", "RTOS", "I2C / SPI / UART", "Git"),
                List.of("FreeRTOS", "Linux Device Drivers", "BLE / Wi-Fi Protocols", "Oscilloscopes", "Logic Analyzers"),
                List.of("Keil uVision", "STM32CubeIDE", "PlatformIO", "Wireshark"),
                List.of("Develop deterministic firmware for microcontrollers", "Write hardware abstraction layers (HAL) and device drivers", "Perform board bring-up and hardware-software integration"),
                "Smart IoT environmental monitor with sensor communication over I2C/SPI and MQTT telemetry transmission.",
                List.of("ARM Certified Engineer"),
                List.of("Digital Electronics", "Microcontroller Architecture", "C Programming"),
                List.of("Firmware Engineer", "VLSI / RTL Design Engineer", "Robotics Engineer"),
                "High growth across automotive, robotics, aerospace, and consumer electronics in India."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "vlsi-design-engineer", "VLSI / RTL Design Engineer", "Software Engineering", "Semiconductor & Hardware",
                "Graduate to Senior", "Design and verify digital integrated circuits, ASICs, and FPGA architectures.",
                List.of("RTL Design Engineer", "Digital IC Design Engineer", "ASIC Design Engineer"),
                List.of("Verilog", "SystemVerilog", "Digital Design", "FPGA", "Timing Analysis (STA)", "Computer Architecture"),
                List.of("UVM", "VHDL", "Tcl Scripting", "Synthesis Tools", "Perl / Python"),
                List.of("Cadence Innovus / Genus", "Synopsys Design Compiler", "ModelSim / QuestaSim", "Vivado"),
                List.of("Write synthesizable RTL code for digital logic blocks", "Develop verification testbenches in SystemVerilog/UVM", "Resolve setup/hold timing violations and optimize clock trees"),
                "Complete pipelined RISC-V processor core designed in Verilog and verified with automated testbenches.",
                List.of("Certified Verification Engineer"),
                List.of("Digital Logic Design", "Boolean Algebra", "Computer Organization"),
                List.of("Embedded Software Engineer", "Hardware Engineer", "FPGA Engineer"),
                "Massive growth in Indian semiconductor design hubs (Bangalore, Noida, Hyderabad)."
        ));

        addRole(softwareRoles, new RoleDefinition(
                "qa-automation-engineer", "QA Automation Engineer / SDET", "Software Engineering", "Quality Assurance & Test Automation",
                "Entry to Mid-Level", "Engineer automated testing frameworks and validate software reliability across APIs and web interfaces.",
                List.of("SDET", "Automation Test Engineer", "Software Test Engineer", "QA Engineer"),
                List.of("Java", "Python", "Selenium", "TestNG / JUnit", "Postman", "SQL", "Git"),
                List.of("Cypress", "RestAssured", "Cucumber (BDD)", "Jenkins", "Docker"),
                List.of("Selenium WebDriver", "Postman", "Jira", "Jenkins", "Git"),
                List.of("Build modular end-to-end automation test suites", "Automate regression and smoke test cycles in CI/CD pipelines", "Execute REST API performance and contract validation"),
                "Hybrid test automation framework utilizing Selenium WebDriver and RestAssured with ExtentReports reporting.",
                List.of("ISTQB Certified Tester", "Certified SDET"),
                List.of("Software Testing Methodologies", "Basic Programming (Java/Python)", "HTML/CSS Basics"),
                List.of("Manual Test Engineer", "Software Engineer", "DevOps Engineer"),
                "Critical role in ensuring zero-defect deployments across enterprise software."
        ));

        JOB_ROLES.put("Software Engineering", softwareRoles);

        // =========================================================================
        // 2. AI, Machine Learning & Data
        // =========================================================================
        Map<String, RoleDefinition> aiRoles = new LinkedHashMap<>();

        addRole(aiRoles, new RoleDefinition(
                "ai-engineer", "AI Engineer", "AI, Machine Learning & Data", "Artificial Intelligence & LLMs",
                "Junior to Senior", "Develop and deploy generative AI applications, agent workflows, and intelligent microservices.",
                List.of("Generative AI Engineer", "LLM Application Engineer", "AI Solutions Developer"),
                List.of("Python", "LangChain / LlamaIndex", "Google Gemini / OpenAI APIs", "RAG", "Vector Databases (Qdrant / Chroma)", "Prompt Engineering", "FastAPI"),
                List.of("PyTorch", "HuggingFace Transformers", "Docker", "Docker Compose", "Fine-Tuning (LoRA / QLoRA)"),
                List.of("VS Code", "Jupyter Notebook", "Postman", "Qdrant Console", "Git"),
                List.of("Architect enterprise RAG pipelines with chunking and reranking", "Implement agentic tool-calling workflows and safeguards", "Optimize LLM token latency, cost, and context grounding"),
                "Production RAG document assistant with grounded source citations, vector search in Qdrant, and guardrails.",
                List.of("Google Cloud Certified Professional Machine Learning Engineer", "DeepLearning.AI Generative AI Specialist"),
                List.of("Python Proficiency", "Basic Machine Learning Concepts", "REST API Development"),
                List.of("Machine Learning Engineer", "Data Scientist", "NLP Engineer"),
                "One of the fastest growing software specializations globally."
        ));

        addRole(aiRoles, new RoleDefinition(
                "machine-learning-engineer", "Machine Learning Engineer", "AI, Machine Learning & Data", "Applied ML & MLOps",
                "Mid-Level to Senior", "Design, train, optimize, and serve statistical machine learning models in production.",
                List.of("ML Engineer", "MLOps Engineer", "Applied ML Specialist"),
                List.of("Python", "Scikit-Learn", "PyTorch", "Pandas", "NumPy", "FastAPI", "Docker", "Git"),
                List.of("MLflow", "TensorFlow", "Kubeflow", "Feature Stores", "AWS SageMaker", "ONNX"),
                List.of("JupyterLab", "Git", "Docker", "MLflow", "Linux Terminal"),
                List.of("Develop reproducible training, validation, and testing pipelines", "Serve models as low-latency REST microservices", "Monitor model accuracy drift and automate retraining workflows"),
                "End-to-end predictive pipeline trained on tabular data, tracked with MLflow, and packaged in Docker with automated CI.",
                List.of("TensorFlow Developer Certificate", "AWS Certified Machine Learning - Specialty"),
                List.of("Linear Algebra & Calculus", "Probability & Statistics", "Python Programming"),
                List.of("AI Engineer", "Data Scientist", "MLOps Engineer"),
                "Strong requirement in analytics product companies and enterprise data teams."
        ));

        addRole(aiRoles, new RoleDefinition(
                "data-scientist", "Data Scientist", "AI, Machine Learning & Data", "Data Science & Predictive Analytics",
                "Graduate to Senior", "Extract actionable business insights and train predictive models from structured and unstructured data.",
                List.of("Applied Scientist", "Analytics Data Scientist", "Data Science Specialist"),
                List.of("Python", "SQL", "Pandas", "NumPy", "Scikit-Learn", "Data Visualization", "Statistics"),
                List.of("Seaborn / Matplotlib", "PyTorch", "A/B Testing", "Tableau", "BigQuery / Snowflake"),
                List.of("JupyterLab", "VS Code", "Git", "Tableau", "SQL Workbench"),
                List.of("Formulate hypotheses and execute statistical exploratory data analysis", "Build and cross-validate predictive models", "Translate quantitative findings into executive stakeholder presentations"),
                "Comprehensive customer churn or price prediction project with statistical hypothesis testing and feature importance analysis.",
                List.of("IBM Data Science Professional Certificate"),
                List.of("Probability & Statistics", "SQL Queries", "Python Data Science Stack"),
                List.of("Data Analyst", "Machine Learning Engineer", "Business Intelligence Analyst"),
                "High strategic value across finance, healthcare, e-commerce, and logistics."
        ));

        addRole(aiRoles, new RoleDefinition(
                "data-engineer", "Data Engineer", "AI, Machine Learning & Data", "Big Data & Data Pipelines",
                "Junior to Senior", "Build resilient data ingestion pipelines, warehouse architectures, and batch/streaming ETL systems.",
                List.of("Big Data Engineer", "ETL Developer", "Data Platform Engineer"),
                List.of("SQL", "Python", "Apache Spark", "Data Modeling", "ETL Pipelines", "PostgreSQL", "Git"),
                List.of("Apache Airflow", "Kafka", "Snowflake", "dbt", "AWS (S3/Redshift)", "Docker"),
                List.of("Airflow Web UI", "Databricks", "DBeaver", "Git", "Docker"),
                List.of("Design star/snowflake warehouse schemas", "Implement robust batch and streaming ETL pipelines", "Ensure data governance, quality contracts, and idempotency"),
                "Automated data pipeline orchestrating daily batch ingestion into Snowflake with dbt transformations and Airflow DAGs.",
                List.of("Google Cloud Professional Data Engineer", "Databricks Certified Data Engineer"),
                List.of("Advanced SQL", "Relational Database Design", "Python Basics"),
                List.of("Data Analyst", "Cloud Engineer", "Database Administrator"),
                "Extremely high enterprise demand as businesses scale their AI and analytics data infrastructure."
        ));

        addRole(aiRoles, new RoleDefinition(
                "data-analyst", "Data Analyst", "AI, Machine Learning & Data", "Business & Product Analytics",
                "Entry to Mid-Level", "Analyze organizational metrics, design KPI dashboards, and deliver operational reports.",
                List.of("Business Data Analyst", "Product Analyst", "BI Analyst"),
                List.of("SQL", "Excel (Advanced)", "Power BI / Tableau", "Data Visualization", "Python (Basic Pandas)"),
                List.of("Statistics", "Google Analytics", "DAX", "Storytelling with Data"),
                List.of("Power BI Desktop", "Excel", "SQL Server Management Studio", "Jupyter"),
                List.of("Extract, clean, and aggregate relational datasets using complex SQL queries", "Design executive reporting dashboards with drill-down capabilities", "Identify trends, outliers, and revenue growth opportunities"),
                "Interactive multi-page Power BI sales analytics dashboard with dynamic filtering, DAX KPIs, and trend forecasts.",
                List.of("Microsoft Certified: Power BI Data Analyst Associate (PL-300)"),
                List.of("Spreadsheet Formulas", "Basic Mathematics & Statistics", "Analytical Curiosity"),
                List.of("Data Scientist", "Business Analyst", "Data Engineer"),
                "Excellent entry pathway for graduates into the technology and data domain."
        ));

        JOB_ROLES.put("AI, Machine Learning & Data", aiRoles);

        // =========================================================================
        // 3. Cloud, Infrastructure & DevOps
        // =========================================================================
        Map<String, RoleDefinition> cloudRoles = new LinkedHashMap<>();

        addRole(cloudRoles, new RoleDefinition(
                "devops-engineer", "DevOps Engineer", "Cloud, Infrastructure & DevOps", "Cloud Infrastructure & CI/CD",
                "Junior to Senior", "Automate infrastructure provisioning, deployment pipelines, and operational reliability.",
                List.of("Site Reliability Engineer", "Platform Engineer", "Cloud Operations Engineer"),
                List.of("Linux", "Docker", "Kubernetes", "CI/CD (GitHub Actions / Jenkins)", "AWS", "Bash", "Git"),
                List.of("Terraform", "Ansible", "Prometheus & Grafana", "Helm", "Python Scripting"),
                List.of("Terminal", "Docker Desktop", "Kubectl", "GitHub Actions", "AWS Console"),
                List.of("Automate zero-downtime deployment pipelines", "Provision reproducible cloud infrastructure via Terraform", "Configure cluster metrics monitoring, alerts, and log aggregators"),
                "GitOps CI/CD pipeline deploying containerized microservices to a Kubernetes cluster with Prometheus monitoring.",
                List.of("Certified Kubernetes Administrator (CKA)", "AWS Certified DevOps Engineer"),
                List.of("Linux System Administration", "Networking Basics (TCP/IP, DNS)", "Shell Scripting"),
                List.of("Cloud Engineer", "Backend Developer", "Site Reliability Engineer"),
                "Consistently ranked among the top in-demand technology roles across India."
        ));

        addRole(cloudRoles, new RoleDefinition(
                "cloud-engineer", "Cloud Engineer (AWS / Azure / GCP)", "Cloud, Infrastructure & DevOps", "Cloud Computing",
                "Entry to Mid-Level", "Architect and maintain cloud computing resources, storage, VPC networks, and access security.",
                List.of("AWS Cloud Engineer", "Azure Solutions Specialist", "Cloud Infrastructure Engineer"),
                List.of("AWS / Azure / GCP", "Linux", "Networking (VPC, Subnets, VPN)", "Cloud Security (IAM)", "Docker", "SQL"),
                List.of("Terraform", "Serverless (AWS Lambda)", "CloudWatch", "Python Scripting", "Kubernetes"),
                List.of("AWS Management Console", "Terraform CLI", "AWS CLI", "Git", "VS Code"),
                List.of("Design highly available multi-tier cloud architectures", "Manage IAM roles and least-privilege security policies", "Optimize cloud infrastructure consumption and monthly expenditures"),
                "Multi-tier secure cloud architecture deployed via Terraform with auto-scaling EC2 instances, RDS, and S3 storage.",
                List.of("AWS Certified Solutions Architect - Associate", "Microsoft Certified: Azure Fundamentals (AZ-900)"),
                List.of("Computer Networks", "Operating Systems", "Basic Virtualization"),
                List.of("DevOps Engineer", "Database Administrator", "Systems Engineer"),
                "High enterprise demand as organizations migrate legacy workloads to public clouds."
        ));

        JOB_ROLES.put("Cloud, Infrastructure & DevOps", cloudRoles);

        // =========================================================================
        // 4. Cybersecurity
        // =========================================================================
        Map<String, RoleDefinition> cyberRoles = new LinkedHashMap<>();

        addRole(cyberRoles, new RoleDefinition(
                "cybersecurity-analyst", "Cybersecurity Analyst / SOC Analyst", "Cybersecurity", "Information Security & Defense",
                "Entry to Mid-Level", "Monitor security telemetry, analyze threat alerts, and investigate unauthorized network intrusions.",
                List.of("SOC Analyst", "Information Security Analyst", "Security Operations Engineer"),
                List.of("Network Security", "SIEM (Splunk / QRadar)", "Wireshark", "Vulnerability Assessment", "Linux", "TCP/IP"),
                List.of("Incident Response", "EDR Tools", "Threat Hunting", "OWASP Top 10", "Python Scripting"),
                List.of("Splunk", "Wireshark", "Nmap", "Burp Suite Community", "Kali Linux"),
                List.of("Triage and escalate security alerts in 24/7 SOC workflows", "Analyze packet captures to identify malicious traffic signatures", "Perform vulnerability scanning and remediation tracking"),
                "Hands-on SOC incident investigation lab report documenting packet analysis, attack attribution, and remediation steps.",
                List.of("CompTIA Security+", "Certified Ethical Hacker (CEH)", "Cisco Certified CyberOps Associate"),
                List.of("Computer Networking Fundamentals", "Operating System Concepts", "Basic Cryptography"),
                List.of("Penetration Tester", "Security Engineer", "GRC Analyst"),
                "Significant hiring across banking, defense, IT services, and global security operations centers."
        ));

        addRole(cyberRoles, new RoleDefinition(
                "penetration-tester", "Penetration Tester / Security Engineer", "Cybersecurity", "Offensive Security & AppSec",
                "Junior to Senior", "Perform ethical hacking, red team assessments, and web application vulnerability auditing.",
                List.of("Ethical Hacker", "AppSec Engineer", "Vulnerability Assessment & Pen Testing (VAPT) Specialist"),
                List.of("OWASP Top 10", "Burp Suite", "Web Security", "Linux", "Nmap / Metasploit", "Python / Bash Scripting"),
                List.of("API Security Testing", "Active Directory Attacks", "Reverse Engineering", "Cloud Security Assessment"),
                List.of("Burp Suite Pro", "Kali Linux", "Nmap", "Wireshark", "Postman"),
                List.of("Identify security vulnerabilities in web applications and APIs", "Draft comprehensive executive and remediation penetration test reports", "Conduct security code reviews and recommend hardening measures"),
                "Detailed penetration testing report of an intentionally vulnerable target (e.g. OWASP Juice Shop) detailing exploitation vectors and code patches.",
                List.of("Offensive Security Certified Professional (OSCP)", "eLearnSecurity Junior Penetration Tester (eJPT)"),
                List.of("HTTP Protocol Details", "Web Application Architecture", "Linux CLI"),
                List.of("Cybersecurity Analyst", "Application Security Engineer", "DevSecOps Engineer"),
                "Prestigious and high-value specialization in information security."
        ));

        JOB_ROLES.put("Cybersecurity", cyberRoles);

        // =========================================================================
        // 5. Product, Business & Consulting
        // =========================================================================
        Map<String, RoleDefinition> productRoles = new LinkedHashMap<>();

        addRole(productRoles, new RoleDefinition(
                "product-manager", "Product Manager / Technical Product Manager", "Product, Business & Consulting", "Product Strategy",
                "Mid-Level to Senior", "Define product vision, author PRDs, prioritize feature backlogs, and drive roadmap execution.",
                List.of("Associate Product Manager (APM)", "Technical Product Manager", "Product Owner"),
                List.of("Product Strategy", "User Stories & PRDs", "Agile / Scrum", "Data-Driven Prioritization", "Stakeholder Alignment"),
                List.of("A/B Testing", "Figma (Wireframing)", "Mixpanel / Amplitude", "SQL (Product Analytics)", "Jira"),
                List.of("Jira", "Confluence", "Figma", "Miro", "Notion"),
                List.of("Synthesize customer discovery interviews into actionable product requirements", "Define release KPIs and feature adoption success metrics", "Facilitate cross-functional sprint planning with engineering and design"),
                "Complete Product Requirement Document (PRD) with wireframes, user journeys, GTM strategy, and success metrics for a SaaS application.",
                List.of("Certified Scrum Product Owner (CSPO)", "Pragmatic Institute Certified"),
                List.of("Software Development Lifecycle", "Analytical Thinking", "Strong Written Communication"),
                List.of("Business Analyst", "Project Manager", "Scrum Master"),
                "High-impact leadership role bridging business, engineering, and user experience."
        ));

        addRole(productRoles, new RoleDefinition(
                "business-analyst", "Business Analyst / Systems Analyst", "Product, Business & Consulting", "Business Operations",
                "Entry to Mid-Level", "Bridge business requirements and technical implementation through process modeling and functional specs.",
                List.of("Business Systems Analyst", "Functional Consultant", "Requirements Engineer"),
                List.of("Requirements Gathering", "UML Modeling", "SQL", "Excel", "Agile / Scrum", "Jira"),
                List.of("BPMN 2.0", "Tableau / Power BI", "User Acceptance Testing (UAT)", "Gap Analysis"),
                List.of("Jira", "MS Visio / Lucidchart", "Excel", "Confluence"),
                List.of("Conduct stakeholder discovery workshops to document business needs", "Create functional specification documents, flowcharts, and use cases", "Coordinate user acceptance testing (UAT) and validate requirements"),
                "Comprehensive Business Analysis dossier with AS-IS vs TO-BE process flowcharts, use cases, and traceability matrix.",
                List.of("ECBA (Entry Certificate in Business Analysis - IIBA)", "CBAP"),
                List.of("Analytical Thinking", "Structured Problem Solving", "Basic Data Analysis"),
                List.of("Product Analyst", "Project Manager", "Product Manager"),
                "Consistent high volume of hiring in IT consulting services and corporate enterprises."
        ));

        JOB_ROLES.put("Product, Business & Consulting", productRoles);

        // =========================================================================
        // 6. Design & Creative Technology
        // =========================================================================
        Map<String, RoleDefinition> designRoles = new LinkedHashMap<>();

        addRole(designRoles, new RoleDefinition(
                "ui-ux-designer", "UI/UX Designer / Product Designer", "Design & Creative Technology", "Design & Human Experience",
                "Entry to Senior", "Craft intuitive user journeys, high-fidelity prototypes, and cohesive design systems.",
                List.of("Product Designer", "User Experience Designer", "Interaction Designer", "UI Designer"),
                List.of("Figma", "User Research", "Wireframing", "High-Fidelity Prototyping", "Design Systems", "UI Design"),
                List.of("Usability Testing", "Information Architecture", "HTML/CSS Basics", "Micro-Interactions", "Accessibility (WCAG)"),
                List.of("Figma", "Miro", "Adobe XD", "FigJam", "Notion"),
                List.of("Conduct user interviews and synthesize journey maps and personas", "Build scalable design system component libraries in Figma", "Deliver responsive design handoffs with interactive specs to engineers"),
                "End-to-end design case study detailing user research, low-fidelity wireframes, interactive Figma prototype, and usability evaluation.",
                List.of("Google UX Design Professional Certificate"),
                List.of("Visual Design Principles (Color, Typography, Hierarchy)", "Empathy & User-Centric Mindset"),
                List.of("Frontend Developer", "Product Manager", "Design Systems Specialist"),
                "Rapidly growing demand across consumer apps, B2B SaaS, and design studios."
        ));

        JOB_ROLES.put("Design & Creative Technology", designRoles);

        // =========================================================================
        // 7. Mobile and Embedded Development
        // =========================================================================
        Map<String, RoleDefinition> mobileRoles = new LinkedHashMap<>();

        addRole(mobileRoles, new RoleDefinition(
                "mobile-app-engineer", "Mobile App Engineer (Android / iOS / Flutter)", "Mobile and Embedded Development", "Mobile Applications",
                "Graduate to Mid-Level", "Develop high-performance natively compiled mobile applications across Android and iOS platforms.",
                List.of("Android Developer", "Flutter Developer", "iOS Developer", "Mobile Developer"),
                List.of("Flutter", "Dart", "Kotlin", "Android SDK", "REST APIs", "Mobile UI/UX"),
                List.of("Swift", "Firebase", "State Management (Bloc / Provider)", "SQLite / Room", "CI/CD for Mobile"),
                List.of("Android Studio", "Xcode", "VS Code", "Firebase Console"),
                List.of("Engineer cross-platform mobile apps using Flutter and Dart", "Integrate device sensors, push notifications, and offline data sync", "Optimize app rendering frame rates and memory footprint"),
                "Cross-platform mobile application published on GitHub with offline caching, state management, and biometric auth.",
                List.of("Google Associate Android Developer Certification"),
                List.of("Object-Oriented Programming", "Mobile Lifecycle Concepts", "Asynchronous Programming"),
                List.of("Frontend Developer", "Full Stack Developer", "Software Engineer"),
                "Strong demand in mobile-first Indian internet economy and startups."
        ));

        JOB_ROLES.put("Mobile and Embedded Development", mobileRoles);

        // =========================================================================
        // 8. Additional Domains (Finance, Healthcare, Operations)
        // =========================================================================
        Map<String, RoleDefinition> additionalRoles = new LinkedHashMap<>();

        addRole(additionalRoles, new RoleDefinition(
                "fintech-quant-analyst", "Fintech & Quantitative Systems Analyst", "Additional Domains", "Fintech & Capital Markets",
                "Graduate to Mid-Level", "Analyze financial market data, build algorithmic risk models, and develop fintech payment integrations.",
                List.of("Quantitative Analyst", "Financial Systems Analyst", "Fintech Engineer"),
                List.of("Python", "SQL", "Financial Modeling", "Time Series Analysis", "Statistics", "REST APIs"),
                List.of("Pandas", "NumPy", "Algorithmic Trading", "Risk Management (VaR)", "Excel VBA"),
                List.of("Jupyter", "Bloomberg / Yahoo Finance API", "PostgreSQL", "Git"),
                List.of("Develop backtested algorithmic trading and asset valuation models", "Audit transaction reconciliation flows and financial risk telemetry", "Synthesize financial KPIs into compliance dashboards"),
                "Algorithmic portfolio optimization and backtesting framework analyzing historical equity volatility and Sharpe ratio.",
                List.of("CFA Level 1", "FRM (Financial Risk Manager)"),
                List.of("Basic Accounting", "Statistics", "Python Programming"),
                List.of("Data Analyst", "Data Scientist", "Business Analyst"),
                "High compensation and specialized hiring in investment banking and fintech hubs."
        ));

        addRole(additionalRoles, new RoleDefinition(
                "health-informatics-specialist", "Health Informatics Specialist", "Additional Domains", "Healthcare Technology",
                "Junior to Mid-Level", "Manage electronic health record systems, clinical data pipelines, and healthcare interoperability standards.",
                List.of("Healthcare Data Analyst", "Clinical Informatics Specialist", "Health IT Engineer"),
                List.of("HL7 / FHIR", "Healthcare Data Privacy (HIPAA)", "SQL", "Python", "EHR Systems"),
                List.of("Clinical Data Modeling", "Tableau / Power BI", "REST APIs", "ETL Pipelines"),
                List.of("Epic / Cerner Tools", "PostgreSQL", "Jupyter", "Postman"),
                List.of("Integrate hospital data systems using HL7/FHIR messaging protocols", "Audit clinical databases for patient data integrity and HIPAA privacy compliance", "Develop clinical efficacy dashboards for healthcare administrators"),
                "Interoperable healthcare data exchange pipeline parsing FHIR JSON resources and generating clinical outcome metrics.",
                List.of("Certified Professional in Healthcare Information and Management Systems (CPHIMS)"),
                List.of("Database Concepts", "Healthcare Systems Overview", "Data Privacy Standards"),
                List.of("Data Analyst", "Data Engineer", "Systems Analyst"),
                "Rapidly expanding in global healthtech R&D centers in India."
        ));

        JOB_ROLES.put("Additional Domains", additionalRoles);
    }

    private static void addRole(Map<String, RoleDefinition> categoryMap, RoleDefinition role) {
        categoryMap.put(role.getRoleName(), role);
        ROLES_BY_NAME.put(role.getRoleName(), role);
        ROLES_BY_ID.put(role.getRoleId(), role);
    }

    public static Map<String, Map<String, RoleDefinition>> getJobRoles() {
        return JOB_ROLES;
    }

    public static RoleDefinition getRoleByName(String roleName) {
        if (roleName == null) return null;
        RoleDefinition exact = ROLES_BY_NAME.get(roleName);
        if (exact != null) return exact;

        // Try lookup by ID
        RoleDefinition byId = ROLES_BY_ID.get(roleName.toLowerCase());
        if (byId != null) return byId;

        // Try case-insensitive alias match
        for (RoleDefinition role : ROLES_BY_NAME.values()) {
            if (role.getRoleName().equalsIgnoreCase(roleName)) return role;
            for (String alias : role.getAliases()) {
                if (alias.equalsIgnoreCase(roleName)) return role;
            }
        }
        return null;
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
        return getAllRoles(false);
    }

    public static List<RoleDefinition> getAllRoles(boolean includeInactive) {
        if (includeInactive) {
            return new ArrayList<>(ROLES_BY_NAME.values());
        }
        return ROLES_BY_NAME.values().stream()
                .filter(RoleDefinition::isActive)
                .collect(Collectors.toList());
    }

    public static boolean setRoleActive(String roleIdentifier, boolean active) {
        RoleDefinition role = getRoleByName(roleIdentifier);
        if (role != null) {
            role.setActive(active);
            return true;
        }
        return false;
    }

    public static void addCustomRole(RoleDefinition customRole) {
        if (customRole == null) return;
        Map<String, RoleDefinition> catMap = JOB_ROLES.computeIfAbsent(customRole.getCategory(), k -> new LinkedHashMap<>());
        addRole(catMap, customRole);
    }

    /**
     * Dynamically infers a role definition and extracts skills when a user pastes an unknown job description.
     */
    public static RoleDefinition inferRoleFromJobDescription(String customTitle, String jobDescription) {
        if (jobDescription == null || jobDescription.isBlank()) {
            return getRoleByName("Full Stack Developer");
        }

        String title = (customTitle != null && !customTitle.isBlank()) ? customTitle.trim() : "Custom Target Role";
        String lowerDesc = jobDescription.toLowerCase();

        // 1. Extract matched canonical skills using word boundary matching
        Set<String> matchedSkills = new LinkedHashSet<>();
        for (String skill : CANONICAL_SKILLS) {
            Pattern p = Pattern.compile("\\\\b" + Pattern.quote(skill.toLowerCase()) + "\\\\b", Pattern.CASE_INSENSITIVE);
            if (p.matcher(lowerDesc).find()) {
                matchedSkills.add(skill);
            }
        }

        List<String> coreList = new ArrayList<>();
        List<String> preferredList = new ArrayList<>();
        List<String> toolsList = new ArrayList<>();

        int count = 0;
        for (String s : matchedSkills) {
            if (count < 8) coreList.add(s);
            else if (count < 14) preferredList.add(s);
            else toolsList.add(s);
            count++;
        }

        if (coreList.isEmpty()) {
            coreList = List.of("Problem-Solving", "Analytical Skills", "Domain Competence");
        }

        // 2. Infer role category
        String inferredCategory = "Software Engineering";
        if (lowerDesc.contains("machine learning") || lowerDesc.contains("data science") || lowerDesc.contains("ai") || lowerDesc.contains("analytics")) {
            inferredCategory = "AI, Machine Learning & Data";
        } else if (lowerDesc.contains("cloud") || lowerDesc.contains("devops") || lowerDesc.contains("kubernetes") || lowerDesc.contains("aws")) {
            inferredCategory = "Cloud, Infrastructure & DevOps";
        } else if (lowerDesc.contains("security") || lowerDesc.contains("soc") || lowerDesc.contains("penetration") || lowerDesc.contains("vulnerability")) {
            inferredCategory = "Cybersecurity";
        } else if (lowerDesc.contains("product manager") || lowerDesc.contains("business analyst") || lowerDesc.contains("agile")) {
            inferredCategory = "Product, Business & Consulting";
        } else if (lowerDesc.contains("figma") || lowerDesc.contains("ui/ux") || lowerDesc.contains("wireframing")) {
            inferredCategory = "Design & Creative Technology";
        }

        String roleId = "custom-" + System.currentTimeMillis();
        String summary = "Dynamically extracted role specification parsed from pasted employer job requirements.";

        RoleDefinition dynamicRole = new RoleDefinition(
                roleId, title, inferredCategory, "Custom / Employer Stated Domain",
                "Custom Inferred Level", summary, List.of(title, "Pasted Job Description Target"),
                coreList, preferredList, toolsList,
                List.of("Fulfill stated technical competencies in custom job description", "Execute core responsibilities as required by hiring team"),
                "Build demonstrable project evidence highlighting: " + String.join(", ", coreList.subList(0, Math.min(3, coreList.size()))),
                List.of("Role-relevant industry certifications"),
                List.of("Core domain fundamentals", "Foundational practical experience"),
                List.of("Related Industry Technical Roles"),
                "Custom job description pasted by candidate; dynamically parsed via taxonomy inference."
        );

        addCustomRole(dynamicRole);
        return dynamicRole;
    }
}
'''

with open('Smart-Resume-Analyzer-Java/src/main/java/com/smartresume/config/JobRolesConfig.java', 'w', encoding='utf-8') as f:
    f.write(code)

print("Generated expanded JobRolesConfig.java successfully.")
