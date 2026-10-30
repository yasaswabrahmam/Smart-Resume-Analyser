package com.smartresume;

import com.smartresume.dto.ResumeAnalysisResult;
import com.smartresume.service.ResumeAnalyzerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResumeAnalyzerServiceTest {

    private final ResumeAnalyzerService analyzerService = new ResumeAnalyzerService();

    @Test
    void testStandardAnalysisWithValidResume() {
        String resumeText = "Yasaswa Brahmam\n" +
                "Email: yasaswa@example.com Phone: 987-654-3210 linkedin.com/in/yasaswa\n" +
                "PROFESSIONAL SUMMARY\n" +
                "Accomplished Software Engineer with over 3 years of experience in Java, Spring Boot, MySQL, and microservices architecture.\n" +
                "EXPERIENCE\n" +
                "Software Engineer at Enterprise Systems (2022 - Present)\n" +
                "• Developed high-performance backend microservices using Java and Spring Boot\n" +
                "• Designed SQL database queries and improved API throughput\n" +
                "EDUCATION\n" +
                "B.Tech in Computer Science, State University, 2022, GPA: 3.9\n" +
                "SKILLS\n" +
                "Java, Spring Boot, Python, SQL, REST APIs, Microservices, Git, Docker, Database Design\n";

        ResumeAnalysisResult result = analyzerService.analyze(resumeText, "Backend Developer");

        assertNotNull(result);
        assertEquals("resume", result.getDocumentType());
        assertTrue(result.getAtsScore() > 60.0, "ATS Score should be high for well-formatted resume");
        assertTrue(result.getKeywordMatchScore() >= 50.0, "Keyword score should match Backend Developer skills");
        assertFalse(result.getFoundSkills().isEmpty());
        assertEquals("yasaswa@example.com", result.getEmail());
    }

    @Test
    void testNonResumeDocumentDetection() {
        String marksheetText = "BOARD OF SECONDARY EDUCATION\n" +
                "SEMESTER EXAMINATION RESULTS GRADE CARD\n" +
                "Student ID: 12345 Examination Year: 2023\n" +
                "Marks Obtained: Mathematics 95/100, Physics 90/100, Chemistry 88/100\n" +
                "SGPA: 9.2 CGPA: 9.1 Result: Passed with Distinction\n";

        ResumeAnalysisResult result = analyzerService.analyze(marksheetText, "Frontend Developer");

        assertNotNull(result);
        assertEquals("marksheet", result.getDocumentType());
        assertEquals(0.0, result.getAtsScore(), "Non-resume documents should yield 0 ATS score");
    }
}
