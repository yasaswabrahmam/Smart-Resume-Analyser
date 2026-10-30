package com.smartresume.controller;

import com.smartresume.model.JobMatch;
import com.smartresume.model.User;
import com.smartresume.service.ApplicationTrackerService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tracker")
public class ApplicationTrackerController {

    private final ApplicationTrackerService applicationTrackerService;
    private final UserService userService;

    public ApplicationTrackerController(ApplicationTrackerService applicationTrackerService,
                                        UserService userService) {
        this.applicationTrackerService = applicationTrackerService;
        this.userService = userService;
    }

    @GetMapping
    public String showTracker(Model model) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/login";
        }

        User user = currentUserOpt.get();
        model.addAttribute("currentUser", user);

        List<JobMatch> applications = applicationTrackerService.getApplicationsForUser(user);
        ApplicationTrackerService.PipelineMetrics metrics = applicationTrackerService.getMetricsForUser(user);

        model.addAttribute("applications", applications);
        model.addAttribute("metrics", metrics);

        return "career/tracker";
    }

    @PostMapping("/save")
    public String saveJob(@RequestParam("jobTitle") String jobTitle,
                          @RequestParam("company") String company,
                          @RequestParam(value = "location", required = false, defaultValue = "Remote / India") String location,
                          @RequestParam(value = "matchScore", required = false, defaultValue = "85.0") double matchScore,
                          @RequestParam(value = "url", required = false) String url,
                          @RequestParam(value = "source", required = false, defaultValue = "LinkedIn") String source,
                          @RequestParam(value = "notes", required = false) String notes,
                          RedirectAttributes redirectAttributes) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/login";
        }

        applicationTrackerService.saveJob(currentUserOpt.get(), jobTitle, company, location, matchScore, url, source, notes);
        redirectAttributes.addFlashAttribute("successMessage", "Job saved successfully to your Application Tracker!");
        return "redirect:/tracker";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("id") Long id,
                               @RequestParam("status") String status,
                               @RequestParam(value = "notes", required = false) String notes,
                               RedirectAttributes redirectAttributes) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/login";
        }

        boolean updated = applicationTrackerService.updateStatus(id, currentUserOpt.get(), status, notes);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Application status updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to update application record.");
        }
        return "redirect:/tracker";
    }

    @PostMapping("/delete")
    public String deleteJob(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/login";
        }

        boolean deleted = applicationTrackerService.deleteJob(id, currentUserOpt.get());
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Application removed from tracker.");
        }
        return "redirect:/tracker";
    }
}
