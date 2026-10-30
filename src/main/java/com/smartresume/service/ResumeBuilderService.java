package com.smartresume.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.smartresume.dto.ResumeBuilderDto;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hardened Resume Builder Service
 * Provides complete, null-safe generation for all 4 templates
 * (Modern, Professional, Minimal, Creative) across both DOCX and PDF.
 */
@Service
public class ResumeBuilderService {

    public byte[] generateDocx(ResumeBuilderDto data) {
        if (data == null) data = new ResumeBuilderDto();

        try (XWPFDocument doc = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            String template = (data.getTemplate() != null) ? data.getTemplate().toLowerCase().trim() : "modern";

            switch (template) {
                case "professional":
                    buildProfessionalDocx(doc, data);
                    break;
                case "minimal":
                    buildMinimalDocx(doc, data);
                    break;
                case "creative":
                    buildCreativeDocx(doc, data);
                    break;
                case "modern":
                default:
                    buildModernDocx(doc, data);
                    break;
            }

            doc.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("Error generating DOCX resume: " + e.getMessage());
            return new byte[0];
        }
    }

    public byte[] generatePdf(ResumeBuilderDto data) {
        if (data == null) data = new ResumeBuilderDto();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, out);
            document.open();

            String template = (data.getTemplate() != null) ? data.getTemplate().toLowerCase().trim() : "modern";

