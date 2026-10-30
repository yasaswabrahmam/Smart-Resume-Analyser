package com.smartresume.dto;

import java.util.ArrayList;
import java.util.List;

public class AIAnalysisResult {
    private double resumeScore;
    private double atsScore;
    private double jobMatchScore;
    private List<String> strengths = new ArrayList<>();
    private List<String> weaknesses = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();
    private List<String> recommendations = new ArrayList<>();
    private String fullAnalysis;
    private String modelUsed;

    private String candidateName;
    private String targetRole;
    private String targetCategory;
    private String jobDescription;
    private String error;

    public AIAnalysisResult() {
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

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public String getFullAnalysis() {
        return fullAnalysis;
    }

    public void setFullAnalysis(String fullAnalysis) {
        this.fullAnalysis = fullAnalysis;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // Template alias helpers
    public double getMatchPercentage() {
        return jobMatchScore > 0 ? jobMatchScore : (resumeScore > 0 ? resumeScore : atsScore);
    }

    public List<String> getAreasForImprovement() {
        return (weaknesses != null && !weaknesses.isEmpty()) ? weaknesses : missingSkills;
    }
}
