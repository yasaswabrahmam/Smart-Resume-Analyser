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
    public String showInterviewPrep(@RequestParam(value = "role", required = false, defaultValue = "Full Stack Java Developer") String role,
                                    Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));

        InterviewPrepService.InterviewPrepKit prepKit = interviewPrepService.generatePrepKit(role);
        model.addAttribute("prepKit", prepKit);
        model.addAttribute("selectedRole", role);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());

        return "career/interview";
    }
}
