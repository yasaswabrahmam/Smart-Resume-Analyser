package com.smartresume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartresume.model.Resume;
import com.smartresume.repository.ResumeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ExportService {

    private final ResumeRepository resumeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExportService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public byte[] exportToExcel() {
        List<Resume> resumes = resumeRepository.findAllByOrderByCreatedAtDesc();
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Resumes");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {"ID", "Name", "Email", "Phone", "Target Role", "Target Category", "Template", "ATS Score", "Created At"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Resume r : resumes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getId() != null ? r.getId() : 0);
                row.createCell(1).setCellValue(r.getName() != null ? r.getName() : "");
                row.createCell(2).setCellValue(r.getEmail() != null ? r.getEmail() : "");
                row.createCell(3).setCellValue(r.getPhone() != null ? r.getPhone() : "");
                row.createCell(4).setCellValue(r.getTargetRole() != null ? r.getTargetRole() : "");
                row.createCell(5).setCellValue(r.getTargetCategory() != null ? r.getTargetCategory() : "");
                row.createCell(6).setCellValue(r.getTemplate() != null ? r.getTemplate() : "");
                double ats = (r.getStandardAnalysis() != null) ? r.getStandardAnalysis().getAtsScore() : 0.0;
                row.createCell(7).setCellValue(ats);
                row.createCell(8).setCellValue(r.getCreatedAt() != null ? r.getCreatedAt().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("Error exporting to Excel: " + e.getMessage());
            return new byte[0];
        }
    }

    public byte[] exportToCsv() {
        List<Resume> resumes = resumeRepository.findAllByOrderByCreatedAtDesc();
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Name,Email,Phone,Target Role,Target Category,Template,ATS Score,Created At\n");

        for (Resume r : resumes) {
            double ats = (r.getStandardAnalysis() != null) ? r.getStandardAnalysis().getAtsScore() : 0.0;
            sb.append(escapeCsv(String.valueOf(r.getId()))).append(",")
              .append(escapeCsv(r.getName())).append(",")
              .append(escapeCsv(r.getEmail())).append(",")
              .append(escapeCsv(r.getPhone())).append(",")
              .append(escapeCsv(r.getTargetRole())).append(",")
              .append(escapeCsv(r.getTargetCategory())).append(",")
              .append(escapeCsv(r.getTemplate())).append(",")
              .append(ats).append(",")
              .append(escapeCsv(r.getCreatedAt() != null ? r.getCreatedAt().toString() : ""))
              .append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportToJson() {
        List<Resume> resumes = resumeRepository.findAllByOrderByCreatedAtDesc();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Resume r : resumes) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", r.getId());
            map.put("name", r.getName());
            map.put("email", r.getEmail());
            map.put("phone", r.getPhone());
            map.put("targetRole", r.getTargetRole());
            map.put("targetCategory", r.getTargetCategory());
            map.put("template", r.getTemplate());
            map.put("atsScore", (r.getStandardAnalysis() != null) ? r.getStandardAnalysis().getAtsScore() : 0.0);
            map.put("createdAt", r.getCreatedAt() != null ? r.getCreatedAt().toString() : null);
            list.add(map);
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(list);
        } catch (Exception e) {
            System.err.println("Error exporting to JSON: " + e.getMessage());
            return "[]".getBytes(StandardCharsets.UTF_8);
        }
    }

    public String escapeCsv(String value) {
        if (value == null) return "\"\"";
        String sanitized = value;
        // OWASP CSV Formula Injection defense
        if (sanitized.startsWith("=") || sanitized.startsWith("+") || sanitized.startsWith("-") ||
            sanitized.startsWith("@") || sanitized.startsWith("\t") || sanitized.startsWith("\r")) {
            sanitized = "'" + sanitized;
        }
        return "\"" + sanitized.replace("\"", "\"\"") + "\"";
    }
}
