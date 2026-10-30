package com.smartresume;

import com.smartresume.config.CourseConfig;
import com.smartresume.service.ExportService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityHardenedTest {

    @Test
    void testCsvFormulaInjectionSanitization() {
        ExportService exportService = new ExportService(null);

        // Standard OWASP CSV Formula Injection payloads
        String equalFormula = "=1+1";
        String plusFormula = "+cmd|' /C calc'!A0";
        String minusFormula = "-2+3";
        String atFormula = "@SUM(A1:A10)";
        String tabFormula = "\t=1+1";
        String safeName = "John Doe";

        String escapedEqual = exportService.escapeCsv(equalFormula);
        String escapedPlus = exportService.escapeCsv(plusFormula);
        String escapedMinus = exportService.escapeCsv(minusFormula);
        String escapedAt = exportService.escapeCsv(atFormula);
        String escapedTab = exportService.escapeCsv(tabFormula);
        String escapedSafe = exportService.escapeCsv(safeName);

        // Prepending single quote neutralizes spreadsheet formula execution
        assertTrue(escapedEqual.startsWith("\"'="), "Formula starting with = must be prepended with a quote");
        assertTrue(escapedPlus.startsWith("\"'+"), "Formula starting with + must be prepended with a quote");
        assertTrue(escapedMinus.startsWith("\"'-"), "Formula starting with - must be prepended with a quote");
        assertTrue(escapedAt.startsWith("\"'@"), "Formula starting with @ must be prepended with a quote");
        assertTrue(escapedTab.startsWith("\"'\t"), "Formula starting with tab must be prepended with a quote");
        assertEquals("\"John Doe\"", escapedSafe, "Normal text should not be prepended with quote");
    }

    @Test
    void testCourseConfigRealYouTubeUrlsAndSafeFallback() {
        // Test verified skill tutorials have real legitimate URLs
        String[] dockerVideo = CourseConfig.getVideoRecommendation("Docker");
        assertNotNull(dockerVideo);
        assertEquals("Docker Tutorial for Beginners [Hands-on]", dockerVideo[0]);
        assertTrue(dockerVideo[1].startsWith("https://youtu.be/"), "Verified video should use authentic YouTube URL");
        assertFalse(dockerVideo[1].contains("watch?v=dQw4w9WgXcQ"), "Should not be a dummy placeholder");

        String[] springBootVideo = CourseConfig.getVideoRecommendation("Spring Boot");
        assertNotNull(springBootVideo);
        assertEquals("Spring Boot Full Course - 2024", springBootVideo[0]);
        assertTrue(springBootVideo[1].startsWith("https://youtu.be/"));

        // Test safe fallback for unknown skill: generates real YouTube search query URL, never invented fake video IDs
        String[] unknownSkillVideo = CourseConfig.getVideoRecommendation("QuantumComputingX9", "AI Researcher");
        assertNotNull(unknownSkillVideo);
        assertTrue(unknownSkillVideo[1].startsWith("https://www.youtube.com/results?search_query="),
                "Fallback must be a safe YouTube search query");
        assertEquals("Targeted YouTube Learning Search", unknownSkillVideo[2]);
    }

    @Test
    void testSkillToCoursesMapping() {
        List<String[]> dockerCourses = CourseConfig.getCoursesForSkill("Docker");
        assertFalse(dockerCourses.isEmpty(), "Docker courses should be mapped");
        assertTrue(dockerCourses.stream().anyMatch(c -> c[0].toLowerCase().contains("docker")));

        List<String[]> javaCourses = CourseConfig.getCoursesForSkill("Java");
        assertFalse(javaCourses.isEmpty(), "Java courses should be mapped");
    }
}
