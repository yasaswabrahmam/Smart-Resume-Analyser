package com.smartresume.agent;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Interview Preparation Agent
 * Generates tailored technical and behavioral mock interview questions
 * based on detected candidate resume weaknesses and missing skills.
 */
@Component
public class InterviewPreparationAgent {

    public List<String> generateQuestions(List<String> skillGaps, String targetRole) {
        List<String> questions = new ArrayList<>();
        questions.add("Technical Architecture: How do you handle database concurrency, indexing, and transactional isolation in high-throughput enterprise systems?");
        questions.add("System Resilience: Describe your approach to designing fault-tolerant microservices using circuit breakers and retry policies.");
        questions.add("Behavioral: Tell me about a time you identified a critical production performance bottleneck and how you measured the resolution.");

        if (skillGaps != null && !skillGaps.isEmpty()) {
            for (String gap : skillGaps.subList(0, Math.min(3, skillGaps.size()))) {
                questions.add("Domain Competency (" + gap + "): How would you implement and test " + gap + " in a production CI/CD environment?");
            }
        } else {
            questions.add("Cloud Infrastructure: How have you containerized and deployed backend services to AWS or Docker environments?");
        }

        return questions;
    }
}
