package com.smartresume.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

/**
 * Layered File Upload Security Validator
 * Validates file presence, size, filename safety, extension, MIME type,
 * and binary magic bytes (file signature) to prevent malicious uploads.
 */
@Service
public class FileValidationService {

    public static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20 MB
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "docx");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/x-pdf",
            "application/octet-stream" // occasionally reported by browsers for binary formats
    );

    // Magic bytes (file signatures)
    private static final byte[] PDF_MAGIC = new byte[]{0x25, 0x50, 0x44, 0x46}; // %PDF
    private static final byte[] ZIP_MAGIC = new byte[]{0x50, 0x4B, 0x03, 0x04}; // PK.. (DOCX is a zip)

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final String sanitizedFileName;
        private final String detectedType; // "PDF" or "DOCX"

        public ValidationResult(boolean valid, String errorMessage, String sanitizedFileName, String detectedType) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.sanitizedFileName = sanitizedFileName;
            this.detectedType = detectedType;
        }

        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        public String getSanitizedFileName() { return sanitizedFileName; }
        public String getDetectedType() { return detectedType; }

        public static ValidationResult success(String sanitizedFileName, String detectedType) {
            return new ValidationResult(true, null, sanitizedFileName, detectedType);
        }

        public static ValidationResult failure(String errorMessage) {
            return new ValidationResult(false, errorMessage, null, null);
        }
    }

    public ValidationResult validateResumeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ValidationResult.failure("Please select a valid resume file to upload.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ValidationResult.failure("File size exceeds the 20MB limit. Please upload a smaller resume document.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            return ValidationResult.failure("Uploaded file has an invalid or missing filename.");
        }

        // Path traversal and dangerous character check
        if (originalName.contains("..") || originalName.contains("/") || originalName.contains("\\") || originalName.contains("\0")) {
            return ValidationResult.failure("Filename contains illegal characters or path traversal sequences.");
        }

        // Sanitize filename
        String sanitizedName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (sanitizedName.length() > 150) {
            sanitizedName = sanitizedName.substring(sanitizedName.length() - 150);
        }

        // Extension check
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == originalName.length() - 1) {
            return ValidationResult.failure("Unsupported file format. Only PDF (.pdf) and Microsoft Word (.docx) files are supported.");
        }
        String ext = originalName.substring(dotIndex + 1).toLowerCase().trim();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return ValidationResult.failure("Unsupported file format '." + ext + "'. Only PDF (.pdf) and DOCX (.docx) documents are permitted.");
        }

        // MIME type check
        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank()) {
            String lowerMime = contentType.toLowerCase().trim();
            if (!ALLOWED_MIME_TYPES.contains(lowerMime)) {
                return ValidationResult.failure("Invalid file MIME type ('" + lowerMime + "'). Expected PDF or Word DOCX document.");
            }
        }

        // Magic bytes / File signature check
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[4];
            int read = is.read(header);
            if (read < 4) {
                return ValidationResult.failure("File header is corrupted or incomplete.");
            }

            if ("pdf".equals(ext)) {
                if (!Arrays.equals(header, PDF_MAGIC)) {
                    return ValidationResult.failure("File signature verification failed: file is not a valid PDF document (magic bytes mismatch).");
                }
                return ValidationResult.success(sanitizedName, "PDF");
            } else if ("docx".equals(ext)) {
                if (!Arrays.equals(header, ZIP_MAGIC)) {
                    return ValidationResult.failure("File signature verification failed: file is not a valid DOCX document (magic bytes mismatch).");
                }
                return ValidationResult.success(sanitizedName, "DOCX");
            }
        } catch (IOException e) {
            return ValidationResult.failure("Failed to read file stream for security validation: " + e.getMessage());
        }

        return ValidationResult.failure("File validation could not be completed.");
    }
}
