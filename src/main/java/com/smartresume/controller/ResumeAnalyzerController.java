package com.smartresume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartresume.config.JobRolesConfig;
import com.smartresume.dto.ResumeAnalysisResult;
import com.smartresume.model.Resume;
import com.smartresume.model.ResumeAnalysis;
import com.smartresume.model.User;
import com.smartresume.repository.ResumeAnalysisRepository;
import com.smartresume.repository.ResumeRepository;
import com.smartresume.service.FileValidationService;
import com.smartresume.service.ResumeAnalyzerService;
import com.smartresume.service.ResumeParserService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/analyzer")
public class ResumeAnalyzerController {

    private final ResumeParserService parserService;
    private final ResumeAnalyzerService analyzerService;
    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final UserService userService;
    private final FileValidationService fileValidationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeAnalyzerController(ResumeParserService parserService,
                                    ResumeAnalyzerService analyzerService,
                                    ResumeRepository resumeRepository,
                                    ResumeAnalysisRepository resumeAnalysisRepository,
                                    UserService userService,
                                    FileValidationService fileValidationService) {
        this.parserService = parserService;
        this.analyzerService = analyzerService;
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.userService = userService;
        this.fileValidationService = fileValidationService;
    }

    @GetMapping
    public String showAnalyzerPage(Model model) {
        model.addAttribute("categories", JobRolesConfig.getCategories());
        model.addAttribute("jobRoles", JobRolesConfig.getJobRoles());
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "analyzer/standard";
    }

    @PostMapping("/analyze")
    public String analyzeResume(@RequestParam("resumeFile") MultipartFile file,
                                @RequestParam(value = "targetCategory", required = false) String targetCategory,
                                @RequestParam(value = "targetRole", required = false) String targetRole,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        FileValidationService.ValidationResult validationResult = fileValidationService.validateResumeFile(file);
        if (!validationResult.isValid()) {
            redirectAttributes.addFlashAttribute("errorMessage", validationResult.getErrorMessage());
            return "redirect:/analyzer";
        }

        String text = parserService.extractText(file);
        if (text == null || text.trim().length() < 20) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not extract readable text from the file. If this is a scanned or image-based PDF, please upload a text-based PDF or Word document.");
            return "redirect:/analyzer";
        }

        ResumeAnalysisResult result = analyzerService.analyze(text, targetRole);

        // Persist to MySQL database linked to current authenticated user
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isPresent()) {
            try {
                User user = currentUserOpt.get();
                Resume resume = new Resume();
                resume.setUser(user);
                resume.setName(result.getName() != null ? result.getName() : user.getFullName());
                resume.setEmail(result.getEmail() != null ? result.getEmail() : user.getEmail());
                resume.setPhone(result.getPhone());
                resume.setLinkedin(result.getLinkedin());
                resume.setGithub(result.getGithub());
                resume.setTargetCategory(targetCategory);
                resume.setTargetRole(targetRole);
                resume.setRawText(text);
                resume.setFileName(validationResult.getSanitizedFileName());
                resume.setFileType(validationResult.getDetectedType());
                resume.setSummary(result.getSummary());

                Resume savedResume = resumeRepository.save(resume);

                ResumeAnalysis analysis = new ResumeAnalysis();
                analysis.setResume(savedResume);
                analysis.setAtsScore(result.getAtsScore());
                analysis.setKeywordMatchScore(result.getKeywordMatchScore());
                analysis.setFormatScore(result.getFormatScore());
                analysis.setSectionScore(result.getSectionScore());
                analysis.setContactScore(result.getContactScore());
                analysis.setSummaryScore(result.getSummaryScore());
                analysis.setExperienceScore(result.getExperienceScore());
                analysis.setEducationScore(result.getEducationScore());
                analysis.setDocumentType(result.getDocumentType());
                analysis.setFoundSkillsJson(objectMapper.writeValueAsString(result.getFoundSkills()));
                analysis.setMissingSkillsJson(objectMapper.writeValueAsString(result.getMissingSkills()));
                analysis.setRecommendationsJson(objectMapper.writeValueAsString(result.getSuggestions()));

                resumeAnalysisRepository.save(analysis);
                model.addAttribute("savedResumeId", savedResume.getId());
            } catch (Exception e) {
                System.err.println("Error saving resume analysis to database: " + e.getMessage());
            }
        }

        model.addAttribute("result", result);
        model.addAttribute("targetCategory", targetCategory);
        model.addAttribute("targetRole", targetRole);
        model.addAttribute("fileName", validationResult.getSanitizedFileName());
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));

        return "analyzer/results";
    }

    @GetMapping("/view/{id}")
    public String viewSavedAnalysis(@PathVariable("id") Long resumeId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
        if (resumeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resume analysis record not found.");
            return "redirect:/dashboard";
        }
        Resume resume = resumeOpt.get();

        // Enforce user authorization / IDOR prevention
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/login";
        }
        User currentUser = currentUserOpt.get();
        boolean isAdmin = "ROLE_ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isOwner = resume.getUser() != null && resume.getUser().getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied: You do not have permission to view this analysis.");
            return "redirect:/dashboard";
        }

        ResumeAnalysisResult result = analyzerService.analyze(resume.getRawText(), resume.getTargetRole());

        model.addAttribute("result", result);
        model.addAttribute("targetCategory", resume.getTargetCategory());
        model.addAttribute("targetRole", resume.getTargetRole());
        model.addAttribute("fileName", resume.getFileName());
        model.addAttribute("savedResumeId", resume.getId());
        model.addAttribute("currentUser", currentUser);

        return "analyzer/results";
    }
}
