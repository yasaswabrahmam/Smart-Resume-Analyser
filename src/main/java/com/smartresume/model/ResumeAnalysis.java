package com.smartresume.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_analysis")
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    private double atsScore;
    private double keywordMatchScore;
    private double formatScore;
    private double sectionScore;
    private double contactScore;
    private double summaryScore;
    private double experienceScore;
    private double educationScore;

    private String documentType; // "resume", "marksheet", "certificate", "id_card"

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String foundSkillsJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String missingSkillsJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String recommendationsJson;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ResumeAnalysis() {
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

    public double getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(double atsScore) {
        this.atsScore = atsScore;
    }

    public double getKeywordMatchScore() {
        return keywordMatchScore;
    }

    public void setKeywordMatchScore(double keywordMatchScore) {
        this.keywordMatchScore = keywordMatchScore;
    }

    public double getFormatScore() {
        return formatScore;
    }

    public void setFormatScore(double formatScore) {
        this.formatScore = formatScore;
    }

    public double getSectionScore() {
        return sectionScore;
    }

    public void setSectionScore(double sectionScore) {
        this.sectionScore = sectionScore;
    }

    public double getContactScore() {
        return contactScore;
    }

    public void setContactScore(double contactScore) {
        this.contactScore = contactScore;
    }

    public double getSummaryScore() {
        return summaryScore;
    }

    public void setSummaryScore(double summaryScore) {
        this.summaryScore = summaryScore;
    }

    public double getExperienceScore() {
        return experienceScore;
    }

    public void setExperienceScore(double experienceScore) {
        this.experienceScore = experienceScore;
    }

    public double getEducationScore() {
        return educationScore;
    }

    public void setEducationScore(double educationScore) {
        this.educationScore = educationScore;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFoundSkillsJson() {
        return foundSkillsJson;
    }

    public void setFoundSkillsJson(String foundSkillsJson) {
        this.foundSkillsJson = foundSkillsJson;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
