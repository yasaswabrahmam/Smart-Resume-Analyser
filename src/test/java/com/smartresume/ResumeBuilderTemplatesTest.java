package com.smartresume;

import com.smartresume.dto.ResumeBuilderDto;
import com.smartresume.service.ResumeBuilderService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResumeBuilderTemplatesTest {

    private final ResumeBuilderService builderService = new ResumeBuilderService();

    @Test
    void testAllFourTemplatesDocxGeneration() {
        String[] templates = {"Modern", "Professional", "Minimal", "Creative"};

        for (String template : templates) {
            ResumeBuilderDto dto = createSampleDto();
            dto.setTemplate(template);

            byte[] docx = builderService.generateDocx(dto);
            assertNotNull(docx, "Generated DOCX should not be null for template: " + template);
            assertTrue(docx.length > 500, "DOCX should have valid content size for template: " + template);
            // Verify DOCX ZIP header (PK\x03\x04)
            assertEquals(0x50, docx[0]);
            assertEquals(0x4B, docx[1]);
        }
    }

    @Test
    void testAllFourTemplatesPdfGeneration() {
        String[] templates = {"Modern", "Professional", "Minimal", "Creative"};

        for (String template : templates) {
            ResumeBuilderDto dto = createSampleDto();
            dto.setTemplate(template);

            byte[] pdf = builderService.generatePdf(dto);
            assertNotNull(pdf, "Generated PDF should not be null for template: " + template);
            assertTrue(pdf.length > 500, "PDF should have valid content size for template: " + template);
            // Verify PDF header (%PDF)
            String header = new String(pdf, 0, Math.min(pdf.length, 5));
            assertTrue(header.startsWith("%PDF"), "PDF header should be %PDF for template: " + template);
        }
    }

    @Test
    void testNullSafetyAcrossAllTemplates() {
        // Completely empty DTO with null fields
        ResumeBuilderDto emptyDto = new ResumeBuilderDto();
        emptyDto.setFullName(null);
        emptyDto.setEmail(null);
        emptyDto.setPhone(null);
        emptyDto.setLocation(null);
        emptyDto.setSummary(null);
        emptyDto.setExperiences(null);
        emptyDto.setEducations(null);
        emptyDto.setProjects(null);
        emptyDto.setTechnicalSkills(null);
        emptyDto.setSoftSkills(null);

        String[] templates = {"Modern", "Professional", "Minimal", "Creative"};

        for (String template : templates) {
            emptyDto.setTemplate(template);

            assertDoesNotThrow(() -> {
                byte[] docx = builderService.generateDocx(emptyDto);
                assertNotNull(docx);
                assertTrue(docx.length > 0);
            }, "DOCX generation must be 100% null safe for template: " + template);

            assertDoesNotThrow(() -> {
                byte[] pdf = builderService.generatePdf(emptyDto);
                assertNotNull(pdf);
                assertTrue(pdf.length > 0);
            }, "PDF generation must be 100% null safe for template: " + template);
        }
    }

    private ResumeBuilderDto createSampleDto() {
        ResumeBuilderDto dto = new ResumeBuilderDto();
        dto.setFullName("Alexander Pierce");
        dto.setTitle("Lead Cloud Architect");
        dto.setEmail("alex@example.com");
        dto.setPhone("+1-555-0199");
        dto.setLocation("Seattle, WA");
        dto.setLinkedin("linkedin.com/in/alexpierce");
        dto.setSummary("High-performing Cloud Architect with 8+ years designing fault-tolerant distributed systems.");

        ResumeBuilderDto.ExperienceItem exp = new ResumeBuilderDto.ExperienceItem();
        exp.setPosition("Senior Cloud Engineer");
        exp.setCompany("Cloud Tech Inc");
        exp.setStartDate("2021");
        exp.setEndDate("Present");
        exp.setDescription("Spearheaded enterprise cloud migration");
        exp.setResponsibilities("Architected Kubernetes clusters\nReduced infrastructure latency by 40%");
        dto.setExperiences(List.of(exp));

        ResumeBuilderDto.ProjectItem proj = new ResumeBuilderDto.ProjectItem();
        proj.setName("AI Resume Engine");
        proj.setTechnologies("Java, Spring Boot, OpenAI, Docker");
        proj.setDescription("Full-stack resume evaluation pipeline.");
        dto.setProjects(List.of(proj));

        ResumeBuilderDto.EducationItem edu = new ResumeBuilderDto.EducationItem();
        edu.setSchool("Washington State University");
        edu.setDegree("B.S.");
        edu.setField("Computer Science");
        edu.setGraduationDate("2020");
        edu.setGpa("3.85");
        dto.setEducations(List.of(edu));

        dto.setTechnicalSkills(List.of("Java", "Spring Boot", "Docker", "Kubernetes", "AWS"));
        dto.setSoftSkills(List.of("Leadership", "Communication", "Problem-solving"));

        return dto;
    }
}
