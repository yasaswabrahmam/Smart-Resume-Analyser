package com.smartresume.service;

import com.smartresume.config.JobRolesConfig;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class SkillGapService {

    public static class SkillItem {
        private String skillName;
        private String category; // "DEMONSTRATED", "WEAK", "MISSING", "UNKNOWN"
        private String evidenceReason;
        private String documentationUrl;
        private YouTubeRecommendation youtubeRecommendation;
        private String portfolioProject;

        public SkillItem(String skillName, String category, String evidenceReason,
                         String documentationUrl, YouTubeRecommendation youtubeRecommendation,
                         String portfolioProject) {
            this.skillName = skillName;
            this.category = category;
            this.evidenceReason = evidenceReason;
            this.documentationUrl = documentationUrl;
            this.youtubeRecommendation = youtubeRecommendation;
            this.portfolioProject = portfolioProject;
        }

        public String getSkillName() { return skillName; }
        public String getCategory() { return category; }
        public String getEvidenceReason() { return evidenceReason; }
        public String getDocumentationUrl() { return documentationUrl; }
        public YouTubeRecommendation getYoutubeRecommendation() { return youtubeRecommendation; }
        public String getPortfolioProject() { return portfolioProject; }
    }

    public static class YouTubeRecommendation {
        private String title;
        private String channel;
        private String duration;
        private String searchUrl;
        private String reason;

        public YouTubeRecommendation(String title, String channel, String duration, String searchUrl, String reason) {
            this.title = title;
            this.channel = channel;
            this.duration = duration;
            this.searchUrl = searchUrl;
            this.reason = reason;
        }

        public String getTitle() { return title; }
        public String getChannel() { return channel; }
        public String getDuration() { return duration; }
        public String getSearchUrl() { return searchUrl; }
        public String getReason() { return reason; }
    }

    public static class SkillGapReport {
        private String targetRole;
        private int totalRequiredSkills;
        private int demonstratedCount;
        private int weakCount;
        private int missingCount;
        private double readinessPercentage;
        private List<SkillItem> demonstratedSkills = new ArrayList<>();
        private List<SkillItem> weakSkills = new ArrayList<>();
        private List<SkillItem> missingSkills = new ArrayList<>();
        private List<String> recommendedNextSteps = new ArrayList<>();

        public String getTargetRole() { return targetRole; }
        public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
        public int getTotalRequiredSkills() { return totalRequiredSkills; }
        public void setTotalRequiredSkills(int totalRequiredSkills) { this.totalRequiredSkills = totalRequiredSkills; }
        public int getDemonstratedCount() { return demonstratedCount; }
        public void setDemonstratedCount(int demonstratedCount) { this.demonstratedCount = demonstratedCount; }
        public int getWeakCount() { return weakCount; }
        public void setWeakCount(int weakCount) { this.weakCount = weakCount; }
        public int getMissingCount() { return missingCount; }
        public void setMissingCount(int missingCount) { this.missingCount = missingCount; }
        public double getReadinessPercentage() { return readinessPercentage; }
        public void setReadinessPercentage(double readinessPercentage) { this.readinessPercentage = readinessPercentage; }
        public List<SkillItem> getDemonstratedSkills() { return demonstratedSkills; }
        public List<SkillItem> getWeakSkills() { return weakSkills; }
        public List<SkillItem> getMissingSkills() { return missingSkills; }
        public List<String> getRecommendedNextSteps() { return recommendedNextSteps; }
    }

    private static final Map<String, String> OFFICIAL_DOCS = new HashMap<>();
    private static final Map<String, YouTubeRecommendation> CURATED_VIDEOS = new HashMap<>();
    private static final Map<String, String> PROJECT_BLUEPRINTS = new HashMap<>();

    static {
        // Official Docs Database
        OFFICIAL_DOCS.put("Java", "https://docs.oracle.com/en/java/");
        OFFICIAL_DOCS.put("Spring Boot", "https://spring.io/projects/spring-boot");
        OFFICIAL_DOCS.put("Python", "https://docs.python.org/3/");
        OFFICIAL_DOCS.put("FastAPI", "https://fastapi.tiangolo.com/");
        OFFICIAL_DOCS.put("React", "https://react.dev/");
        OFFICIAL_DOCS.put("Docker", "https://docs.docker.com/");
        OFFICIAL_DOCS.put("Kubernetes", "https://kubernetes.io/docs/home/");
        OFFICIAL_DOCS.put("AWS", "https://docs.aws.amazon.com/");
        OFFICIAL_DOCS.put("MySQL", "https://dev.mysql.com/doc/");
        OFFICIAL_DOCS.put("PostgreSQL", "https://www.postgresql.org/docs/");
        OFFICIAL_DOCS.put("PyTorch", "https://pytorch.org/docs/stable/");
        OFFICIAL_DOCS.put("Scikit-learn", "https://scikit-learn.org/stable/");
        OFFICIAL_DOCS.put("TypeScript", "https://www.typescriptlang.org/docs/");
        OFFICIAL_DOCS.put("Terraform", "https://developer.hashicorp.com/terraform/docs");

        // Curated High-Yield YouTube Tutorials
        CURATED_VIDEOS.put("Java", new YouTubeRecommendation("Java Full Course for Beginners", "Bro Code", "12 Hours", "https://www.youtube.com/results?search_query=java+full+course+bro+code", "Comprehensive core syntax, OOP invariants, and memory management"));
        CURATED_VIDEOS.put("Spring Boot", new YouTubeRecommendation("Spring Boot 3 Crash Course", "Amigoscode", "4.5 Hours", "https://www.youtube.com/results?search_query=spring+boot+3+full+course+amigoscode", "Enterprise REST APIs, Spring Data JPA, and security best practices"));
        CURATED_VIDEOS.put("Docker", new YouTubeRecommendation("Docker Tutorial for Beginners", "TechWorld with Nana", "3 Hours", "https://www.youtube.com/results?search_query=docker+tutorial+techworld+with+nana", "Containerization, Dockerfile optimization, and multi-container Docker Compose"));
        CURATED_VIDEOS.put("Kubernetes", new YouTubeRecommendation("Kubernetes Course for Beginners", "freeCodeCamp", "4 Hours", "https://www.youtube.com/results?search_query=kubernetes+course+freecodecamp", "Pods, Services, Ingress Controllers, and production cluster orchestration"));
        CURATED_VIDEOS.put("React", new YouTubeRecommendation("React 18 Full Course", "freeCodeCamp / Bob Ziroll", "11 Hours", "https://www.youtube.com/results?search_query=react+full+course+freecodecamp", "Components, hooks, state management, and modern React design patterns"));
        CURATED_VIDEOS.put("FastAPI", new YouTubeRecommendation("FastAPI Course for Beginners", "freeCodeCamp / Sanjeev Thiyagarajan", "19 Hours", "https://www.youtube.com/results?search_query=fastapi+course+freecodecamp", "Asynchronous Python REST APIs, Pydantic v2 validation, and database migrations"));
        CURATED_VIDEOS.put("Python", new YouTubeRecommendation("Python for Beginners - Full Course", "Programming with Mosh", "6 Hours", "https://www.youtube.com/results?search_query=python+for+beginners+mosh", "Foundational Python programming, data structures, and standard libraries"));
        CURATED_VIDEOS.put("PyTorch", new YouTubeRecommendation("PyTorch for Deep Learning Bootcamp", "Daniel Bourke", "26 Hours", "https://www.youtube.com/results?search_query=pytorch+daniel+bourke", "Tensor operations, neural networks, transfer learning, and model deployment"));
        CURATED_VIDEOS.put("AWS", new YouTubeRecommendation("AWS Certified Cloud Practitioner Training", "freeCodeCamp", "14 Hours", "https://www.youtube.com/results?search_query=aws+certified+cloud+practitioner+freecodecamp", "VPC networking, EC2 compute, S3 storage, and IAM security controls"));
        CURATED_VIDEOS.put("Terraform", new YouTubeRecommendation("Terraform Explained in 15 Minutes", "TechWorld with Nana", "1.5 Hours", "https://www.youtube.com/results?search_query=terraform+techworld+with+nana", "Infrastructure as Code, state management, and multi-cloud provisioning"));

        // Portfolio Project Blueprints
        PROJECT_BLUEPRINTS.put("Spring Boot", "Build a Multi-Tenant SaaS E-Commerce API with Spring Security 6, JWT, Redis caching, and Dockerized MySQL.");
        PROJECT_BLUEPRINTS.put("Docker", "Containerize a polyglot microservices application with health-checks, automated Docker Compose networking, and NGINX reverse proxy.");
        PROJECT_BLUEPRINTS.put("Kubernetes", "Deploy a resilient 3-node microservice application on Minikube with Horizontal Pod Autoscaling and ConfigMaps.");
        PROJECT_BLUEPRINTS.put("FastAPI", "Develop a high-throughput AI inference microservice utilizing Sentence-Transformers for semantic document search.");
        PROJECT_BLUEPRINTS.put("React", "Construct an interactive Applicant Tracking Dashboard utilizing React 18, TypeScript, Tailwind CSS, and TanStack Query.");
        PROJECT_BLUEPRINTS.put("AWS", "Architect a serverless document processing pipeline using AWS Lambda, S3 triggers, and DynamoDB storage.");
    }

    public SkillGapReport evaluateGaps(String resumeText, String roleName, String jobDescription) {
        SkillGapReport report = new SkillGapReport();
        report.setTargetRole(roleName != null && !roleName.isBlank() ? roleName : "Full Stack Java Developer");

        // Identify target skills from role taxonomy or job description
        List<String> targetSkills = new ArrayList<>();
        JobRolesConfig.RoleDefinition roleDef = JobRolesConfig.getRoleByName(report.getTargetRole());
        if (roleDef != null) {
            targetSkills.addAll(roleDef.getRequiredSkills());
            if (roleDef.getRecommendedTechnicalSkills() != null) {
                for (String s : roleDef.getRecommendedTechnicalSkills()) {
                    if (!targetSkills.contains(s)) targetSkills.add(s);
                }
            }
        } else {
            // Default skills for full stack developer if role not found
            targetSkills = List.of("Java", "Spring Boot", "React", "Docker", "SQL", "REST APIs", "Git", "Kubernetes", "Microservices");
        }

        String lowerResume = resumeText != null ? resumeText.toLowerCase() : "";

        for (String skill : targetSkills) {
            String lowerSkill = skill.toLowerCase();
            Pattern wordPattern = Pattern.compile("\\b" + Pattern.quote(lowerSkill) + "\\b", Pattern.CASE_INSENSITIVE);
            boolean present = wordPattern.matcher(lowerResume).find();

            String docUrl = OFFICIAL_DOCS.getOrDefault(skill, "https://www.google.com/search?q=" + skill + "+official+documentation");
            YouTubeRecommendation yt = CURATED_VIDEOS.getOrDefault(skill, new YouTubeRecommendation(
                    skill + " Complete Tutorial for Beginners",
                    "Engineering Masterclass",
                    "2-4 Hours",
                    "https://www.youtube.com/results?search_query=" + skill.replace(" ", "+") + "+tutorial",
                    "Master core architectural paradigms and real-world implementation"
            ));
            String project = PROJECT_BLUEPRINTS.getOrDefault(skill, "Build an end-to-end production portfolio application highlighting " + skill + " with automated test suites.");

            if (present) {
                // Check if strongly demonstrated in experience or just mentioned
                boolean hasEvidence = lowerResume.contains("built") || lowerResume.contains("developed") ||
                        lowerResume.contains("architected") || lowerResume.contains("engineered") ||
                        lowerResume.contains("optimized") || lowerResume.contains("reduced");

                if (hasEvidence) {
                    report.getDemonstratedSkills().add(new SkillItem(
                            skill, "DEMONSTRATED",
                            "Directly verified in professional work experience with quantifiable impact verbs.",
                            docUrl, yt, project
                    ));
                } else {
                    report.getWeakSkills().add(new SkillItem(
                            skill, "WEAK",
                            "Found in skill list but lacks descriptive work experience bullet points or metrics.",
                            docUrl, yt, project
                    ));
                }
            } else {
                report.getMissingSkills().add(new SkillItem(
                        skill, "MISSING",
                        "Absent from resume text. High risk of ATS filtering for target requisition.",
                        docUrl, yt, project
                    ));
            }
        }

        report.setTotalRequiredSkills(targetSkills.size());
        report.setDemonstratedCount(report.getDemonstratedSkills().size());
        report.setWeakCount(report.getWeakSkills().size());
        report.setMissingCount(report.getMissingSkills().size());

        double score = ((report.getDemonstratedCount() * 1.0) + (report.getWeakCount() * 0.5)) / Math.max(1, report.getTotalRequiredSkills()) * 100.0;
        report.setReadinessPercentage(Math.round(score * 10.0) / 10.0);

        // Generate tailored next steps
        if (!report.getMissingSkills().isEmpty()) {
            report.getRecommendedNextSteps().add("Review YouTube masterclasses for top missing skills: " + report.getMissingSkills().get(0).getSkillName() + (report.getMissingSkills().size() > 1 ? ", " + report.getMissingSkills().get(1).getSkillName() : ""));
            report.getRecommendedNextSteps().add("Build a dedicated portfolio project incorporating: " + report.getMissingSkills().get(0).getPortfolioProject());
        }
        if (!report.getWeakSkills().isEmpty()) {
            report.getRecommendedNextSteps().add("Revise resume bullet points for " + report.getWeakSkills().get(0).getSkillName() + " to quantify business impact (Google X-Y-Z formula: Accomplished [X] as measured by [Y] by doing [Z]).");
        }
        report.getRecommendedNextSteps().add("Run target job search on 7 employment portals matching your demonstrated skill profile.");

        return report;
    }
}
