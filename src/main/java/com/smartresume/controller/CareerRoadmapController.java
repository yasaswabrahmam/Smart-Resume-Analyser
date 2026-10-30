package com.smartresume.controller;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.service.CareerRoadmapService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/roadmap")
public class CareerRoadmapController {

    private final CareerRoadmapService careerRoadmapService;
    private final UserService userService;

    public CareerRoadmapController(CareerRoadmapService careerRoadmapService,
                                   UserService userService) {
        this.careerRoadmapService = careerRoadmapService;
        this.userService = userService;
    }

    @GetMapping
    public String showRoadmap(@RequestParam(value = "role", required = false, defaultValue = "Full Stack Java Developer") String role,
                              @RequestParam(value = "missingSkills", required = false) List<String> missingSkills,
                              Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));

        CareerRoadmapService.CareerRoadmapPlan plan = careerRoadmapService.generateRoadmap(
                role,
                missingSkills != null ? missingSkills : Collections.emptyList()
        );

        model.addAttribute("plan", plan);
        model.addAttribute("selectedRole", role);
        model.addAttribute("allRoles", JobRolesConfig.getAllRoleNames());

        return "career/roadmap";
    }
}
