package com.smartresume.config;

import com.smartresume.model.AdminLog;
import com.smartresume.model.Feedback;
import com.smartresume.model.Resume;
import com.smartresume.model.ResumeAnalysis;
import com.smartresume.model.User;
import com.smartresume.repository.AdminLogRepository;
import com.smartresume.repository.FeedbackRepository;
import com.smartresume.repository.ResumeAnalysisRepository;
import com.smartresume.repository.ResumeRepository;
import com.smartresume.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.demo.seed-enabled:true}")
    private boolean seedEnabled;

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final FeedbackRepository feedbackRepository;
    private final AdminLogRepository adminLogRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ResumeRepository resumeRepository,
                           ResumeAnalysisRepository resumeAnalysisRepository,
                           FeedbackRepository feedbackRepository,
                           AdminLogRepository adminLogRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.feedbackRepository = feedbackRepository;
        this.adminLogRepository = adminLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!seedEnabled) {
            return;
        }

        // Initialize Default Admin if not present
        if (!userRepository.existsByEmail("admin@smartresume.com")) {
            User admin = new User(
                    "admin@smartresume.com",
                    passwordEncoder.encode("admin123"),
                    "System Administrator",
                    "ROLE_ADMIN"
            );
            userRepository.save(admin);
            adminLogRepository.save(new AdminLog("admin@smartresume.com", "SYSTEM_INIT", "Initialized default administrator account"));
        }

        // Initialize Demo User if not present
        if (!userRepository.existsByEmail("user@smartresume.com")) {
            User demoUser = new User(
                    "user@smartresume.com",
                    passwordEncoder.encode("user123"),
                    "Demo Candidate",
                    "ROLE_USER"
            );
            User savedUser = userRepository.save(demoUser);

            // Seed a sample demonstration resume with high-scoring analysis
            Resume sampleResume = new Resume();
            sampleResume.setUser(savedUser);
            sampleResume.setName("Demo Candidate");
            sampleResume.setEmail("user@smartresume.com");
            sampleResume.setPhone("+1-555-0199");
            sampleResume.setLocation("Bangalore, India");
            sampleResume.setLinkedin("linkedin.com/in/democandidate");
            sampleResume.setGithub("github.com/democandidate");
            sampleResume.setTargetRole("Backend Developer");
            sampleResume.setTargetCategory("Software Development and Engineering");
            sampleResume.setSummary("Experienced Backend Developer specializing in Java, Spring Boot, microservices, and MySQL. Proven track record designing scalable REST APIs.");
            sampleResume.setTemplate("Professional");
            sampleResume.setFileType("DOCX");
            sampleResume.setFileName("demo_candidate_resume.docx");
            sampleResume.setRawText("Demo Candidate\nEmail: user@smartresume.com Phone: 555-0199 linkedin.com/in/democandidate\nSUMMARY: Experienced Backend Developer specializing in Java, Spring Boot, microservices, SQL, and APIs.\nEXPERIENCE:\nSoftware Engineer at Tech Corp (2022 - Present)\n• Developed microservices using Java and Spring Boot\n• Optimized SQL database queries reducing latency by 35%\nEDUCATION:\nB.Tech in Computer Science, State University, 2022, GPA: 3.8\nSKILLS:\nJava, Spring Boot, Python, SQL, APIs, Docker, Git, RESTful APIs");
            Resume savedResume = resumeRepository.save(sampleResume);

            ResumeAnalysis analysis = new ResumeAnalysis();
            analysis.setResume(savedResume);
            analysis.setAtsScore(86.0);
            analysis.setKeywordMatchScore(87.5);
            analysis.setFormatScore(90.0);
            analysis.setSectionScore(95.0);
            analysis.setContactScore(100.0);
            analysis.setSummaryScore(100.0);
            analysis.setExperienceScore(90.0);
            analysis.setEducationScore(100.0);
            analysis.setDocumentType("resume");
            analysis.setFoundSkillsJson("[\"Java\", \"Spring Boot\", \"Python\", \"SQL\", \"APIs\", \"Docker\"]");
            analysis.setMissingSkillsJson("[\"Node.js\", \"Django\"]");
            analysis.setRecommendationsJson("[\"Add quantifiable metrics to recent projects\", \"Highlight container orchestration experience\"]");
            resumeAnalysisRepository.save(analysis);

            // Seed initial feedback
            Feedback feedback = new Feedback();
            feedback.setUser(savedUser);
            feedback.setRating(5);
            feedback.setUsabilityScore(5);
            feedback.setFeatureSatisfaction(5);
            feedback.setMissingFeatures("None, everything works seamlessly!");
            feedback.setImprovementSuggestions("Keep adding more resume templates.");
            feedback.setUserExperience("Superb resume analysis and instant ATS suggestions!");
            feedback.setTimestamp(LocalDateTime.now().minusDays(1));
            feedbackRepository.save(feedback);
        }
    }
}
