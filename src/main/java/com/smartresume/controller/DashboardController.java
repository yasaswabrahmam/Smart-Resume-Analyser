package com.smartresume.controller;

import com.smartresume.dto.DashboardMetricsDto;
import com.smartresume.model.AIAnalysis;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import com.smartresume.repository.AIAnalysisRepository;
import com.smartresume.service.ApplicationTrackerService;
import com.smartresume.service.DashboardService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final ApplicationTrackerService applicationTrackerService;

    public DashboardController(DashboardService dashboardService,
                               UserService userService,
                               AIAnalysisRepository aiAnalysisRepository,
                               ApplicationTrackerService applicationTrackerService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.applicationTrackerService = applicationTrackerService;
    }

    @GetMapping("/dashboard")
    public String showUserDashboard(Model model) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (currentUserOpt.isEmpty()) {
            return "redirect:/login";
        }

        User user = currentUserOpt.get();
        model.addAttribute("currentUser", user);

        List<Resume> userResumes = dashboardService.getUserResumes(user);
        model.addAttribute("resumes", userResumes);

        List<AIAnalysis> userAiAnalyses = aiAnalysisRepository.findByUserOrderByCreatedAtDesc(user);
        model.addAttribute("aiAnalyses", userAiAnalyses);

        ApplicationTrackerService.PipelineMetrics trackerMetrics = applicationTrackerService.getMetricsForUser(user);
        model.addAttribute("trackerMetrics", trackerMetrics);

        return "dashboard/user";
    }

    @GetMapping("/analytics")
    public String showAnalyticsPage(Model model) {
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        DashboardMetricsDto metrics = dashboardService.getAnalyticsMetrics();
        model.addAttribute("metrics", metrics);
        return "dashboard/analytics";
    }
}
