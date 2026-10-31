package com.smartresume.controller;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.service.InterviewPrepService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/interview")
public class InterviewPrepController {

    private final InterviewPrepService interviewPrepService;
    private final UserService userService;

    public InterviewPrepController(InterviewPrepService interviewPrepService,
                                   UserService userService) {
        this.interviewPrepService = interviewPrepService;
        this.userService = userService;
    }

    @GetMapping
    public String showInterviewPrep(@RequestParam(value = "role", required = false, defaultValue = "Full Stack Developer") String role,
                                    Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));

        String resolvedRole = (role != null && !role.isBlank()) ? role.trim() : "Full Stack Developer";

        InterviewPrepService.InterviewPrepKit prepKit = interviewPrepService.generatePrepKit(resolvedRole);
        model.addAttribute("prepKit", prepKit);
        model.addAttribute("selectedRole", resolvedRole);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());
        model.addAttribute("title", "Interview Preparation — " + resolvedRole);

        return "career/interview";
    }

    @GetMapping("/role/{role}")
    public String showInterviewPrepByPath(@PathVariable("role") String role, Model model) {
        return showInterviewPrep(role, model);
    }
}
