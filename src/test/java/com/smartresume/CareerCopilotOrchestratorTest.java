package com.smartresume;

import com.smartresume.agent.CareerCopilotOrchestrator;
import com.smartresume.agent.CareerCopilotOrchestrator.CopilotResponse;
import com.smartresume.model.User;
import com.smartresume.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Career Copilot Orchestrator & Tool Registry Tests")
public class CareerCopilotOrchestratorTest {

    private UserService userService;
    private ResumeAnalyzerService resumeAnalyzerService;
    private SkillGapService skillGapService;
    private CareerRoadmapService careerRoadmapService;
    private JobSearchService jobSearchService;
    private InterviewPrepService interviewPrepService;
    private CareerKnowledgeRagService ragService;
    private ApplicationTrackerService applicationTrackerService;

    private CareerCopilotOrchestrator copilot;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        resumeAnalyzerService = Mockito.mock(ResumeAnalyzerService.class);
        skillGapService = Mockito.mock(SkillGapService.class);
        careerRoadmapService = Mockito.mock(CareerRoadmapService.class);
        jobSearchService = Mockito.mock(JobSearchService.class);
        interviewPrepService = Mockito.mock(InterviewPrepService.class);
        ragService = new CareerKnowledgeRagService();
        applicationTrackerService = Mockito.mock(ApplicationTrackerService.class);

        copilot = new CareerCopilotOrchestrator(
                userService, resumeAnalyzerService, skillGapService,
                careerRoadmapService, jobSearchService, interviewPrepService,
                ragService, applicationTrackerService
        );
    }

    @Test
    @DisplayName("Verify Tool Registry contains exactly 17 allowlisted tools (12 read, 5 write)")
    void testToolRegistryCount() {
        assertEquals(17, copilot.getToolRegistry().size());
        assertTrue(copilot.getToolRegistry().containsKey("getCareerProfile"));
        assertTrue(copilot.getToolRegistry().containsKey("retrieveCareerKnowledge"));
        assertTrue(copilot.getToolRegistry().containsKey("createLearningRoadmap"));
        assertEquals(CareerCopilotOrchestrator.ToolType.USER_CONFIRMED_WRITE,
                copilot.getToolRegistry().get("createLearningRoadmap").getType());
    }

    @Test
    @DisplayName("Verify safe read query executes within step budget and cites verified sources")
    void testReadQueryExecution() {
        User student = new User("student@univ.edu", "pass", "Aarav Sharma", "ROLE_USER");
        student.setUserType("STUDENT");
        student.setTargetRole("AI Engineer");
        student.setCurrentSkills("Python, PyTorch");

        CopilotResponse response = copilot.processUserQuery("What should I learn next for an AI Engineer internship?", student, false, null);

        assertNotNull(response);
        assertFalse(response.isRequiresConfirmation());
        assertFalse(response.getAuditLog().isEmpty());
        assertTrue(response.getAuditLog().size() <= 5, "Tool steps must not exceed step budget of 5");
        assertFalse(response.getCitedSources().isEmpty(), "Response must cite sources");
    }

    @Test
    @DisplayName("Verify consequential write action triggers Human Confirmation Gate")
    void testWriteActionConfirmationGate() {
        User student = new User("student@univ.edu", "pass", "Aarav Sharma", "ROLE_USER");
        student.setUserType("STUDENT");
        student.setTargetRole("Backend Developer");
        student.setCurrentSkills("Java");

        CopilotResponse response = copilot.processUserQuery("Please save this roadmap to my profile", student, false, null);

        assertTrue(response.isRequiresConfirmation(), "Write action must require confirmation");
        assertNotNull(response.getPendingActionToken());
        assertTrue(response.getPendingActionDescription().contains("Save 8-week learning plan"));
    }

    @Test
    @DisplayName("Verify executing write action with confirmation token succeeds")
    void testWriteActionConfirmed() {
        User student = new User("student@univ.edu", "pass", "Aarav Sharma", "ROLE_USER");
        CopilotResponse response = copilot.processUserQuery("CONFIRM_ACTION", student, true, "act-test-1234");

        assertFalse(response.isRequiresConfirmation());
        assertTrue(response.getResponseText().contains("securely saved and confirmed"));
    }
}
