package com.smartresume.agent;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Resume Generation Agent
 * Suggests impactful phrasing enhancements, strong action verbs, and quantifiable metrics
 * for candidate accomplishment bullet points.
 */
@Component
public class ResumeGenerationAgent {

    public List<String> suggestImprovements(List<String> rawExperienceBullets) {
        List<String> suggestions = new ArrayList<>();
        if (rawExperienceBullets == null || rawExperienceBullets.isEmpty()) {
            suggestions.add("Add quantified achievements leading with powerful verbs: 'Architected high-throughput microservice reducing latency by 35%'.");
            suggestions.add("Detail team collaboration and CI/CD automation: 'Spearheaded automated GitHub Actions pipeline cutting build time by 50%'.");
            return suggestions;
        }

        for (String bullet : rawExperienceBullets) {
            String trimmed = bullet.trim();
            if (trimmed.length() > 10) {
                if (!trimmed.matches("(?i)^(architected|spearheaded|developed|engineered|optimized|designed|implemented).*")) {
                    suggestions.add("Elevate impact: Begin accomplishment with strong verb — '" + trimmed.substring(0, Math.min(50, trimmed.length())) + "...' -> 'Architected and delivered...'");
                }
            }
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Ensure top 3 bullet points include specific quantifiable metrics (% performance increase, cost reduction, throughput).");
        }
        return suggestions;
    }
}
