package com.smartresume.service;

import com.smartresume.config.JobRolesConfig;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewPrepService {

    public static class InterviewQuestion {
        private String category; // "TECHNICAL", "SYSTEM_DESIGN", "BEHAVIORAL", "CODING"
        private String question;
        private String idealAnswerGuide;
        private String evaluationCriteria;
        private String difficulty; // "BEGINNER", "INTERMEDIATE", "ADVANCED"

        public InterviewQuestion(String category, String question, String idealAnswerGuide, String evaluationCriteria, String difficulty) {
            this.category = category;
            this.question = question;
            this.idealAnswerGuide = idealAnswerGuide;
            this.evaluationCriteria = evaluationCriteria;
            this.difficulty = difficulty;
        }

        public String getCategory() { return category; }
        public String getQuestion() { return question; }
        public String getIdealAnswerGuide() { return idealAnswerGuide; }
        public String getEvaluationCriteria() { return evaluationCriteria; }
        public String getDifficulty() { return difficulty; }
    }

    public static class InterviewPrepKit {
        private String targetRole;
        private List<InterviewQuestion> technicalQuestions = new ArrayList<>();
        private List<InterviewQuestion> systemDesignQuestions = new ArrayList<>();
        private List<InterviewQuestion> behavioralQuestions = new ArrayList<>();
        private List<String> topRedFlagsToAvoid = new ArrayList<>();
        private List<String> questionsToAskInterviewer = new ArrayList<>();

        public String getTargetRole() { return targetRole; }
        public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
        public List<InterviewQuestion> getTechnicalQuestions() { return technicalQuestions; }
        public List<InterviewQuestion> getSystemDesignQuestions() { return systemDesignQuestions; }
        public List<InterviewQuestion> getBehavioralQuestions() { return behavioralQuestions; }
        public List<String> getTopRedFlagsToAvoid() { return topRedFlagsToAvoid; }
        public List<String> getQuestionsToAskInterviewer() { return questionsToAskInterviewer; }
    }

    public InterviewPrepKit generatePrepKit(String targetRole) {
        InterviewPrepKit kit = new InterviewPrepKit();
        kit.setTargetRole(targetRole != null && !targetRole.isBlank() ? targetRole : "Full Stack Java Developer");

        // Technical Questions
        kit.getTechnicalQuestions().add(new InterviewQuestion(
                "TECHNICAL",
                "How does the JVM garbage collector work, and what are the key differences between G1, ZGC, and Parallel GC?",
                "Explain generational heap memory (Young, Old, Metaspace). Describe pause times, concurrent marking, and when low-latency GC (ZGC) is preferred over throughput GC (Parallel GC).",
                "Deep understanding of Java memory management, heap sizing (-Xms, -Xmx), and GC tuning flags.",
                "ADVANCED"
        ));

        kit.getTechnicalQuestions().add(new InterviewQuestion(
                "TECHNICAL",
                "How do Spring Boot microservices manage distributed transactions across independent databases?",
                "Discuss why traditional 2PC (Two-Phase Commit) doesn't scale. Detail the Saga Pattern (Orchestration vs Choreography), compensating transactions, and idempotency guarantees.",
                "Microservice architecture resilience, event-driven decoupling (Kafka/RabbitMQ), and data consistency.",
                "ADVANCED"
        ));

        kit.getTechnicalQuestions().add(new InterviewQuestion(
                "TECHNICAL",
                "Explain the difference between optimistic locking and pessimistic locking in JPA/Hibernate.",
                "Detail @Version column timestamp/number comparison for optimistic locking vs SELECT FOR UPDATE database-level row locking for pessimistic locking.",
                "Database concurrency control, transaction isolation levels, and race condition prevention.",
                "INTERMEDIATE"
        ));

        // System Design Questions
        kit.getSystemDesignQuestions().add(new InterviewQuestion(
                "SYSTEM_DESIGN",
                "Design a high-throughput URL shortening service (like Bitly) capable of handling 100M daily writes.",
                "Estimate QPS and storage. Propose Base62 encoding with Snowflake ID generator. Introduce Redis caching for read amplification and partition database by hash keys.",
                "Capacity estimation, caching layers, write-heavy database sharding, and collision resolution.",
                "ADVANCED"
        ));

        kit.getSystemDesignQuestions().add(new InterviewQuestion(
                "SYSTEM_DESIGN",
                "How would you architect a resilient real-time notification service delivering millions of push/email events?",
                "Propose an event-driven architecture using Kafka topic partitioning, consumer group workers, dead-letter queues (DLQ), and circuit breakers for external third-party email APIs.",
                "Decoupled asynchronous design, backpressure handling, and graceful degradation.",
                "ADVANCED"
        ));

        // Behavioral Questions (STAR Method)
        kit.getBehavioralQuestions().add(new InterviewQuestion(
                "BEHAVIORAL",
                "Tell me about a time you diagnosed and resolved a critical production bug under intense time pressure.",
                "Use STAR: Situation (production downtime/latency spike), Task (triage and isolate root cause), Action (checked metrics, analyzed thread dumps/logs, rolled back or patched), Result (restored service in 18 minutes, added automated regression test).",
                "Composure under stress, systematic debugging methodology, and blameless post-mortem accountability.",
                "INTERMEDIATE"
        ));

        kit.getBehavioralQuestions().add(new InterviewQuestion(
                "BEHAVIORAL",
                "Describe a situation where you had a strong technical disagreement with a senior engineer or product manager.",
                "Explain how you gathered objective benchmark data, presented trade-offs calmly, listened to opposing business constraints, and committed fully to the chosen decision (Disagree and Commit).",
                "Professional maturity, communication of technical trade-offs, and team alignment.",
                "INTERMEDIATE"
        ));

        // Red Flags to Avoid
        kit.getTopRedFlagsToAvoid().addAll(List.of(
                "Speaking negatively about former employers or team members.",
                "Saying 'I don't know' without offering a hypothesis or describing how you would investigate.",
                "Failing to ask clarifying questions before jumping into system design or coding.",
                "Over-engineering a simple problem with unnecessary distributed microservices."
        ));

        // Questions to Ask Interviewer
        kit.getQuestionsToAskInterviewer().addAll(List.of(
                "What does the deployment and on-call escalation workflow look like for the engineering team?",
                "How does the team balance shipping new product features versus tackling accumulated technical debt?",
                "What would success look like for someone in this role over their first 90 days?"
        ));

        return kit;
    }
}
