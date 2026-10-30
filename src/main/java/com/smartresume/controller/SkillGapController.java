package com.smartresume.controller;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import com.smartresume.repository.ResumeRepository;
import com.smartresume.service.SkillGapService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/skill-gap")
public class SkillGapController {

    private final SkillGapService skillGapService;
    private final ResumeRepository resumeRepository;
    private final UserService userService;

    public SkillGapController(SkillGapService skillGapService,
                              ResumeRepository resumeRepository,
                              UserService userService) {
        this.skillGapService = skillGapService;
        this.resumeRepository = resumeRepository;
        this.userService = userService;
    }

    @GetMapping
    public String showSkillGapPage(@RequestParam(value = "role", required = false, defaultValue = "Full Stack Java Developer") String role,
                                   @RequestParam(value = "resumeId", required = false) Long resumeId,
                                   Model model) {
        Optional<User> currentUser = userService.getCurrentUser();
        currentUser.ifPresent(u -> model.addAttribute("currentUser", u));

        String resumeText = "";
        if (resumeId != null) {
            Optional<Resume> resumeOpt = resumeRepository.findById(resumeId);
            if (resumeOpt.isPresent()) {
                resumeText = resumeOpt.get().getRawText();
                model.addAttribute("selectedResume", resumeOpt.get());
            }
        } else if (currentUser.isPresent()) {
            List<Resume> userResumes = resumeRepository.findByUserOrderByCreatedAtDesc(currentUser.get());
            if (!userResumes.isEmpty()) {
                resumeText = userResumes.get(0).getRawText();
                model.addAttribute("selectedResume", userResumes.get(0));
            }
        }

        // Evaluate skill gaps
        SkillGapService.SkillGapReport report = skillGapService.evaluateGaps(resumeText, role, "");
        model.addAttribute("report", report);
        model.addAttribute("selectedRole", role);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());

        return "career/skill-gap";
    }

    @PostMapping("/analyze")
    public String analyzeCustomGap(@RequestParam("role") String role,
                                   @RequestParam(value = "customText", required = false) String customText,
                                   Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        SkillGapService.SkillGapReport report = skillGapService.evaluateGaps(customText, role, "");
        model.addAttribute("report", report);
        model.addAttribute("selectedRole", role);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());
        return "career/skill-gap";
    }
}
