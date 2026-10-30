package com.smartresume.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeParserService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeParserService.class);

    public static class ParseResult {
        private final String text;
        private final boolean scannedPdf;
        private final String errorMessage;

        public ParseResult(String text, boolean scannedPdf, String errorMessage) {
            this.text = text != null ? text : "";
            this.scannedPdf = scannedPdf;
            this.errorMessage = errorMessage;
        }

        public String getText() { return text; }
        public boolean isScannedPdf() { return scannedPdf; }
        public String getErrorMessage() { return errorMessage; }
        public boolean isSuccess() { return errorMessage == null && !text.isBlank(); }
    }

    public String extractText(MultipartFile file) {
        ParseResult result = parseFile(file);
        return result.getText();
    }

    public ParseResult parseFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ParseResult("", false, "Uploaded file is empty.");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return new ParseResult("", false, "Filename is missing.");
        }
        String lowerName = fileName.toLowerCase();

        try (InputStream is = file.getInputStream()) {
            if (lowerName.endsWith(".pdf")) {
                return extractTextFromPdfWithMeta(is);
            } else if (lowerName.endsWith(".docx")) {
                return extractTextFromDocxWithMeta(is);
            } else {
                return new ParseResult("", false, "Unsupported file format. Please upload PDF or DOCX.");
            }
        } catch (Exception e) {
            logger.error("Error reading file stream for {}: {}", fileName, e.getMessage());
            return new ParseResult("", false, "Error reading file: " + e.getMessage());
        }
    }

    public String extractTextFromPdf(InputStream inputStream) {
        return extractTextFromPdfWithMeta(inputStream).getText();
    }

    public ParseResult extractTextFromPdfWithMeta(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            int pageCount = document.getNumberOfPages();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(document).trim();

            if (pageCount > 0 && text.length() < 20) {
                logger.warn("PDF contains {} page(s) but less than 20 characters of extractable text. Likely scanned image-only document.", pageCount);
                return new ParseResult(text, true, "Scanned or image-only PDF detected. Please upload a text-searchable PDF or Word DOCX resume.");
            }

            return new ParseResult(text, false, null);
        } catch (Exception e) {
            logger.error("PDFBox extraction failed: {}", e.getMessage());
            return new ParseResult("", false, "Failed to parse PDF document. It may be password-protected or corrupted.");
        }
    }

    public String extractTextFromDocx(InputStream inputStream) {
        return extractTextFromDocxWithMeta(inputStream).getText();
    }

    public ParseResult extractTextFromDocxWithMeta(InputStream inputStream) {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            StringBuilder sb = new StringBuilder();

            // 1. Extract standard paragraphs
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text != null && !text.isBlank()) {
                    sb.append(text).append("\n");
                }
            }

            // 2. Extract text from tables (crucial for multi-column resumes)
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    StringBuilder rowText = new StringBuilder();
                    for (XWPFTableCell cell : row.getTableCells()) {
                        String cellText = cell.getText();
                        if (cellText != null && !cellText.isBlank()) {
                            if (rowText.length() > 0) rowText.append(" | ");
                            rowText.append(cellText.trim());
                        }
                    }
                    if (rowText.length() > 0) {
                        sb.append(rowText).append("\n");
                    }
                }
            }

            String fullText = sb.toString().trim();
            if (fullText.length() < 20) {
                return new ParseResult(fullText, false, "DOCX document contains minimal or unreadable text content.");
            }

            return new ParseResult(fullText, false, null);
        } catch (Exception e) {
            logger.error("Apache POI DOCX extraction failed: {}", e.getMessage());
            return new ParseResult("", false, "Failed to parse DOCX document. It may be corrupted or in an unsupported format.");
        }
    }

    public List<String> extractCommonSkills(String text) {
        List<String> found = new ArrayList<>();
        if (text == null || text.isBlank()) return found;

        String[] skillKeywords = {
                "python", "java", "javascript", "typescript", "html", "css", "sql", "react",
                "angular", "vue", "node", "express", "django", "flask", "spring", "docker",
                "kubernetes", "aws", "azure", "gcp", "git", "jenkins", "jira", "linux",
                "c++", "c#", "mongodb", "postgresql", "mysql", "redis", "kafka", "rest", "graphql",
                "ci/cd", "microservices", "agile", "scrum", "junit", "selenium", "terraform"
        };

        String textLower = text.toLowerCase();
        for (String skill : skillKeywords) {
            if (textLower.contains(skill.toLowerCase())) {
                found.add(skill);
            }
        }
        return found;
    }
}
