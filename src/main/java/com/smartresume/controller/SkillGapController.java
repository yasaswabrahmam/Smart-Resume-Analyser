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
    public String showSkillGapPage(@RequestParam(value = "role", required = false, defaultValue = "Full Stack Developer") String role,
                                   @RequestParam(value = "resumeId", required = false) Long resumeId,
                                   Model model) {
        Optional<User> currentUser = userService.getCurrentUser();
        currentUser.ifPresent(u -> model.addAttribute("currentUser", u));

        String resolvedRole = (role != null && !role.isBlank()) ? role.trim() : "Full Stack Developer";

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
        SkillGapService.SkillGapReport report = skillGapService.evaluateGaps(resumeText, resolvedRole, "");
        model.addAttribute("report", report);
        model.addAttribute("selectedRole", resolvedRole);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());

        return "career/skill-gap";
    }

    @GetMapping("/role/{role}")
    public String showSkillGapByPath(@PathVariable("role") String role, Model model) {
        return showSkillGapPage(role, null, model);
    }

    @PostMapping("/analyze")
    public String analyzeCustomGap(@RequestParam(value = "role", defaultValue = "Full Stack Developer") String role,
                                   @RequestParam(value = "customText", required = false) String customText,
                                   Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        String resolvedRole = (role != null && !role.isBlank()) ? role.trim() : "Full Stack Developer";
        SkillGapService.SkillGapReport report = skillGapService.evaluateGaps(customText, resolvedRole, "");
        model.addAttribute("report", report);
        model.addAttribute("selectedRole", resolvedRole);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());
        return "career/skill-gap";
    }
}
