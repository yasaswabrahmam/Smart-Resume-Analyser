package com.smartresume.controller;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.model.User;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model) {
        User user = userService.getCurrentUser().orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("roleList", JobRolesConfig.getAllRoleNames());
        model.addAttribute("title", "Profile & Career Settings — Smart Resume AI");
        return "profile/settings";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam(required = false, defaultValue = "STUDENT") String userType,
            @RequestParam(required = false, defaultValue = "Full Stack Developer") String targetRole,
            @RequestParam(required = false) String educationDegree,
            @RequestParam(required = false) String institution,
            @RequestParam(required = false) Integer graduationYear,
            @RequestParam(required = false, defaultValue = "0") Integer experienceYears,
            @RequestParam(required = false) String currentRole,
            @RequestParam(required = false) String currentSkills,
            @RequestParam(required = false, defaultValue = "Remote") String locationPreference,
            @RequestParam(required = false, defaultValue = "10") Integer weeklyLearningHours,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser().orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        userService.updateProfile(
                user, userType, targetRole, educationDegree, institution,
                graduationYear, experienceYears, currentRole, currentSkills,
                locationPreference, weeklyLearningHours);

        redirectAttributes.addFlashAttribute("successMessage", "Your career profile and pathway have been updated successfully!");
        return "redirect:/profile";
    }
}
