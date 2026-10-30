package com.smartresume.agent;

import com.smartresume.dto.ResumeAnalysisResult;
import com.smartresume.service.ResumeAnalyzerService;
import org.springframework.stereotype.Component;

/**
 * Resume Analysis Agent
 * Specializes in structural evaluation, formatting standards, action-verb density,
 * and deterministic ATS scoring execution.
 */
@Component
public class ResumeAnalysisAgent {

    private final ResumeAnalyzerService analyzerService;

    public ResumeAnalysisAgent(ResumeAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    public ResumeAnalysisResult evaluate(String resumeText, String targetRole) {
        if (resumeText == null || resumeText.isBlank()) {
            ResumeAnalysisResult emptyResult = new ResumeAnalysisResult();
            emptyResult.setAtsScore(0.0);
            return emptyResult;
        }
        return analyzerService.analyze(resumeText, targetRole != null ? targetRole : "Software Engineer");
    }
}
