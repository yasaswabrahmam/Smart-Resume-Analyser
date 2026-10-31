package com.smartresume.service;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.config.JobRolesConfig.RoleDefinition;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CareerRoadmapService {

    public static class RoadmapWeek {
        private int weekNumber;
        private String phaseTitle;
        private String focusArea;
        private List<String> learningGoals;
        private String milestoneDeliverable;
        private String estimatedHours;
        private String youtubeSearchUrl;
        private String documentationUrl;
        private String practiceLabUrl;
        private List<String> keyTools;

        public RoadmapWeek(int weekNumber, String phaseTitle, String focusArea,
                           List<String> learningGoals, String milestoneDeliverable,
                           String estimatedHours, String youtubeSearchUrl,
                           String documentationUrl, String practiceLabUrl,
                           List<String> keyTools) {
            this.weekNumber = weekNumber;
            this.phaseTitle = phaseTitle;
            this.focusArea = focusArea;
            this.learningGoals = learningGoals != null ? learningGoals : new ArrayList<>();
            this.milestoneDeliverable = milestoneDeliverable;
            this.estimatedHours = estimatedHours;
            this.youtubeSearchUrl = youtubeSearchUrl;
            this.documentationUrl = documentationUrl;
            this.practiceLabUrl = practiceLabUrl;
            this.keyTools = keyTools != null ? keyTools : new ArrayList<>();
        }

        public int getWeekNumber() { return weekNumber; }
        public String getPhaseTitle() { return phaseTitle; }
        public String getFocusArea() { return focusArea; }
        public List<String> getLearningGoals() { return learningGoals; }
        public String getMilestoneDeliverable() { return milestoneDeliverable; }
        public String getEstimatedHours() { return estimatedHours; }
        public String getYoutubeSearchUrl() { return youtubeSearchUrl; }
        public String getDocumentationUrl() { return documentationUrl; }
        public String getPracticeLabUrl() { return practiceLabUrl; }
        public List<String> getKeyTools() { return keyTools; }
    }

    public static class CareerRoadmapPlan {
        private String targetRole;
        private String category;
        private String careerLevel;
        private int durationWeeks;
        private String difficultyLevel;
        private List<RoadmapWeek> weeks = new ArrayList<>();
        private List<String> recommendedCertifications = new ArrayList<>();
        private String careerTrajectoryOutcome;
        private String milestoneProjectTitle;
        private String milestoneProjectDescription;
        private List<String> prerequisites = new ArrayList<>();

        public String getTargetRole() { return targetRole; }
        public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getCareerLevel() { return careerLevel; }
        public void setCareerLevel(String careerLevel) { this.careerLevel = careerLevel; }
        public int getDurationWeeks() { return durationWeeks; }
        public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }
        public String getDifficultyLevel() { return difficultyLevel; }
        public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
        public List<RoadmapWeek> getWeeks() { return weeks; }
        public List<String> getRecommendedCertifications() { return recommendedCertifications; }
        public String getCareerTrajectoryOutcome() { return careerTrajectoryOutcome; }
        public void setCareerTrajectoryOutcome(String careerTrajectoryOutcome) { this.careerTrajectoryOutcome = careerTrajectoryOutcome; }
        public String getMilestoneProjectTitle() { return milestoneProjectTitle; }
        public void setMilestoneProjectTitle(String milestoneProjectTitle) { this.milestoneProjectTitle = milestoneProjectTitle; }
        public String getMilestoneProjectDescription() { return milestoneProjectDescription; }
        public void setMilestoneProjectDescription(String milestoneProjectDescription) { this.milestoneProjectDescription = milestoneProjectDescription; }
        public List<String> getPrerequisites() { return prerequisites; }
    }

    public CareerRoadmapPlan generateRoadmap(String targetRole, List<String> missingSkills) {
        // Resolve role definition from catalog
        String resolvedName = (targetRole != null && !targetRole.isBlank()) ? targetRole.trim() : "Full Stack Developer";
        RoleDefinition role = JobRolesConfig.getRoleByName(resolvedName);
        if (role == null) {
            // Try alias or fuzzy lookup
            for (String rName : JobRolesConfig.getAllRoleNames()) {
                if (rName.equalsIgnoreCase(resolvedName) || resolvedName.toLowerCase().contains(rName.toLowerCase()) || rName.toLowerCase().contains(resolvedName.toLowerCase())) {
                    role = JobRolesConfig.getRoleByName(rName);
                    break;
                }
            }
        }
        if (role == null) {
            role = JobRolesConfig.getRoleByName("Full Stack Developer");
        }

        CareerRoadmapPlan plan = new CareerRoadmapPlan();
        plan.setTargetRole(role != null ? role.getRoleName() : resolvedName);
        plan.setCategory(role != null ? role.getCategory() : "Software Engineering");
        plan.setCareerLevel(role != null ? role.getCareerLevel() : "Entry to Mid-Level");
        plan.setDurationWeeks(8);
        plan.setDifficultyLevel(role != null && role.getCareerLevel().toLowerCase().contains("senior") ? "Advanced" : "Intermediate to Advanced");

        List<String> coreSkills = role != null && role.getCoreSkills() != null && !role.getCoreSkills().isEmpty()
                ? role.getCoreSkills()
                : List.of("Java", "JavaScript", "React", "Spring Boot", "SQL", "REST APIs");

        List<String> preferredSkills = role != null && role.getPreferredSkills() != null && !role.getPreferredSkills().isEmpty()
                ? role.getPreferredSkills()
                : List.of("Docker", "Git", "CI/CD", "Redis", "Cloud");

        List<String> tools = role != null && role.getTools() != null && !role.getTools().isEmpty()
                ? role.getTools()
                : List.of("Git", "Postman", "Docker", "VS Code");

        String primarySkill1 = coreSkills.size() > 0 ? coreSkills.get(0) : "Core Fundamentals";
        String primarySkill2 = coreSkills.size() > 1 ? coreSkills.get(1) : primarySkill1;
        String framework1 = coreSkills.size() > 2 ? coreSkills.get(2) : primarySkill1;
        String framework2 = coreSkills.size() > 3 ? coreSkills.get(3) : preferredSkills.get(0);
        String dataSkill = coreSkills.stream().filter(s -> s.equalsIgnoreCase("SQL") || s.toLowerCase().contains("data") || s.equalsIgnoreCase("PostgreSQL") || s.equalsIgnoreCase("MongoDB")).findFirst().orElse("Data Modeling & APIs");
        String missingFocus = (missingSkills != null && !missingSkills.isEmpty()) ? missingSkills.get(0) : preferredSkills.get(0);
        String secondaryMissing = (missingSkills != null && missingSkills.size() > 1) ? missingSkills.get(1) : (preferredSkills.size() > 1 ? preferredSkills.get(1) : "System Architecture");

        String encodedRole = urlEncode(plan.getTargetRole());

        // Phase 1: Foundations & Core Technologies (Weeks 1-2)
        plan.getWeeks().add(new RoadmapWeek(
                1,
                "Phase 1: Foundations & Core Environment",
                "Core Language Mastery: " + primarySkill1 + " & " + primarySkill2,
                List.of(
                        "Master syntax invariants, memory models, and idiomatic coding patterns in " + primarySkill1,
                        "Configure a production-ready development workspace using " + (tools.isEmpty() ? "Git" : tools.get(0)) + " and testing frameworks",
                        "Implement algorithms, data validation routines, and clean error handling hierarchies"
                ),
                "Publish a clean GitHub repository containing 5 practical algorithm katas and unit test suites.",
                "10-12 Hours",
                ytUrl(primarySkill1 + " tutorial complete course 2026"),
                docUrl(primarySkill1),
                "https://github.com/topics/" + urlEncode(primarySkill1.toLowerCase()),
                List.of(primarySkill1, primarySkill2)
        ));

        plan.getWeeks().add(new RoadmapWeek(
                2,
                "Phase 1: Foundations & Core Environment",
                "Framework Deep Dive: " + framework1 + " Standards",
                List.of(
                        "Deep dive into standard application structure, dependency injection, and component design in " + framework1,
                        "Build modular services and decoupled interfaces adhering to clean architecture principles",
                        "Study performance optimization, state management, and configuration profiles"
                ),
                "Construct and test a standalone service prototype with comprehensive integration tests.",
                "12-14 Hours",
                ytUrl(framework1 + " full course project 2026"),
                docUrl(framework1),
                "https://github.com/topics/" + urlEncode(framework1.toLowerCase()),
                List.of(framework1, (tools.size() > 1 ? tools.get(1) : "Git"))
        ));

        // Phase 2: Architecture & Integration (Weeks 3-4)
        plan.getWeeks().add(new RoadmapWeek(
                3,
                "Phase 2: Architecture & Integration",
                "Persistence, State & API Engineering (" + dataSkill + ")",
                List.of(
                        "Design normalized schemas, relationship mappings, and index optimizations for " + dataSkill,
                        "Implement transactional boundaries, secure REST/GraphQL API endpoints, and input sanitization",
                        "Study query latency tuning, pagination patterns, and database migration tooling"
                ),
                "Deliver a high-throughput backend data API with automated migrations and API test collections.",
                "12-15 Hours",
                ytUrl(dataSkill + " backend API design tutorial"),
                docUrl(dataSkill),
                "https://github.com/topics/rest-api",
                List.of(dataSkill, framework1)
        ));

        plan.getWeeks().add(new RoadmapWeek(
                4,
                "Phase 2: Architecture & Integration",
                "Secondary Competency & Integration: " + missingFocus,
                List.of(
                        "Integrate " + missingFocus + " into your application stack to resolve key industry requirements",
                        "Implement environment parity using containerization (Docker) and configuration variables",
                        "Conduct threat modeling, input validation, and secure authentication workflows"
                ),
                "Containerize services with Docker Compose and verify zero-downtime health endpoints.",
                "10-14 Hours",
                ytUrl(missingFocus + " crash course hands on"),
                docUrl(missingFocus),
                "https://github.com/topics/" + urlEncode(missingFocus.toLowerCase()),
                List.of(missingFocus, secondaryMissing)
        ));

        // Phase 3: Production Portfolio Blueprint (Weeks 5-6)
        String projectTitle = role != null && role.getProjectEvidence() != null && !role.getProjectEvidence().isBlank()
                ? role.getProjectEvidence()
                : "Enterprise Full-Stack Platform with Authentication and Microservices";
        plan.setMilestoneProjectTitle("Milestone Capstone: " + plan.getTargetRole() + " Portfolio Benchmark");
        plan.setMilestoneProjectDescription(projectTitle);

        plan.getWeeks().add(new RoadmapWeek(
                5,
                "Phase 3: Production Portfolio Project",
                "Capstone Architecture & End-to-End Build",
                List.of(
                        "Architect the milestone capstone: " + (projectTitle.length() > 80 ? projectTitle.substring(0, 80) + "..." : projectTitle),
                        "Implement core user journeys, business validation, and asynchronous processing",
                        "Ensure test-driven development (TDD) coverage exceeding 80% on business-critical services"
                ),
                "Functional MVP deployed locally with end-to-end integration test suites passing.",
                "14-16 Hours",
                ytUrl(plan.getTargetRole() + " full stack portfolio project"),
                docUrl(framework2),
                "https://github.com/topics/fullstack",
                List.of(primarySkill1, framework1, framework2)
        ));

        plan.getWeeks().add(new RoadmapWeek(
                6,
                "Phase 3: Production Portfolio Project",
                "Cloud Deployment, CI/CD & Observability",
                List.of(
                        "Build automated GitHub Actions CI/CD pipelines with automated test gates and linters",
                        "Deploy application live to cloud hosting (AWS / GCP / Render / Vercel)",
                        "Configure observability: centralized logging, structured health probes, and latency telemetry"
                ),
                "Live public web demo URL running in cloud with documentation and architecture diagrams on GitHub.",
                "12-14 Hours",
                ytUrl("github actions cicd docker cloud deployment"),
                "https://docs.github.com/en/actions",
                "https://github.com/features/actions",
                List.of("Docker", "CI/CD", "Cloud")
        ));

        // Phase 4: Market Readiness & Career Acceleration (Weeks 7-8)
        plan.getWeeks().add(new RoadmapWeek(
                7,
                "Phase 4: Market Readiness & Opportunities",
                "Google X-Y-Z Resume Fine-Tuning & ATS Audit",
                List.of(
                        "Rewrite resume project bullets using Google's formula: 'Accomplished [X] measured by [Y] doing [Z]'",
                        "Upload updated resume to Smart Resume AI ATS Checker to verify a readiness score >= 85%",
                        "Target 15 verified openings across Remotive, LinkedIn, Indeed, and Wellfound"
                ),
                "Submit 15 customized, high-fit job applications with personalized cover letters.",
                "8-10 Hours",
                ytUrl("tech resume google xyz formula engineering"),
                "https://www.google.com/about/careers/applications/faq/",
                "https://smartresume.ai",
                List.of("Resume ATS", "Google X-Y-Z", "LinkedIn")
        ));

        plan.getWeeks().add(new RoadmapWeek(
                8,
                "Phase 4: Market Readiness & Opportunities",
                "Technical Interview Mastery & Mock Drills",
                List.of(
                        "Practice role-specific STAR behavioral answers (Situation, Task, Action, Result)",
                        "Simulate 3 technical whiteboarding / system design rounds using Smart Resume Interview Prep",
                        "Formulate insightful architectural questions for engineering managers and prepare offer negotiation"
                ),
                "Complete 3 full mock interview rounds and log all application statuses in the Kanban tracker.",
                "8-10 Hours",
                ytUrl(plan.getTargetRole() + " technical interview questions answers"),
                "https://github.com/jwasham/coding-interview-university",
                "https://leetcode.com",
                List.of("Interview Prep", "STAR Method", "System Design")
        ));

        // Industry Certifications
        if (role != null && role.getCertifications() != null && !role.getCertifications().isEmpty()) {
            plan.getRecommendedCertifications().addAll(role.getCertifications());
        } else {
            plan.getRecommendedCertifications().addAll(List.of(
                    "AWS Certified Solutions Architect – Associate",
                    "Certified Kubernetes Application Developer (CKAD)",
                    "HashiCorp Certified: Terraform Associate",
                    "Google Professional Cloud Architect"
            ));
        }

        // Prerequisites
        if (role != null && role.getPrerequisites() != null && !role.getPrerequisites().isEmpty()) {
            plan.getPrerequisites().addAll(role.getPrerequisites());
        } else {
            plan.getPrerequisites().addAll(List.of("Basic Data Structures", "Git Version Control", "Command Line Proficiency"));
        }

        // Trajectory Outcome & Market Context
        if (role != null && role.getMarketContext() != null && !role.getMarketContext().isBlank()) {
            plan.setCareerTrajectoryOutcome(role.getMarketContext() + " — Qualified for competitive hiring offers ($85,000 - $135,000 / ₹12 - ₹28 LPA) as a verified " + plan.getTargetRole() + ".");
        } else {
            plan.setCareerTrajectoryOutcome("Targeting competitive technology offers ($85,000 - $130,000 / ₹12 - ₹24 LPA) as a verified " + plan.getTargetRole() + ".");
        }

        return plan;
    }

    private String urlEncode(String value) {
        if (value == null) return "";
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String ytUrl(String query) {
        return "https://www.youtube.com/results?search_query=" + urlEncode(query);
    }

    private String docUrl(String tool) {
        if (tool == null) return "https://devdocs.io";
        String t = tool.toLowerCase();
        if (t.contains("java") || t.contains("spring")) return "https://spring.io/guides";
        if (t.contains("react")) return "https://react.dev";
        if (t.contains("python") || t.contains("django") || t.contains("fastapi")) return "https://docs.python.org/3/";
        if (t.contains("docker")) return "https://docs.docker.com";
        if (t.contains("kubernetes")) return "https://kubernetes.io/docs/";
        if (t.contains("aws")) return "https://docs.aws.amazon.com";
        if (t.contains("figma")) return "https://help.figma.com";
        if (t.contains("sql") || t.contains("postgres")) return "https://www.postgresql.org/docs/";
        return "https://devdocs.io/#q=" + urlEncode(tool);
    }
}
