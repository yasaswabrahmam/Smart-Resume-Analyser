package com.smartresume;

import com.smartresume.service.FileValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileValidationServiceTest {

    private final FileValidationService validator = new FileValidationService();

    @Test
    void testValidPdfUpload() {
        byte[] validPdfBytes = "%PDF-1.5\nSample PDF resume body text".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "resumeFile",
                "john_doe_resume.pdf",
                "application/pdf",
                validPdfBytes
        );

        FileValidationService.ValidationResult result = validator.validateResumeFile(file);

        assertTrue(result.isValid(), "Valid PDF should pass validation");
        assertEquals("PDF", result.getDetectedType());
        assertEquals("john_doe_resume.pdf", result.getSanitizedFileName());
        assertNull(result.getErrorMessage());
    }

    @Test
    void testValidDocxUpload() {
        // PK\x03\x04 header for DOCX (Zip archive)
        byte[] validDocxBytes = new byte[]{0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x06, 0x00};
        MockMultipartFile file = new MockMultipartFile(
                "resumeFile",
                "jane_smith_resume.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                validDocxBytes
        );

        FileValidationService.ValidationResult result = validator.validateResumeFile(file);

        assertTrue(result.isValid(), "Valid DOCX should pass validation");
        assertEquals("DOCX", result.getDetectedType());
        assertEquals("jane_smith_resume.docx", result.getSanitizedFileName());
    }

    @Test
    void testFakePdfMagicBytesMismatch() {
        // Plain text content disguised as a .pdf file
        byte[] fakePdfBytes = "This is a plain text file, not a PDF".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "resumeFile",
                "malicious_disguise.pdf",
                "application/pdf",
                fakePdfBytes
        );

        FileValidationService.ValidationResult result = validator.validateResumeFile(file);

        assertFalse(result.isValid(), "File with invalid magic bytes must be rejected");
        assertTrue(result.getErrorMessage().contains("magic bytes mismatch"));
    }

    @Test
    void testPathTraversalInFilename() {
        byte[] validPdfBytes = "%PDF-1.4\nTest".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "resumeFile",
                "../../etc/passwd.pdf",
                "application/pdf",
                validPdfBytes
        );

        FileValidationService.ValidationResult result = validator.validateResumeFile(file);

        assertFalse(result.isValid(), "Path traversal in filename must be rejected");
        assertTrue(result.getErrorMessage().contains("path traversal"));
    }

    @Test
    void testUnsupportedExtension() {
        byte[] bytes = "%PDF-1.4\nTest".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "resumeFile",
                "resume.exe",
                "application/octet-stream",
                bytes
        );

        FileValidationService.ValidationResult result = validator.validateResumeFile(file);

        assertFalse(result.isValid(), "Non-PDF/DOCX extension must be rejected");
        assertTrue(result.getErrorMessage().contains("Unsupported file format"));
    }

    @Test
    void testEmptyAndNullFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "resumeFile",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        FileValidationService.ValidationResult res1 = validator.validateResumeFile(emptyFile);
        assertFalse(res1.isValid(), "Empty file must fail validation");

        FileValidationService.ValidationResult res2 = validator.validateResumeFile(null);
        assertFalse(res2.isValid(), "Null file must fail validation");
    }

    @Test
    void testOversizedFile() {
        byte[] header = "%PDF".getBytes();
        MockMultipartFile largeFile = new MockMultipartFile(
                "resumeFile",
                "giant.pdf",
                "application/pdf",
                header
        ) {
            @Override
            public long getSize() {
                return 25 * 1024 * 1024; // 25 MB
            }
        };

        FileValidationService.ValidationResult result = validator.validateResumeFile(largeFile);

        assertFalse(result.isValid(), "File exceeding 20MB limit must be rejected");
        assertTrue(result.getErrorMessage().contains("exceeds the 20MB limit"));
    }
}
