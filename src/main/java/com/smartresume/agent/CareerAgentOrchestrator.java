package com.smartresume.agent;

import com.smartresume.dto.ResumeAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Central Career Agent Orchestrator
 * Coordinates specialized autonomous career intelligence agents,
 * manages agent execution pipelines, enforces permissions, and aggregates outputs.
 */
@Service
public class CareerAgentOrchestrator {

    private static final Logger logger = LoggerFactory.getLogger(CareerAgentOrchestrator.class);

    private final ResumeAnalysisAgent resumeAnalysisAgent;
    private final SkillGapAgent skillGapAgent;
    private final JobRecommendationAgent jobRecommendationAgent;
    private final CareerGuidanceAgent careerGuidanceAgent;
    private final ResumeGenerationAgent resumeGenerationAgent;
    private final InterviewPreparationAgent interviewPreparationAgent;

    public CareerAgentOrchestrator(ResumeAnalysisAgent resumeAnalysisAgent,
                                   SkillGapAgent skillGapAgent,
                                   JobRecommendationAgent jobRecommendationAgent,
                                   CareerGuidanceAgent careerGuidanceAgent,
                                   ResumeGenerationAgent resumeGenerationAgent,
                                   InterviewPreparationAgent interviewPreparationAgent) {
        this.resumeAnalysisAgent = resumeAnalysisAgent;
        this.skillGapAgent = skillGapAgent;
        this.jobRecommendationAgent = jobRecommendationAgent;
        this.careerGuidanceAgent = careerGuidanceAgent;
        this.resumeGenerationAgent = resumeGenerationAgent;
        this.interviewPreparationAgent = interviewPreparationAgent;
    }

    public static class OrchestratedCareerPlan {
        private final ResumeAnalysisResult analysisResult;
        private final List<String> skillGaps;
        private final List<String> careerRoadmap;
        private final List<String> interviewQuestions;
        private final List<String> improvedBulletPoints;

        public OrchestratedCareerPlan(ResumeAnalysisResult analysisResult,
                                     List<String> skillGaps,
                                     List<String> careerRoadmap,
                                     List<String> interviewQuestions,
                                     List<String> improvedBulletPoints) {
            this.analysisResult = analysisResult;
            this.skillGaps = skillGaps != null ? skillGaps : Collections.emptyList();
            this.careerRoadmap = careerRoadmap != null ? careerRoadmap : Collections.emptyList();
            this.interviewQuestions = interviewQuestions != null ? interviewQuestions : Collections.emptyList();
            this.improvedBulletPoints = improvedBulletPoints != null ? improvedBulletPoints : Collections.emptyList();
        }

        public ResumeAnalysisResult getAnalysisResult() { return analysisResult; }
        public List<String> getSkillGaps() { return skillGaps; }
        public List<String> getCareerRoadmap() { return careerRoadmap; }
        public List<String> getInterviewQuestions() { return interviewQuestions; }
        public List<String> getImprovedBulletPoints() { return improvedBulletPoints; }
    }

    /**
     * Executes the comprehensive multi-agent evaluation pipeline.
     */
    public OrchestratedCareerPlan executeCareerPipeline(String resumeText, String targetRole) {
        logger.info("Initiating Multi-Agent Career Pipeline for target role: {}", targetRole);

        // 1. Resume Analysis Agent: structural & ATS evaluation
        ResumeAnalysisResult analysisResult = resumeAnalysisAgent.evaluate(resumeText, targetRole);

        // 2. Skill Gap Agent: differential competency diagnosis
        List<String> gaps = skillGapAgent.diagnoseGaps(analysisResult.getFoundSkills(), targetRole);

        // 3. Career Guidance Agent: phased roadmap formulation
        List<String> roadmap = careerGuidanceAgent.generateRoadmap(gaps, targetRole);

        // 4. Interview Preparation Agent: tailored technical/behavioral questions
        List<String> questions = interviewPreparationAgent.generateQuestions(gaps, targetRole);

        // 5. Resume Generation Agent: enhanced action-verb bullet points
        List<String> improvedBullets = resumeGenerationAgent.suggestImprovements(analysisResult.getExperience());

        logger.info("Multi-Agent Career Pipeline completed successfully for role: {}", targetRole);
        return new OrchestratedCareerPlan(analysisResult, gaps, roadmap, questions, improvedBullets);
    }
}
