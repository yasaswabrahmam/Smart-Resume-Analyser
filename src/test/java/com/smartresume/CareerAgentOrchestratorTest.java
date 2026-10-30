package com.smartresume;

import com.smartresume.agent.*;
import com.smartresume.service.JobSearchService;
import com.smartresume.service.ResumeAnalyzerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CareerAgentOrchestratorTest {

    private CareerAgentOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        ResumeAnalyzerService analyzerService = new ResumeAnalyzerService();
        JobSearchService jobSearchService = Mockito.mock(JobSearchService.class);
        Mockito.when(jobSearchService.buildPortalSearchUrls(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyMap());

        ResumeAnalysisAgent analysisAgent = new ResumeAnalysisAgent(analyzerService);
        SkillGapAgent skillGapAgent = new SkillGapAgent();
        JobRecommendationAgent jobAgent = new JobRecommendationAgent(jobSearchService);
        CareerGuidanceAgent careerAgent = new CareerGuidanceAgent();
        ResumeGenerationAgent resumeGenAgent = new ResumeGenerationAgent();
        InterviewPreparationAgent interviewAgent = new InterviewPreparationAgent();

        orchestrator = new CareerAgentOrchestrator(
                analysisAgent,
                skillGapAgent,
                jobAgent,
                careerAgent,
                resumeGenAgent,
                interviewAgent
        );
    }

    @Test
    void testAgentPipelineExecution() {
        String sampleResume = "Yasaswa Brahmam\n" +
                "Email: yasaswa@example.com Phone: 987-654-3210 linkedin.com/in/yasaswa\n" +
                "PROFESSIONAL SUMMARY\n" +
                "Software Engineer with 3 years of experience in Java, Spring Boot, SQL, and microservices.\n" +
                "EXPERIENCE\n" +
                "Backend Engineer at Tech Corp (2022 - 2024)\n" +
                "• Developed distributed microservices using Java and Spring Boot\n" +
                "EDUCATION\n" +
                "B.Tech in Computer Science, 2022\n" +
                "SKILLS\n" +
                "Java, Spring Boot, SQL, Git, REST APIs\n";

        CareerAgentOrchestrator.OrchestratedCareerPlan plan = orchestrator.executeCareerPipeline(sampleResume, "Backend Developer");

        assertNotNull(plan);
        assertNotNull(plan.getAnalysisResult());
        assertTrue(plan.getAnalysisResult().getAtsScore() > 60.0);
        assertNotNull(plan.getSkillGaps());
        assertNotNull(plan.getCareerRoadmap());
        assertFalse(plan.getCareerRoadmap().isEmpty());
        assertNotNull(plan.getInterviewQuestions());
        assertFalse(plan.getInterviewQuestions().isEmpty());
        assertNotNull(plan.getImprovedBulletPoints());
    }
}
