package com.smartresume;

import com.smartresume.dto.ResumeAnalysisResult;
import com.smartresume.service.ResumeAnalyzerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResumeAnalyzerEdgeCasesTest {

    private final ResumeAnalyzerService analyzerService = new ResumeAnalyzerService();

    @Test
    void testInvoiceDetectionYieldsZeroAtsScore() {
        String invoiceText = "TAX INVOICE / RECEIPT\n" +
                "Invoice Number: INV-2024-8891\n" +
                "Bill To: Acme Global Enterprises\n" +
                "Due Date: 15-Nov-2024 Payment Terms: Net 30\n" +
                "Description | Qty | Unit Price | Total Amount\n" +
                "Cloud Server Hosting | 2 | $500.00 | $1,000.00\n" +
                "Subtotal: $1,000.00 Tax: $180.00 Total Amount: $1,180.00\n" +
                "Remit to: payments@cloudservices.com\n";

        ResumeAnalysisResult result = analyzerService.analyze(invoiceText, "Backend Developer");

        assertNotNull(result);
        assertEquals("invoice", result.getDocumentType(), "Invoice must be classified as non-resume invoice");
        assertEquals(0.0, result.getAtsScore(), "Invoice must yield 0 ATS score");
        assertTrue(result.getSuggestions().stream().anyMatch(s -> s.contains("invoice")));
    }

    @Test
    void testInternationalPhoneNumbersExtraction() {
        // India number format
        String textIndia = "Aarav Sharma\nEmail: aarav@example.com\nPhone: +91 98765 43210\n" +
                "SUMMARY: Software developer\nEXPERIENCE: Developer at Corp\nEDUCATION: B.Tech\nSKILLS: Java, SQL";
        ResumeAnalysisResult resultIndia = analyzerService.analyze(textIndia, "Backend Developer");
        assertNotNull(resultIndia.getPhone());
        assertTrue(resultIndia.getPhone().contains("98765"));

        // UK number format
        String textUK = "Oliver Smith\nEmail: oliver@example.co.uk\nPhone: +44 7911 123456\n" +
                "SUMMARY: Software developer\nEXPERIENCE: Developer at Corp\nEDUCATION: B.Sc\nSKILLS: Java, SQL";
        ResumeAnalysisResult resultUK = analyzerService.analyze(textUK, "Backend Developer");
        assertNotNull(resultUK.getPhone());
        assertTrue(resultUK.getPhone().contains("7911"));

        // US standard format
        String textUS = "Sarah Connor\nEmail: sarah@example.com\nPhone: (555) 234-5678\n" +
                "SUMMARY: Software developer\nEXPERIENCE: Developer at Corp\nEDUCATION: BS\nSKILLS: Java, SQL";
        ResumeAnalysisResult resultUS = analyzerService.analyze(textUS, "Backend Developer");
        assertNotNull(resultUS.getPhone());
        assertTrue(resultUS.getPhone().contains("234-5678") || resultUS.getPhone().contains("555"));
    }

    @Test
    void testMissingSkillsPopulateTargetedRecommendations() {
        // Resume missing Docker and Kubernetes for Cloud Engineer role
        String partialResume = "Jane Candidate\nEmail: jane@test.com Phone: 555-0101\n" +
                "SUMMARY: Experienced Linux and Networking specialist\n" +
                "EXPERIENCE: Systems Admin at Network Inc (2020-2023)\n• Managed server infrastructure\n" +
                "EDUCATION: BS in IT, 2020\n" +
                "SKILLS: Linux, Bash, Networking, Git, Python\n";

        ResumeAnalysisResult result = analyzerService.analyze(partialResume, "DevOps Engineer");

        assertNotNull(result);
        assertFalse(result.getMissingSkills().isEmpty(), "Should detect missing DevOps skills");
        // Verify recommended courses contain skill gap entries
        boolean hasSkillGapCourse = result.getRecommendedCourses().keySet().stream()
                .anyMatch(k -> k.contains("Skill Gap"));
        assertTrue(hasSkillGapCourse, "Missing skills must bridge directly to targeted learning courses");

        // Verify interview/tutorial videos contain targeted skill gap entries
        boolean hasSkillGapVideo = result.getInterviewVideos().keySet().stream()
                .anyMatch(k -> k.contains("Targeted Skill Gap"));
        assertTrue(hasSkillGapVideo, "Missing skills must bridge directly to targeted video tutorials");
    }

    @Test
    void testScoreBoundsEnforcedBetween0And100() {
        String minimalText = "John\nShort text with bare minimum data\n";
        ResumeAnalysisResult result = analyzerService.analyze(minimalText, "Full Stack Developer");

        assertTrue(result.getAtsScore() >= 0.0 && result.getAtsScore() <= 100.0);
        assertTrue(result.getContactScore() >= 0.0 && result.getContactScore() <= 100.0);
        assertTrue(result.getSummaryScore() >= 0.0 && result.getSummaryScore() <= 100.0);
        assertTrue(result.getFormatScore() >= 0.0 && result.getFormatScore() <= 100.0);
        assertTrue(result.getKeywordMatchScore() >= 0.0 && result.getKeywordMatchScore() <= 100.0);
    }
}
