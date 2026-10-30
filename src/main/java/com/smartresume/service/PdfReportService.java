package com.smartresume.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.smartresume.dto.AIAnalysisResult;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    public byte[] generateAiAnalysisPdf(AIAnalysisResult analysis, String candidateName, String role) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // Colors
            Color primaryColor = new Color(41, 128, 185); // Blue
            Color darkSlate = new Color(44, 62, 80);
            Color lightGray = new Color(245, 247, 250);

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primaryColor);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, darkSlate);
            Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, primaryColor);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, darkSlate);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, darkSlate);
            Font scoreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, primaryColor);

            // Header Banner
            Paragraph title = new Paragraph("Smart AI Resume Analysis Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph meta = new Paragraph(
                    "Candidate: " + (candidateName != null ? candidateName : "Professional Candidate") +
                    "  |  Target Role: " + (role != null ? role : "Software Professional") +
                    "\nGenerated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) +
                    "  |  Engine: " + analysis.getModelUsed(),
                    subtitleFont
            );
            meta.setAlignment(Element.ALIGN_CENTER);
            meta.setSpacingAfter(20);
            document.add(meta);

            // Score Summary Table
            PdfPTable scoreTable = new PdfPTable(3);
            scoreTable.setWidthPercentage(100);
            scoreTable.setSpacingAfter(20);

            addScoreCell(scoreTable, "Resume Score", (int) analysis.getResumeScore() + "/100", scoreFont, normalFont, lightGray);
            addScoreCell(scoreTable, "ATS Score", (int) analysis.getAtsScore() + "/100", scoreFont, normalFont, lightGray);
            addScoreCell(scoreTable, "Job Match Score", (int) analysis.getJobMatchScore() + "/100", scoreFont, normalFont, lightGray);
            document.add(scoreTable);

            // Key Strengths
            addSectionHeader(document, "Key Strengths", sectionTitleFont);
            addBulletList(document, analysis.getStrengths(), normalFont);

            // Areas for Improvement
            addSectionHeader(document, "Areas for Improvement", sectionTitleFont);
            addBulletList(document, analysis.getWeaknesses(), normalFont);

            // Missing Skills
            addSectionHeader(document, "Identified Missing Skills", sectionTitleFont);
            addBulletList(document, analysis.getMissingSkills(), normalFont);

            // Recommendations
            addSectionHeader(document, "Improvement Recommendations & Next Steps", sectionTitleFont);
            addBulletList(document, analysis.getRecommendations(), normalFont);

            // Full Analysis Assessment Summary
            if (analysis.getFullAnalysis() != null && !analysis.getFullAnalysis().isBlank()) {
                addSectionHeader(document, "Detailed Assessment Summary", sectionTitleFont);
                String cleanSummary = analysis.getFullAnalysis().replaceAll("#+", "").trim();
                Paragraph p = new Paragraph(cleanSummary, normalFont);
                p.setSpacingAfter(10);
                document.add(p);
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("Error generating AI analysis PDF: " + e.getMessage());
            return new byte[0];
        }
    }

    private void addScoreCell(PdfPTable table, String label, String score, Font scoreFont, Font labelFont, Color bgColor) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bgColor);
        cell.setPadding(12);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(new Color(220, 224, 230));

        Paragraph scoreP = new Paragraph(score, scoreFont);
        scoreP.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(scoreP);

        Paragraph labelP = new Paragraph(label, labelFont);
        labelP.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(labelP);

        table.addCell(cell);
    }

    private void addSectionHeader(Document document, String title, Font font) throws DocumentException {
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(12);
        p.setSpacingAfter(6);
        document.add(p);
    }

    private void addBulletList(Document document, List<String> items, Font font) throws DocumentException {
        if (items == null || items.isEmpty()) {
            Paragraph p = new Paragraph("None identified.", font);
            p.setSpacingAfter(8);
            document.add(p);
            return;
        }

        com.lowagie.text.List list = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED, 10);
        list.setListSymbol("• ");
        for (String item : items) {
            ListItem listItem = new ListItem(item, font);
            listItem.setSpacingAfter(3);
            list.add(listItem);
        }
        document.add(list);
    }
}
