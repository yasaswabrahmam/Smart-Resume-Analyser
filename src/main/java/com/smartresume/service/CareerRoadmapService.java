package com.smartresume.service;

import org.springframework.stereotype.Service;

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

        public RoadmapWeek(int weekNumber, String phaseTitle, String focusArea,
                           List<String> learningGoals, String milestoneDeliverable, String estimatedHours) {
            this.weekNumber = weekNumber;
            this.phaseTitle = phaseTitle;
            this.focusArea = focusArea;
            this.learningGoals = learningGoals;
            this.milestoneDeliverable = milestoneDeliverable;
            this.estimatedHours = estimatedHours;
        }

        public int getWeekNumber() { return weekNumber; }
        public String getPhaseTitle() { return phaseTitle; }
        public String getFocusArea() { return focusArea; }
        public List<String> getLearningGoals() { return learningGoals; }
        public String getMilestoneDeliverable() { return milestoneDeliverable; }
        public String getEstimatedHours() { return estimatedHours; }
    }

    public static class CareerRoadmapPlan {
        private String targetRole;
        private int durationWeeks;
        private String difficultyLevel;
        private List<RoadmapWeek> weeks = new ArrayList<>();
        private List<String> recommendedCertifications = new ArrayList<>();
        private String careerTrajectoryOutcome;

        public String getTargetRole() { return targetRole; }
        public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
        public int getDurationWeeks() { return durationWeeks; }
        public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }
        public String getDifficultyLevel() { return difficultyLevel; }
        public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
        public List<RoadmapWeek> getWeeks() { return weeks; }
        public List<String> getRecommendedCertifications() { return recommendedCertifications; }
        public String getCareerTrajectoryOutcome() { return careerTrajectoryOutcome; }
        public void setCareerTrajectoryOutcome(String careerTrajectoryOutcome) { this.careerTrajectoryOutcome = careerTrajectoryOutcome; }
    }

    public CareerRoadmapPlan generateRoadmap(String targetRole, List<String> missingSkills) {
        CareerRoadmapPlan plan = new CareerRoadmapPlan();
        plan.setTargetRole(targetRole != null && !targetRole.isBlank() ? targetRole : "Full Stack Java Developer");
        plan.setDurationWeeks(8);
        plan.setDifficultyLevel("Intermediate to Advanced");

        String primaryMissing = !missingSkills.isEmpty() ? missingSkills.get(0) : "Microservices Architecture";
        String secondaryMissing = missingSkills.size() > 1 ? missingSkills.get(1) : "Docker & Kubernetes";

        // Phase 1: Weeks 1-2
        plan.getWeeks().add(new RoadmapWeek(
                1, "Phase 1: Foundation & Skill Remediation", "Core Language Mastery & Missing Prerequisites",
                List.of(
                        "Review language invariants and memory models for " + plan.getTargetRole(),
                        "Master foundational concepts of " + primaryMissing + " through curated documentation and tutorials",
                        "Configure clean development environment with Git, testing frameworks, and linters"
                ),
                "Complete 10 algorithmic problem challenges and verify code quality with automated unit tests",
                "10-12 Hours"
        ));

        plan.getWeeks().add(new RoadmapWeek(
                2, "Phase 1: Foundation & Skill Remediation", "Deep Dive into " + primaryMissing,
                List.of(
                        "Implement idiomatic patterns and error-handling hierarchies",
                        "Build modular component structures adhering to SOLID design principles",
                        "Study official reference documentation and standard library capabilities"
                ),
                "Push working prototype repository to GitHub with comprehensive README documentation",
                "12-14 Hours"
        ));

        // Phase 2: Weeks 3-4
        plan.getWeeks().add(new RoadmapWeek(
                3, "Phase 2: Architecture & Integration", "Framework Deep Dives & Database Engineering",
                List.of(
                        "Design normalized relational database schemas with indexing and query optimization",
                        "Implement transactional boundaries and high-throughput RESTful endpoints",
                        "Study caching strategies, pagination, and API security headers"
                ),
                "Develop functional backend service with database migrations and integration test suites",
                "12-15 Hours"
        ));

        plan.getWeeks().add(new RoadmapWeek(
                4, "Phase 2: Architecture & Integration", "Secondary Competency Mastery: " + secondaryMissing,
                List.of(
                        "Integrate " + secondaryMissing + " into your application architecture",
                        "Implement containerization and environment parity across staging and production",
                        "Verify security safeguards, input sanitization, and secret management"
                ),
                "Containerize service with Docker Compose and verify zero-downtime health endpoints",
                "10-14 Hours"
        ));

        // Phase 3: Weeks 5-6
        plan.getWeeks().add(new RoadmapWeek(
                5, "Phase 3: Production Portfolio Project", "Full-Stack System Architecture & UI Integration",
                List.of(
                        "Connect responsive user interface to authenticated backend microservices",
                        "Implement asynchronous event handling and state management",
                        "Ensure WCAG accessibility, responsive breakpoints, and client-side validation"
                ),
                "Deploy functional full-stack MVP to cloud staging environment (AWS / GCP / Render)",
                "14-16 Hours"
        ));

        plan.getWeeks().add(new RoadmapWeek(
                6, "Phase 3: Production Portfolio Project", "CI/CD Automation, Observability & Performance Tuning",
                List.of(
                        "Establish automated GitHub Actions CI/CD pipelines with test coverage gates",
                        "Configure centralized logging, metrics collection, and latency monitoring",
                        "Conduct load testing and query optimization to reduce endpoint p99 latency"
                ),
                "Live public demo URL published on GitHub with interactive architecture diagram",
                "12-14 Hours"
        ));

        // Phase 4: Weeks 7-8
        plan.getWeeks().add(new RoadmapWeek(
                7, "Phase 4: Market Readiness & Applications", "ATS Resume Fine-Tuning & Targeted Job Discovery",
                List.of(
                        "Update resume bullet points using the Google X-Y-Z formula highlighting your new projects",
                        "Re-run Smart Resume AI ATS audit to achieve an 85%+ readiness score",
                        "Launch targeted searches across 7 employment portals (LinkedIn, Naukri, Indeed)"
                ),
                "Submit 15 customized, high-match applications with tailored cover letters",
                "8-10 Hours"
        ));

        plan.getWeeks().add(new RoadmapWeek(
                8, "Phase 4: Market Readiness & Applications", "Technical Interview Preparation & Negotiation",
                List.of(
                        "Practice role-specific technical questions generated by AI Career Coach",
                        "Conduct mock behavioral interviews using the STAR method (Situation, Task, Action, Result)",
                        "Review system design trade-offs and prepare insightful questions for interviewers"
                ),
                "Complete 3 full mock interviews and track application pipeline in the Application Tracker",
                "8-10 Hours"
        ));

        // Certifications & Outcome
        plan.getRecommendedCertifications().addAll(List.of(
                "Oracle Certified Professional: Java SE 21 Developer",
                "AWS Certified Solutions Architect – Associate",
                "HashiCorp Certified: Terraform Associate",
                "Docker & Kubernetes Certified Application Developer (CKAD)"
        ));

        plan.setCareerTrajectoryOutcome("Targeting competitive engineering offers ($85,000 - $130,000 / ₹12 - ₹24 LPA) as a qualified " + plan.getTargetRole());

        return plan;
    }
}
