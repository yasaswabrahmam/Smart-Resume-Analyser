package com.smartresume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartresume.config.JobRolesConfig;
import com.smartresume.dto.ResumeBuilderDto;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import com.smartresume.repository.ResumeRepository;
import com.smartresume.service.ResumeBuilderService;
import com.smartresume.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/builder")
public class ResumeBuilderController {

    private final ResumeBuilderService builderService;
    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeBuilderController(ResumeBuilderService builderService,
                                   ResumeRepository resumeRepository,
                                   UserService userService) {
        this.builderService = builderService;
        this.resumeRepository = resumeRepository;
        this.userService = userService;
    }

    @GetMapping
    public String showBuilderPage(Model model) {
        model.addAttribute("categories", JobRolesConfig.getCategories());
        model.addAttribute("jobRoles", JobRolesConfig.getJobRoles());
        model.addAttribute("resumeDto", new ResumeBuilderDto());
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "builder/builder";
    }

    @PostMapping("/download/docx")
    public ResponseEntity<byte[]> downloadDocx(@ModelAttribute ResumeBuilderDto resumeDto) {
        byte[] docxBytes = builderService.generateDocx(resumeDto);
        String safeName = sanitizeFilename(resumeDto.getFullName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeName + "_Resume.docx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(docxBytes);
    }

    @PostMapping("/download/pdf")
    public ResponseEntity<byte[]> downloadPdf(@ModelAttribute ResumeBuilderDto resumeDto) {
        byte[] pdfBytes = builderService.generatePdf(resumeDto);
        String safeName = sanitizeFilename(resumeDto.getFullName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeName + "_Resume.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    private String sanitizeFilename(String input) {
        if (input == null || input.isBlank()) {
            return "Resume";
        }
        String cleaned = input.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        if (cleaned.isBlank()) return "Resume";
        return cleaned.length() > 50 ? cleaned.substring(0, 50) : cleaned;
    }

    @PostMapping("/save")
    public String saveResume(@ModelAttribute ResumeBuilderDto resumeDto, Model model) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isPresent()) {
            try {
                User user = currentUserOpt.get();
                Resume resume = new Resume();
                resume.setUser(user);
                resume.setName(resumeDto.getFullName());
                resume.setEmail(resumeDto.getEmail());
                resume.setPhone(resumeDto.getPhone());
                resume.setLocation(resumeDto.getLocation());
                resume.setLinkedin(resumeDto.getLinkedin());
                resume.setPortfolio(resumeDto.getPortfolio());
                resume.setSummary(resumeDto.getSummary());
                resume.setTargetRole(resumeDto.getTargetRole());
                resume.setTargetCategory(resumeDto.getTargetCategory());
                resume.setTemplate(resumeDto.getTemplate());
                resume.setFileType("BUILDER");
                resume.setFileName(resumeDto.getFullName() + "_builder.docx");

                resume.setEducationJson(objectMapper.writeValueAsString(resumeDto.getEducations()));
                resume.setExperienceJson(objectMapper.writeValueAsString(resumeDto.getExperiences()));
                resume.setProjectsJson(objectMapper.writeValueAsString(resumeDto.getProjects()));

                resumeRepository.save(resume);
                return "redirect:/dashboard?saved=true";
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Error saving resume: " + e.getMessage());
            }
        }
        return "redirect:/dashboard";
    }
}
