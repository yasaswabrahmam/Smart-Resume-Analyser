package com.smartresume;

import com.smartresume.dto.ResumeBuilderDto;
import com.smartresume.service.ResumeBuilderService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResumeBuilderServiceTest {

    private final ResumeBuilderService builderService = new ResumeBuilderService();

    @Test
    void testGenerateAllFourTemplatesDocxAndPdf() {
        String[] templates = {"Modern", "Professional", "Minimal", "Creative"};

        for (String tpl : templates) {
            ResumeBuilderDto dto = new ResumeBuilderDto();
            dto.setFullName("Test Candidate");
            dto.setEmail("candidate@example.com");
            dto.setPhone("+1234567890");
            dto.setLocation("Bangalore, India");
            dto.setTitle("Lead Java Architect");
            dto.setSummary("Seasoned software engineer with profound expertise in distributed systems.");
            dto.setTemplate(tpl);
            dto.setTechnicalSkills(List.of("Java", "Spring Boot", "MySQL", "AWS"));
            dto.setSoftSkills(List.of("Leadership", "Communication"));

            ResumeBuilderDto.ExperienceItem exp = new ResumeBuilderDto.ExperienceItem();
            exp.setCompany("Cloud Corp");
            exp.setPosition("Senior Engineer");
            exp.setStartDate("2021");
            exp.setEndDate("2024");
            exp.setResponsibilities("Engineered microservices architecture.");
            dto.setExperiences(List.of(exp));

            ResumeBuilderDto.EducationItem edu = new ResumeBuilderDto.EducationItem();
            edu.setSchool("Tech Institute");
            edu.setDegree("B.Tech");
            edu.setField("Computer Science");
            edu.setGraduationDate("2021");
            dto.setEducations(List.of(edu));

            // Test DOCX generation
            byte[] docx = builderService.generateDocx(dto);
            assertNotNull(docx, "DOCX output should not be null for template: " + tpl);
            assertTrue(docx.length > 500, "DOCX file should contain binary content for template: " + tpl);

            // Test PDF generation
            byte[] pdf = builderService.generatePdf(dto);
            assertNotNull(pdf, "PDF output should not be null for template: " + tpl);
            assertTrue(pdf.length > 500, "PDF file should contain binary content for template: " + tpl);
        }
    }
}
