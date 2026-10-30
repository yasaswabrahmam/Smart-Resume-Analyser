package com.smartresume.controller;

import com.smartresume.config.JobRolesConfig;
import com.smartresume.config.JobRolesConfig.RoleDefinition;
import com.smartresume.model.User;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("title", "Career Explorer — Role & Skill Intelligence");

        return "career/explorer";
    }
}
