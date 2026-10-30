package com.smartresume.agent;

import com.smartresume.config.JobRolesConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Skill Gap Agent
 * Compares detected candidate competencies against industry role benchmarks
 * and isolates missing critical skills.
 */
@Component
public class SkillGapAgent {

    public List<String> diagnoseGaps(List<String> foundSkills, String targetRole) {
        List<String> requiredSkills = new ArrayList<>();
        JobRolesConfig.RoleDefinition roleDef = JobRolesConfig.getRoleByName(targetRole);

        if (roleDef != null) {
            requiredSkills.addAll(roleDef.getRequiredSkills());
        } else {
            requiredSkills = List.of("Java", "Spring Boot", "SQL", "Git", "REST APIs", "Docker", "Microservices");
        }

        List<String> normalizedFound = (foundSkills != null) ?
                foundSkills.stream().map(String::toLowerCase).collect(Collectors.toList()) :
                new ArrayList<>();

        List<String> missing = new ArrayList<>();
        for (String skill : requiredSkills) {
            if (!normalizedFound.contains(skill.toLowerCase())) {
                missing.add(skill);
            }
        }
        return missing;
    }
}
