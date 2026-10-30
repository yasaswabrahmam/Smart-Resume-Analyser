package com.smartresume.service;

import com.smartresume.model.AdminLog;
import com.smartresume.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final FeedbackRepository feedbackRepository;
    private final AdminLogRepository adminLogRepository;

    public AdminService(UserRepository userRepository,
                        ResumeRepository resumeRepository,
                        ResumeAnalysisRepository resumeAnalysisRepository,
                        AIAnalysisRepository aiAnalysisRepository,
                        FeedbackRepository feedbackRepository,
                        AdminLogRepository adminLogRepository) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.feedbackRepository = feedbackRepository;
        this.adminLogRepository = adminLogRepository;
    }

    public Map<String, Object> getAdminOverviewStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalResumes = resumeRepository.count();
        long todaySubmissions = resumeRepository.countByCreatedAtAfter(LocalDate.now().atStartOfDay());
        Double avgAts = resumeAnalysisRepository.getAverageAtsScore();
        Long highScoring = resumeAnalysisRepository.countHighScoring();
        long totalFeedbacks = feedbackRepository.count();

        stats.put("totalUsers", totalUsers);
        stats.put("totalResumes", totalResumes);
        stats.put("todaySubmissions", todaySubmissions);
        stats.put("avgAtsScore", avgAts != null ? Math.round(avgAts * 10.0) / 10.0 : 0.0);
        stats.put("highScoringResumes", highScoring != null ? highScoring : 0L);
        stats.put("totalFeedbacks", totalFeedbacks);
        stats.put("storageSizeEstimate", String.format("%.2f MB", (totalResumes * 0.08) + 2.5));

        return stats;
    }

    public Map<String, Object> getAiStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalAi = aiAnalysisRepository.count();
        Double avgResume = aiAnalysisRepository.getAverageResumeScore();
        Double avgAts = aiAnalysisRepository.getAverageAtsScore();

        stats.put("totalAnalyses", totalAi);
        stats.put("totalAiAnalyses", totalAi);
        stats.put("avgMatchScore", avgResume != null ? Math.round(avgResume * 10.0) / 10.0 : 0.0);
        stats.put("avgAiResumeScore", avgResume != null ? Math.round(avgResume * 10.0) / 10.0 : 0.0);
        stats.put("avgAiAtsScore", avgAts != null ? Math.round(avgAts * 10.0) / 10.0 : 0.0);

        List<Object[]> models = aiAnalysisRepository.countByModelUsed();
        Map<String, Long> modelMap = new HashMap<>();
        for (Object[] row : models) {
            modelMap.put(String.valueOf(row[0]), (Long) row[1]);
        }
        stats.put("modelDistribution", modelMap);

        List<Object[]> roles = aiAnalysisRepository.countByTargetRole();
        Map<String, Long> roleMap = new HashMap<>();
        for (Object[] row : roles) {
            roleMap.put(String.valueOf(row[0]), (Long) row[1]);
        }
        stats.put("roleDistribution", roleMap);

        return stats;
    }

    public List<AdminLog> getAdminLogs() {
        return adminLogRepository.findAllByOrderByTimestampDesc();
    }

    @Transactional
    public void logAction(String adminEmail, String action, String details) {
        AdminLog log = new AdminLog(adminEmail, action, details);
        adminLogRepository.save(log);
    }

    @Transactional
    public void resetAiStatistics(String adminEmail) {
        aiAnalysisRepository.deleteAll();
        logAction(adminEmail, "RESET_AI_STATS", "Admin reset all AI analysis statistical logs.");
    }
}
