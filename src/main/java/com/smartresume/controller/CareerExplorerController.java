package com.smartresume.controller;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.config.JobRolesConfig.RoleDefinition;
import com.smartresume.model.User;
import com.smartresume.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/explore")
public class CareerExplorerController {

    private final UserService userService;

    public CareerExplorerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String exploreRoles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            Model model) {

        User currentUser = userService.getCurrentUser().orElse(null);
        model.addAttribute("currentUser", currentUser);

        List<RoleDefinition> allRoles = JobRolesConfig.getAllRoles();

        // Apply filters
        List<RoleDefinition> filteredRoles = allRoles.stream()
                .filter(r -> {
                    if (search == null || search.isBlank()) return true;
                    String q = search.toLowerCase();
                    return r.getRoleName().toLowerCase().contains(q)
                            || r.getDescription().toLowerCase().contains(q)
                            || r.getCoreSkills().stream().anyMatch(s -> s.toLowerCase().contains(q))
                            || r.getAliases().stream().anyMatch(a -> a.toLowerCase().contains(q));
                })
                .filter(r -> {
                    if (category == null || category.isBlank() || "All".equalsIgnoreCase(category)) return true;
                    return r.getCategory().equalsIgnoreCase(category);
                })
                .filter(r -> {
                    if (level == null || level.isBlank() || "All".equalsIgnoreCase(level)) return true;
                    return r.getCareerLevel().toLowerCase().contains(level.toLowerCase());
                })
                .collect(Collectors.toList());

        model.addAttribute("roles", filteredRoles);
        model.addAttribute("categories", JobRolesConfig.getCategories());
        model.addAttribute("selectedCategory", category != null ? category : "All");
        model.addAttribute("selectedLevel", level != null ? level : "All");
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("totalCount", filteredRoles.size());
        model.addAttribute("canonicalSkillsCount", JobRolesConfig.CANONICAL_SKILLS.size());
        model.addAttribute("title", "Career Explorer — Role & Skill Intelligence");

        return "career/explorer";
    }

    /**
     * Ingest an unknown employer job description, extract canonical skills,
     * infer candidate role category, and dynamically register a custom role profile.
     */
    @PostMapping("/infer-role")
    public String inferRoleFromJobDescription(
            @RequestParam("targetTitle") String targetTitle,
            @RequestParam("jobDescription") String jobDescription,
            RedirectAttributes redirectAttributes) {

        RoleDefinition inferredRole = JobRolesConfig.inferRoleFromJobDescription(targetTitle, jobDescription);
        redirectAttributes.addFlashAttribute("inferredSuccess", true);
        redirectAttributes.addFlashAttribute("inferredRole", inferredRole);
        return "redirect:/explore?search=" + URLEncoder.encode(inferredRole.getRoleName(), StandardCharsets.UTF_8);
    }

    /**
     * REST endpoint to retrieve structured role profile metadata.
     */
    @GetMapping("/api/role/{roleId}")
    @ResponseBody
    public ResponseEntity<RoleDefinition> getRoleDetails(@PathVariable String roleId) {
        RoleDefinition role = JobRolesConfig.getRoleByName(roleId);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }
}
