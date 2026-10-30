package com.smartresume;

import com.smartresume.service.ResumeParserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResumeParserServiceTest {

    private final ResumeParserService parserService = new ResumeParserService();

    @Test
    void testExtractCommonSkills() {
        String sampleText = "Experienced Software Engineer skilled in Java, Python, Spring Boot, Docker, and MySQL databases.";
        List<String> skills = parserService.extractCommonSkills(sampleText);

        assertNotNull(skills);
        assertTrue(skills.contains("java"));
        assertTrue(skills.contains("python"));
        assertTrue(skills.contains("spring"));
        assertTrue(skills.contains("docker"));
        assertTrue(skills.contains("mysql"));
    }
}
