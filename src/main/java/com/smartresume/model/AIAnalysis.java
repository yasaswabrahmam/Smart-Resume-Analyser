package com.smartresume.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_analyses")
public class AIAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String targetRole;
    private String targetCategory;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String jobDescription;

    private String modelUsed;

    private double resumeScore;
    private double atsScore;
    private double jobMatchScore;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String strengthsJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String weaknessesJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String missingSkillsJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String recommendationsJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fullAnalysis;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public AIAnalysis() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public double getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(double resumeScore) {
        this.resumeScore = resumeScore;
    }

    public double getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(double atsScore) {
        this.atsScore = atsScore;
    }

    public double getJobMatchScore() {
        return jobMatchScore;
    }

    public void setJobMatchScore(double jobMatchScore) {
        this.jobMatchScore = jobMatchScore;
    }

    public String getStrengthsJson() {
        return strengthsJson;
    }

    public void setStrengthsJson(String strengthsJson) {
        this.strengthsJson = strengthsJson;
    }

    public String getWeaknessesJson() {
        return weaknessesJson;
    }

    public void setWeaknessesJson(String weaknessesJson) {
        this.weaknessesJson = weaknessesJson;
    }

    public String getMissingSkillsJson() {
        return missingSkillsJson;
    }

    public void setMissingSkillsJson(String missingSkillsJson) {
        this.missingSkillsJson = missingSkillsJson;
    }

    public String getRecommendationsJson() {
        return recommendationsJson;
    }

    public void setRecommendationsJson(String recommendationsJson) {
        this.recommendationsJson = recommendationsJson;
    }

    public String getFullAnalysis() {
        return fullAnalysis;
    }

    public void setFullAnalysis(String fullAnalysis) {
        this.fullAnalysis = fullAnalysis;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
