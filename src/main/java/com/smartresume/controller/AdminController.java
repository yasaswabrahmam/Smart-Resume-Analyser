package com.smartresume.controller;

import com.smartresume.agent.ModelGovernanceAgent;
import com.smartresume.agent.ModelTrainingOrchestratorAgent;
import com.smartresume.model.AdminLog;
import com.smartresume.model.Feedback;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import com.smartresume.repository.FeedbackRepository;
import com.smartresume.repository.ResumeRepository;
import com.smartresume.service.AdminService;
import com.smartresume.service.ExportService;
import com.smartresume.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ExportService exportService;
    private final ResumeRepository resumeRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final ModelGovernanceAgent modelGovernanceAgent;
    private final ModelTrainingOrchestratorAgent modelTrainingOrchestratorAgent;

    public AdminController(AdminService adminService,
                           ExportService exportService,
                           ResumeRepository resumeRepository,
                           FeedbackRepository feedbackRepository,
                           UserService userService,
                           ModelGovernanceAgent modelGovernanceAgent,
                           ModelTrainingOrchestratorAgent modelTrainingOrchestratorAgent) {
        this.adminService = adminService;
        this.exportService = exportService;
        this.resumeRepository = resumeRepository;
        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
        this.modelGovernanceAgent = modelGovernanceAgent;
        this.modelTrainingOrchestratorAgent = modelTrainingOrchestratorAgent;
    }

    @GetMapping
    public String showAdminDashboard(Model model) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        currentUserOpt.ifPresent(u -> model.addAttribute("currentUser", u));

        Map<String, Object> overview = adminService.getAdminOverviewStats();
        model.addAttribute("overview", overview);

        Map<String, Object> aiStats = adminService.getAiStatistics();
        model.addAttribute("aiStats", aiStats);

        List<Resume> allResumes = resumeRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("allResumes", allResumes);

        List<Feedback> allFeedbacks = feedbackRepository.findAllByOrderByTimestampDesc();
        model.addAttribute("allFeedbacks", allFeedbacks);
        model.addAttribute("feedbackList", allFeedbacks);

        List<AdminLog> adminLogs = adminService.getAdminLogs();
        model.addAttribute("adminLogs", adminLogs);
        model.addAttribute("logs", adminLogs);

        model.addAttribute("governedModels", modelGovernanceAgent.getRegisteredModels());
        model.addAttribute("datasetCatalog", modelTrainingOrchestratorAgent.getDatasetCatalog());
        model.addAttribute("orchestratedModels", modelTrainingOrchestratorAgent.getModelRegistry());

        return "dashboard/admin";
    }

    @PostMapping("/models/train")
    public String triggerTrainingPipeline(@RequestParam String datasetId,
                                         @RequestParam String taskType,
                                         @RequestParam String architecture) {
        modelTrainingOrchestratorAgent.runTrainingPipeline(datasetId, taskType, architecture);
        return "redirect:/admin?trained=true#models";
    }

    @PostMapping("/models/promote")
    public String promoteModel(@RequestParam String modelId) {
        String adminEmail = userService.getCurrentUser().map(User::getEmail).orElse("admin");
        modelTrainingOrchestratorAgent.promoteCandidateToProduction(modelId, adminEmail);
        return "redirect:/admin?promoted=true#models";
    }

    @PostMapping("/models/rollback")
    public String rollbackModel(@RequestParam String modelId, @RequestParam String reason) {
        modelTrainingOrchestratorAgent.rollbackModel(modelId, reason);
        return "redirect:/admin?rolledBack=true#models";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() {
        byte[] bytes = exportService.exportToExcel();
        adminService.logAction(userService.getCurrentUser().map(User::getEmail).orElse("admin"), "EXPORT_EXCEL", "Exported resume data to Excel");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume_data_export.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] bytes = exportService.exportToCsv();
        adminService.logAction(userService.getCurrentUser().map(User::getEmail).orElse("admin"), "EXPORT_CSV", "Exported resume data to CSV");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume_data_export.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }

    @GetMapping("/export/json")
    public ResponseEntity<byte[]> exportJson() {
        byte[] bytes = exportService.exportToJson();
        adminService.logAction(userService.getCurrentUser().map(User::getEmail).orElse("admin"), "EXPORT_JSON", "Exported resume data to JSON");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume_data_export.json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(bytes);
    }

    @PostMapping("/reset-ai-stats")
    public String resetAiStats() {
        String adminEmail = userService.getCurrentUser().map(User::getEmail).orElse("admin");
        adminService.resetAiStatistics(adminEmail);
        return "redirect:/admin?reset=true";
    }
}
