package com.smartresume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartresume.config.JobRolesConfig;
import com.smartresume.dto.AIAnalysisResult;
import com.smartresume.model.AIAnalysis;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import com.smartresume.repository.AIAnalysisRepository;
import com.smartresume.repository.ResumeRepository;
import com.smartresume.service.AIResumeAnalyzerService;
import com.smartresume.service.FileValidationService;
import com.smartresume.service.PdfReportService;
import com.smartresume.service.ResumeParserService;
import com.smartresume.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ai-analyzer")
public class AIResumeAnalyzerController {

    private final AIResumeAnalyzerService aiService;
    private final ResumeParserService parserService;
    private final PdfReportService pdfReportService;
    private final ResumeRepository resumeRepository;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final UserService userService;
    private final FileValidationService fileValidationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIResumeAnalyzerController(AIResumeAnalyzerService aiService,
                                      ResumeParserService parserService,
                                      PdfReportService pdfReportService,
                                      ResumeRepository resumeRepository,
                                      AIAnalysisRepository aiAnalysisRepository,
                                      UserService userService,
                                      FileValidationService fileValidationService) {
        this.aiService = aiService;
        this.parserService = parserService;
        this.pdfReportService = pdfReportService;
        this.resumeRepository = resumeRepository;
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.userService = userService;
        this.fileValidationService = fileValidationService;
    }

    @GetMapping
    public String showAiAnalyzerPage(Model model) {
        model.addAttribute("categories", JobRolesConfig.getCategories());
        model.addAttribute("jobRoles", JobRolesConfig.getJobRoles());
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "analyzer/ai";
    }

    @PostMapping("/analyze")
    public String analyzeWithAi(@RequestParam(value = "resumeFile", required = false) MultipartFile file,
                                @RequestParam(value = "resumeText", required = false) String directText,
                                @RequestParam(value = "targetCategory", required = false) String targetCategory,
                                @RequestParam(value = "targetRole", required = false) String targetRole,
                                @RequestParam(value = "jobDescription", required = false) String jobDescription,
                                @RequestParam(value = "customApiKey", required = false) String customApiKey,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        String textToAnalyze = "";
        String fileName = "direct_input.txt";

        if (file != null && !file.isEmpty()) {
            FileValidationService.ValidationResult validationResult = fileValidationService.validateResumeFile(file);
            if (!validationResult.isValid()) {
                redirectAttributes.addFlashAttribute("errorMessage", validationResult.getErrorMessage());
                return "redirect:/ai-analyzer";
            }
            textToAnalyze = parserService.extractText(file);
            fileName = validationResult.getSanitizedFileName();
        } else if (directText != null && !directText.isBlank()) {
            textToAnalyze = directText.trim();
        }

        if (textToAnalyze.isBlank() || textToAnalyze.length() < 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please provide a valid resume file or paste readable resume text.");
            return "redirect:/ai-analyzer";
        }

        AIAnalysisResult result = aiService.analyze(textToAnalyze, targetRole, targetCategory, jobDescription, customApiKey);

        // Link candidate name if available
        Optional<User> currentUserOpt = userService.getCurrentUser();
        String candidateName = currentUserOpt.map(User::getFullName).orElse("Professional Candidate");
        result.setCandidateName(candidateName);

        // Persist AI analysis in MySQL
        Long savedAiId = null;
        if (currentUserOpt.isPresent()) {
            try {
                User user = currentUserOpt.get();

                Resume resume = new Resume();
                resume.setUser(user);
                resume.setName(candidateName);
                resume.setEmail(user.getEmail());
                resume.setTargetCategory(targetCategory);
                resume.setTargetRole(targetRole);
                resume.setRawText(textToAnalyze);
                resume.setFileName(fileName);
                resume.setFileType("AI_ANALYZED");
                Resume savedResume = resumeRepository.save(resume);

                AIAnalysis aiAnalysis = new AIAnalysis();
                aiAnalysis.setUser(user);
                aiAnalysis.setResume(savedResume);
                aiAnalysis.setTargetCategory(targetCategory);
                aiAnalysis.setTargetRole(targetRole);
                aiAnalysis.setJobDescription(jobDescription);
                aiAnalysis.setModelUsed(result.getModelUsed());
                aiAnalysis.setResumeScore(result.getResumeScore());
                aiAnalysis.setAtsScore(result.getAtsScore());
                aiAnalysis.setJobMatchScore(result.getJobMatchScore());
                aiAnalysis.setStrengthsJson(objectMapper.writeValueAsString(result.getStrengths()));
                aiAnalysis.setWeaknessesJson(objectMapper.writeValueAsString(result.getWeaknesses()));
                aiAnalysis.setMissingSkillsJson(objectMapper.writeValueAsString(result.getMissingSkills()));
                aiAnalysis.setRecommendationsJson(objectMapper.writeValueAsString(result.getRecommendations()));
                aiAnalysis.setFullAnalysis(result.getFullAnalysis());

                AIAnalysis saved = aiAnalysisRepository.save(aiAnalysis);
                savedAiId = saved.getId();
            } catch (Exception e) {
                System.err.println("Error persisting AI analysis: " + e.getMessage());
            }
        }

        model.addAttribute("result", result);
        model.addAttribute("targetCategory", targetCategory);
        model.addAttribute("targetRole", targetRole);
        model.addAttribute("savedAiId", savedAiId);
        model.addAttribute("categories", JobRolesConfig.getCategories());
        model.addAttribute("jobRoles", JobRolesConfig.getJobRoles());
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));

        return "analyzer/ai";
    }

    @GetMapping("/download-pdf/{id}")
    public ResponseEntity<byte[]> downloadPdfReport(@PathVariable("id") Long id) {
        Optional<AIAnalysis> analysisOpt = aiAnalysisRepository.findById(id);
        if (analysisOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AIAnalysis analysis = analysisOpt.get();

        // Enforce user authorization / IDOR check
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = currentUserOpt.get();
        boolean isAdmin = "ROLE_ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isOwner = analysis.getUser() != null && analysis.getUser().getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).build();
        }

        AIAnalysisResult dto = new AIAnalysisResult();
        dto.setResumeScore(analysis.getResumeScore());
        dto.setAtsScore(analysis.getAtsScore());
        dto.setJobMatchScore(analysis.getJobMatchScore());
        dto.setModelUsed(analysis.getModelUsed());
        dto.setFullAnalysis(analysis.getFullAnalysis());

        try {
            dto.setStrengths(objectMapper.readValue(analysis.getStrengthsJson(), List.class));
            dto.setWeaknesses(objectMapper.readValue(analysis.getWeaknessesJson(), List.class));
            dto.setMissingSkills(objectMapper.readValue(analysis.getMissingSkillsJson(), List.class));
            dto.setRecommendations(objectMapper.readValue(analysis.getRecommendationsJson(), List.class));
        } catch (Exception ignored) {}

        String candidate = analysis.getUser() != null ? analysis.getUser().getFullName() : "Candidate";
        byte[] pdfBytes = pdfReportService.generateAiAnalysisPdf(dto, candidate, analysis.getTargetRole());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"AI_Resume_Analysis_Report.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