            switch (template) {
                case "professional":
                    buildProfessionalPdf(document, data);
                    break;
                case "minimal":
                    buildMinimalPdf(document, data);
                    break;
                case "creative":
                    buildCreativePdf(document, data);
                    break;
                case "modern":
                default:
                    buildModernPdf(document, data);
                    break;
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("Error generating PDF resume: " + e.getMessage());
            return new byte[0];
        }
    }

    // -------------------------------------------------------------
    // DOCX BUILDERS
    // -------------------------------------------------------------

    private void buildModernDocx(XWPFDocument doc, ResumeBuilderDto data) {
        String blueHex = "2980B9";

        // Name
        XWPFParagraph nameP = doc.createParagraph();
        nameP.setAlignment(ParagraphAlignment.CENTER);
        nameP.setSpacingAfter(60);
        XWPFRun nameRun = nameP.createRun();
        nameRun.setText(safeName(data.getFullName(), "YOUR NAME").toUpperCase());
        nameRun.setBold(true);
        nameRun.setFontSize(22);
        nameRun.setColor(blueHex);
        nameRun.setFontFamily("Arial");

        // Title
        if (isNotBlank(data.getTitle())) {
            XWPFParagraph titleP = doc.createParagraph();
            titleP.setAlignment(ParagraphAlignment.CENTER);
            titleP.setSpacingAfter(80);
            XWPFRun titleRun = titleP.createRun();
            titleRun.setText(data.getTitle().trim());
            titleRun.setFontSize(12);
            titleRun.setColor(blueHex);
            titleRun.setFontFamily("Arial");
        }

        // Contact info
        String contact = joinNonBlank(" | ", data.getEmail(), data.getPhone(), data.getLocation(), data.getLinkedin(), data.getPortfolio());
        if (!contact.isBlank()) {
            XWPFParagraph contactP = doc.createParagraph();
            contactP.setAlignment(ParagraphAlignment.CENTER);
            contactP.setSpacingAfter(180);
            XWPFRun contactRun = contactP.createRun();
            contactRun.setText(contact);
            contactRun.setFontSize(9.5);
            contactRun.setFontFamily("Arial");
        }

        // Summary
        if (isNotBlank(data.getSummary())) {
            addDocxSectionHeader(doc, "PROFESSIONAL SUMMARY", blueHex, "Arial");
            XWPFParagraph p = doc.createParagraph();
            p.setSpacingAfter(140);
            XWPFRun r = p.createRun();
            r.setText(data.getSummary().trim());
            r.setFontSize(10);
            r.setFontFamily("Arial");
        }

        // Experience
        if (data.getExperiences() != null && !data.getExperiences().isEmpty()) {
            addDocxSectionHeader(doc, "WORK EXPERIENCE", blueHex, "Arial");
            for (ResumeBuilderDto.ExperienceItem exp : data.getExperiences()) {
                addDocxExperienceItem(doc, exp, blueHex, "Arial");
            }
        }

        // Projects
        if (data.getProjects() != null && !data.getProjects().isEmpty()) {
            addDocxSectionHeader(doc, "PROJECTS", blueHex, "Arial");
            for (ResumeBuilderDto.ProjectItem proj : data.getProjects()) {
                addDocxProjectItem(doc, proj, blueHex, "Arial");
            }
        }

        // Education
        if (data.getEducations() != null && !data.getEducations().isEmpty()) {
            addDocxSectionHeader(doc, "EDUCATION", blueHex, "Arial");
            for (ResumeBuilderDto.EducationItem edu : data.getEducations()) {
                addDocxEducationItem(doc, edu, blueHex, "Arial");
            }
        }

        // Skills
        addDocxSkillsSection(doc, data, blueHex, "Arial");
    }

    private void buildProfessionalDocx(XWPFDocument doc, ResumeBuilderDto data) {
        String proHex = "0078D7";

        // Name left-aligned
        XWPFParagraph nameP = doc.createParagraph();
        nameP.setAlignment(ParagraphAlignment.LEFT);
        nameP.setSpacingAfter(40);
        XWPFRun nameRun = nameP.createRun();
        nameRun.setText(safeName(data.getFullName(), "YOUR NAME"));
        nameRun.setBold(true);
        nameRun.setFontSize(22);
        nameRun.setColor("111111");
        nameRun.setFontFamily("Calibri");

        // Title
        if (isNotBlank(data.getTitle())) {
            XWPFParagraph titleP = doc.createParagraph();
            titleP.setSpacingAfter(60);
            XWPFRun tr = titleP.createRun();
            tr.setText(data.getTitle().trim());
            tr.setFontSize(12);
            tr.setColor(proHex);
            tr.setFontFamily("Calibri");
        }

        // Contact info
        String contact = joinNonBlank(" | ", data.getEmail(), data.getPhone(), data.getLocation(), data.getLinkedin(), data.getPortfolio());
        if (!contact.isBlank()) {
            XWPFParagraph contactP = doc.createParagraph();
            contactP.setSpacingAfter(140);
            XWPFRun contactRun = contactP.createRun();
            contactRun.setText(contact);
            contactRun.setFontSize(10);
            contactRun.setFontFamily("Calibri");
        }

        // Summary
        if (isNotBlank(data.getSummary())) {
            addDocxSectionHeader(doc, "PROFESSIONAL SUMMARY", proHex, "Calibri");
            XWPFParagraph p = doc.createParagraph();
            p.setSpacingAfter(140);
            XWPFRun r = p.createRun();
            r.setText(data.getSummary().trim());
            r.setFontSize(10);
            r.setFontFamily("Calibri");
        }

        // Experience
        if (data.getExperiences() != null && !data.getExperiences().isEmpty()) {
            addDocxSectionHeader(doc, "PROFESSIONAL EXPERIENCE", proHex, "Calibri");
            for (ResumeBuilderDto.ExperienceItem exp : data.getExperiences()) {
                addDocxExperienceItem(doc, exp, proHex, "Calibri");
            }
        }

        // Projects
        if (data.getProjects() != null && !data.getProjects().isEmpty()) {
            addDocxSectionHeader(doc, "KEY PROJECTS", proHex, "Calibri");
            for (ResumeBuilderDto.ProjectItem proj : data.getProjects()) {
                addDocxProjectItem(doc, proj, proHex, "Calibri");
            }
        }

        // Education
        if (data.getEducations() != null && !data.getEducations().isEmpty()) {
            addDocxSectionHeader(doc, "EDUCATION & CREDENTIALS", proHex, "Calibri");
            for (ResumeBuilderDto.EducationItem edu : data.getEducations()) {
                addDocxEducationItem(doc, edu, proHex, "Calibri");
            }
        }

        // Skills
        addDocxSkillsSection(doc, data, proHex, "Calibri");
    }

    private void buildMinimalDocx(XWPFDocument doc, ResumeBuilderDto data) {
        String darkGray = "212121";

        XWPFParagraph nameP = doc.createParagraph();
        nameP.setAlignment(ParagraphAlignment.LEFT);
        nameP.setSpacingAfter(40);
        XWPFRun nameRun = nameP.createRun();
        nameRun.setText(safeName(data.getFullName(), "YOUR NAME"));
        nameRun.setBold(true);
        nameRun.setFontSize(24);
        nameRun.setColor(darkGray);

        if (isNotBlank(data.getTitle())) {
            XWPFParagraph tp = doc.createParagraph();
            tp.setSpacingAfter(60);
            XWPFRun tr = tp.createRun();
            tr.setText(data.getTitle().trim());
            tr.setFontSize(11);
            tr.setColor("555555");
        }

        String contact = joinNonBlank(" • ", data.getEmail(), data.getPhone(), data.getLocation(), data.getLinkedin());
        if (!contact.isBlank()) {
            XWPFParagraph contactP = doc.createParagraph();
            contactP.setSpacingAfter(160);
            XWPFRun contactRun = contactP.createRun();
            contactRun.setText(contact);
            contactRun.setFontSize(9);
            contactRun.setColor("666666");
        }

        if (isNotBlank(data.getSummary())) {
            addDocxSectionHeader(doc, "SUMMARY", darkGray, "Arial");
            XWPFParagraph p = doc.createParagraph();
            p.setSpacingAfter(140);
            p.createRun().setText(data.getSummary().trim());
        }

        if (data.getExperiences() != null && !data.getExperiences().isEmpty()) {
            addDocxSectionHeader(doc, "EXPERIENCE", darkGray, "Arial");
            for (ResumeBuilderDto.ExperienceItem exp : data.getExperiences()) {
                addDocxExperienceItem(doc, exp, darkGray, "Arial");
            }
        }

        if (data.getProjects() != null && !data.getProjects().isEmpty()) {
            addDocxSectionHeader(doc, "PROJECTS", darkGray, "Arial");
            for (ResumeBuilderDto.ProjectItem proj : data.getProjects()) {
                addDocxProjectItem(doc, proj, darkGray, "Arial");
            }
        }

        if (data.getEducations() != null && !data.getEducations().isEmpty()) {
            addDocxSectionHeader(doc, "EDUCATION", darkGray, "Arial");
            for (ResumeBuilderDto.EducationItem edu : data.getEducations()) {
                addDocxEducationItem(doc, edu, darkGray, "Arial");
            }
        }

        addDocxSkillsSection(doc, data, darkGray, "Arial");
    }

    private void buildCreativeDocx(XWPFDocument doc, ResumeBuilderDto data) {
        String purpleHex = "9B59B6";

        XWPFParagraph nameP = doc.createParagraph();
        nameP.setAlignment(ParagraphAlignment.CENTER);
        nameP.setSpacingAfter(60);
        XWPFRun nameRun = nameP.createRun();
        nameRun.setText("✨ " + safeName(data.getFullName(), "YOUR NAME") + " ✨");
        nameRun.setBold(true);
        nameRun.setFontSize(22);
        nameRun.setColor(purpleHex);

        if (isNotBlank(data.getTitle())) {
            XWPFParagraph titleP = doc.createParagraph();
            titleP.setAlignment(ParagraphAlignment.CENTER);
            titleP.setSpacingAfter(80);
            XWPFRun tr = titleP.createRun();
            tr.setText("💫 " + data.getTitle().trim());
            tr.setColor(purpleHex);
        }

        String contact = joinNonBlank(" | ", data.getEmail(), data.getPhone(), data.getLocation(), data.getLinkedin());
        if (!contact.isBlank()) {
            XWPFParagraph contactP = doc.createParagraph();
            contactP.setAlignment(ParagraphAlignment.CENTER);
            contactP.setSpacingAfter(180);
            XWPFRun cr = contactP.createRun();
            cr.setText(contact);
            cr.setFontSize(9.5);
            cr.setColor(purpleHex);
        }

        if (isNotBlank(data.getSummary())) {
            addDocxSectionHeader(doc, "👨‍💼 PROFESSIONAL SUMMARY", purpleHex, "Arial");
            XWPFParagraph p = doc.createParagraph();
            p.setSpacingAfter(140);
            p.createRun().setText(data.getSummary().trim());
        }

        if (data.getExperiences() != null && !data.getExperiences().isEmpty()) {
            addDocxSectionHeader(doc, "💼 EXPERIENCE", purpleHex, "Arial");
            for (ResumeBuilderDto.ExperienceItem exp : data.getExperiences()) {
                addDocxExperienceItem(doc, exp, purpleHex, "Arial");
            }
        }

        if (data.getProjects() != null && !data.getProjects().isEmpty()) {
            addDocxSectionHeader(doc, "🚀 PROJECTS", purpleHex, "Arial");
            for (ResumeBuilderDto.ProjectItem proj : data.getProjects()) {
                addDocxProjectItem(doc, proj, purpleHex, "Arial");
            }
        }

        if (data.getEducations() != null && !data.getEducations().isEmpty()) {
            addDocxSectionHeader(doc, "🎓 EDUCATION", purpleHex, "Arial");
            for (ResumeBuilderDto.EducationItem edu : data.getEducations()) {
                addDocxEducationItem(doc, edu, purpleHex, "Arial");
            }
        }

        addDocxSkillsSection(doc, data, purpleHex, "Arial");
    }

    private void addDocxSectionHeader(XWPFDocument doc, String title, String colorHex, String fontFamily) {
        XWPFParagraph sectionP = doc.createParagraph();
        sectionP.setSpacingBefore(160);
        sectionP.setSpacingAfter(60);
        XWPFRun r = sectionP.createRun();
        r.setText(title);
        r.setBold(true);
        r.setFontSize(12.5);
        r.setColor(colorHex);
        r.setFontFamily(fontFamily);
    }

    private void addDocxExperienceItem(XWPFDocument doc, ResumeBuilderDto.ExperienceItem exp, String colorHex, String fontFamily) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(30);

        String roleAndCompany = joinNonBlank(" at ", exp.getPosition(), exp.getCompany());
        if (roleAndCompany.isBlank()) roleAndCompany = "Position / Company";

        XWPFRun title = p.createRun();
        title.setText(roleAndCompany);
        title.setBold(true);
        title.setFontSize(10.5);
        title.setFontFamily(fontFamily);

        String dates = joinNonBlank(" - ", exp.getStartDate(), exp.getEndDate());
        if (!dates.isBlank()) {
            XWPFRun datesRun = p.createRun();
            datesRun.setText(" (" + dates + ")");
            datesRun.setColor(colorHex);
            datesRun.setFontSize(9.5);
            datesRun.setFontFamily(fontFamily);
        }

        if (isNotBlank(exp.getDescription())) {
            XWPFParagraph descP = doc.createParagraph();
            descP.setSpacingAfter(30);
            XWPFRun descR = descP.createRun();
            descR.setText(exp.getDescription().trim());
            descR.setFontSize(10);
            descR.setFontFamily(fontFamily);
        }

        if (isNotBlank(exp.getResponsibilities())) {
            for (String bullet : exp.getResponsibilities().split("\n")) {
                if (!bullet.isBlank()) {
                    XWPFParagraph bP = doc.createParagraph();
                    bP.setIndentationLeft(360);
                    bP.setSpacingAfter(20);
                    XWPFRun bR = bP.createRun();
                    bR.setText("• " + bullet.trim());
                    bR.setFontSize(9.5);
                    bR.setFontFamily(fontFamily);
                }
            }
        }
    }

    private void addDocxProjectItem(XWPFDocument doc, ResumeBuilderDto.ProjectItem proj, String colorHex, String fontFamily) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(30);

        XWPFRun title = p.createRun();
        title.setText(safeName(proj.getName(), "Project"));
        title.setBold(true);
        title.setFontSize(10.5);
        title.setFontFamily(fontFamily);

        if (isNotBlank(proj.getTechnologies())) {
            XWPFRun tech = p.createRun();
            tech.setText(" | " + proj.getTechnologies().trim());
            tech.setColor(colorHex);
            tech.setFontSize(9.5);
            tech.setFontFamily(fontFamily);
        }

        if (isNotBlank(proj.getDescription())) {
            XWPFParagraph descP = doc.createParagraph();
            descP.setSpacingAfter(30);
            XWPFRun descR = descP.createRun();
            descR.setText(proj.getDescription().trim());
            descR.setFontSize(10);
            descR.setFontFamily(fontFamily);
        }
    }

    private void addDocxEducationItem(XWPFDocument doc, ResumeBuilderDto.EducationItem edu, String colorHex, String fontFamily) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingAfter(30);

        XWPFRun school = p.createRun();
        school.setText(safeName(edu.getSchool(), "Institution"));
        school.setBold(true);
        school.setFontSize(10.5);
        school.setFontFamily(fontFamily);

        String degreeField = joinNonBlank(" in ", edu.getDegree(), edu.getField());
        StringBuilder details = new StringBuilder();
        if (!degreeField.isBlank()) details.append(" - ").append(degreeField);
        if (isNotBlank(edu.getGraduationDate())) details.append(" (").append(edu.getGraduationDate()).append(")");
        if (isNotBlank(edu.getGpa())) details.append(" | GPA: ").append(edu.getGpa());

        if (details.length() > 0) {
            XWPFRun degRun = p.createRun();
            degRun.setText(details.toString());
            degRun.setFontSize(9.5);
            degRun.setFontFamily(fontFamily);
        }
    }

    private void addDocxSkillsSection(XWPFDocument doc, ResumeBuilderDto data, String colorHex, String fontFamily) {
        boolean hasTech = data.getTechnicalSkills() != null && !data.getTechnicalSkills().isEmpty();
        boolean hasSoft = data.getSoftSkills() != null && !data.getSoftSkills().isEmpty();

        if (hasTech || hasSoft) {
            addDocxSectionHeader(doc, "SKILLS", colorHex, fontFamily);

            if (hasTech) {
                XWPFParagraph p = doc.createParagraph();
                p.setSpacingAfter(30);
                XWPFRun bold = p.createRun();
                bold.setText("Technical Skills: ");
                bold.setBold(true);
                bold.setFontFamily(fontFamily);
                p.createRun().setText(String.join(", ", data.getTechnicalSkills()));
            }

            if (hasSoft) {
                XWPFParagraph p = doc.createParagraph();
                p.setSpacingAfter(30);
                XWPFRun bold = p.createRun();
                bold.setText("Soft Skills: ");
                bold.setBold(true);
                bold.setFontFamily(fontFamily);
                p.createRun().setText(String.join(", ", data.getSoftSkills()));
            }
        }
    }

    // -------------------------------------------------------------
    // PDF BUILDERS (OpenPDF)
    // -------------------------------------------------------------

    private void buildModernPdf(com.lowagie.text.Document doc, ResumeBuilderDto data) throws DocumentException {
        Color primary = new Color(41, 128, 185);
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primary);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, primary);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, primary);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

        renderPdfDocument(doc, data, nameFont, titleFont, headerFont, normalFont, boldFont, true);
    }

    private void buildProfessionalPdf(com.lowagie.text.Document doc, ResumeBuilderDto data) throws DocumentException {
        Color proColor = new Color(0, 120, 215);
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, proColor);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, proColor);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

        renderPdfDocument(doc, data, nameFont, titleFont, headerFont, normalFont, boldFont, false);
    }

    private void buildMinimalPdf(com.lowagie.text.Document doc, ResumeBuilderDto data) throws DocumentException {
        Color darkGray = new Color(33, 33, 33);
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 10.5f, Color.DARK_GRAY);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, darkGray);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9.5f, Color.DARK_GRAY);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, Color.BLACK);

        renderPdfDocument(doc, data, nameFont, titleFont, headerFont, normalFont, boldFont, false);
    }

    private void buildCreativePdf(com.lowagie.text.Document doc, ResumeBuilderDto data) throws DocumentException {
        Color purple = new Color(155, 89, 182);
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, purple);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, purple);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, purple);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

        renderPdfDocument(doc, data, nameFont, titleFont, headerFont, normalFont, boldFont, true);
    }

    private void renderPdfDocument(com.lowagie.text.Document doc,
                                   ResumeBuilderDto data,
                                   Font nameFont,
                                   Font titleFont,
                                   Font headerFont,
                                   Font normalFont,
                                   Font boldFont,
                                   boolean centerHeader) throws DocumentException {

        int align = centerHeader ? Element.ALIGN_CENTER : Element.ALIGN_LEFT;

        // Name
        Paragraph nameP = new Paragraph(safeName(data.getFullName(), "YOUR NAME").toUpperCase(), nameFont);
        nameP.setAlignment(align);
        doc.add(nameP);

        // Title
        if (isNotBlank(data.getTitle())) {
            Paragraph titleP = new Paragraph(data.getTitle().trim(), titleFont);
            titleP.setAlignment(align);
            doc.add(titleP);
        }

        // Contact info
        String contact = joinNonBlank(" | ", data.getEmail(), data.getPhone(), data.getLocation(), data.getLinkedin(), data.getPortfolio());
        if (!contact.isBlank()) {
            Paragraph contactP = new Paragraph(contact, normalFont);
            contactP.setAlignment(align);
            contactP.setSpacingAfter(12);
            doc.add(contactP);
        }

        // Summary
        if (isNotBlank(data.getSummary())) {
            addPdfSectionHeader(doc, "PROFESSIONAL SUMMARY", headerFont);
            Paragraph sumP = new Paragraph(data.getSummary().trim(), normalFont);
            sumP.setSpacingAfter(6);
            doc.add(sumP);
        }

        // Experience
        if (data.getExperiences() != null && !data.getExperiences().isEmpty()) {
            addPdfSectionHeader(doc, "EXPERIENCE", headerFont);
            for (ResumeBuilderDto.ExperienceItem exp : data.getExperiences()) {
                String title = joinNonBlank(" at ", exp.getPosition(), exp.getCompany());
                String dates = joinNonBlank(" - ", exp.getStartDate(), exp.getEndDate());
                String line = title + (!dates.isBlank() ? " (" + dates + ")" : "");

                Paragraph p = new Paragraph(line, boldFont);
                doc.add(p);

                if (isNotBlank(exp.getDescription())) {
                    Paragraph desc = new Paragraph(exp.getDescription().trim(), normalFont);
                    doc.add(desc);
                }

                if (isNotBlank(exp.getResponsibilities())) {
                    for (String bullet : exp.getResponsibilities().split("\n")) {
                        if (!bullet.isBlank()) {
                            Paragraph bP = new Paragraph("  • " + bullet.trim(), normalFont);
                            doc.add(bP);
                        }
                    }
                }
                Paragraph gap = new Paragraph(" ", normalFont);
                gap.setSpacingAfter(3);
                doc.add(gap);
            }
        }

        // Projects
        if (data.getProjects() != null && !data.getProjects().isEmpty()) {
            addPdfSectionHeader(doc, "PROJECTS", headerFont);
            for (ResumeBuilderDto.ProjectItem proj : data.getProjects()) {
                String title = safeName(proj.getName(), "Project");
                if (isNotBlank(proj.getTechnologies())) title += " | " + proj.getTechnologies().trim();

                Paragraph p = new Paragraph(title, boldFont);
                doc.add(p);

                if (isNotBlank(proj.getDescription())) {
                    Paragraph desc = new Paragraph(proj.getDescription().trim(), normalFont);
                    desc.setSpacingAfter(4);
                    doc.add(desc);
                }
            }
        }

        // Education
        if (data.getEducations() != null && !data.getEducations().isEmpty()) {
            addPdfSectionHeader(doc, "EDUCATION", headerFont);
            for (ResumeBuilderDto.EducationItem edu : data.getEducations()) {
                String school = safeName(edu.getSchool(), "Institution");
                String deg = joinNonBlank(" in ", edu.getDegree(), edu.getField());
                String line = school + (!deg.isBlank() ? " — " + deg : "");
                if (isNotBlank(edu.getGraduationDate())) line += " (" + edu.getGraduationDate() + ")";
                if (isNotBlank(edu.getGpa())) line += " | GPA: " + edu.getGpa();

                Paragraph p = new Paragraph(line, normalFont);
                doc.add(p);
            }
        }

        // Skills
        boolean hasTech = data.getTechnicalSkills() != null && !data.getTechnicalSkills().isEmpty();
        boolean hasSoft = data.getSoftSkills() != null && !data.getSoftSkills().isEmpty();
        if (hasTech || hasSoft) {
            addPdfSectionHeader(doc, "SKILLS", headerFont);
            if (hasTech) {
                Paragraph p = new Paragraph("Technical Skills: " + String.join(", ", data.getTechnicalSkills()), normalFont);
                doc.add(p);
            }
            if (hasSoft) {
                Paragraph p = new Paragraph("Soft Skills: " + String.join(", ", data.getSoftSkills()), normalFont);
                doc.add(p);
            }
        }
    }

    private void addPdfSectionHeader(com.lowagie.text.Document doc, String title, Font font) throws DocumentException {
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(8);
        p.setSpacingAfter(4);
        doc.add(p);
    }

    // -------------------------------------------------------------
    // UTILITY METHODS
    // -------------------------------------------------------------

    private static boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static String safeName(String name, String def) {
        return (name != null && !name.isBlank()) ? name.trim() : def;
    }

    private static String joinNonBlank(String delimiter, String... items) {
        if (items == null) return "";
        return Arrays.stream(items)
                .filter(ResumeBuilderService::isNotBlank)
                .map(String::trim)
                .collect(Collectors.joining(delimiter));
    }
}
