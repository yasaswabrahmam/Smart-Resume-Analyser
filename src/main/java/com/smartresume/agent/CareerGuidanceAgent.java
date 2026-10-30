package com.smartresume.agent;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Career Guidance Agent
 * Generates actionable 30/60/90-day learning curriculum and certification pathways
 * grounded on detected skill gaps.
 */
@Component
public class CareerGuidanceAgent {

    public List<String> generateRoadmap(List<String> skillGaps, String targetRole) {
        List<String> roadmap = new ArrayList<>();
        if (skillGaps == null || skillGaps.isEmpty()) {
            roadmap.add("Month 1: Advance existing core proficiencies by building end-to-end distributed applications.");
            roadmap.add("Month 2: Prepare for advanced industry certifications in " + (targetRole != null ? targetRole : "Software Engineering") + ".");
            roadmap.add("Month 3: Engage in open-source collaboration and mock system design interviews.");
            return roadmap;
        }

        roadmap.add("Stage 1 (Days 1-30): Master core missing fundamentals: " + String.join(", ", skillGaps.subList(0, Math.min(2, skillGaps.size()))) + ".");
        if (skillGaps.size() > 2) {
            roadmap.add("Stage 2 (Days 31-60): Implement applied projects integrating: " + String.join(", ", skillGaps.subList(2, Math.min(4, skillGaps.size()))) + ".");
        } else {
            roadmap.add("Stage 2 (Days 31-60): Build a public GitHub portfolio showcase demonstrating hands-on architectural implementations.");
        }
        roadmap.add("Stage 3 (Days 61-90): Complete comprehensive interview preparation and peer code reviews.");
        return roadmap;
    }
}
