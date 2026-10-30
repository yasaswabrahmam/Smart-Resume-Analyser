package com.smartresume.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardMetricsDto {
    private long totalResumes;
    private double avgAtsScore;
    private double avgKeywordScore;
    private long highScoringResumes;
    private double highScoringPercentage;

    private Map<String, Long> skillDistribution = new HashMap<>();
    private Map<String, Long> weeklyTrends = new HashMap<>();
    private Map<String, Double> categorySuccessRates = new HashMap<>();

    // Chart.js Series for Analytics Dashboard
    private List<String> skillLabels = new ArrayList<>();
    private List<Long> skillCounts = new ArrayList<>();
    private List<String> scoreLabels = new ArrayList<>();
    private List<Long> scoreCounts = new ArrayList<>();
    private List<String> categoryLabels = new ArrayList<>();
    private List<Long> categoryCounts = new ArrayList<>();
    private List<String> roleLabels = new ArrayList<>();
    private List<Long> roleCounts = new ArrayList<>();

    public DashboardMetricsDto() {
    }

    public long getTotalResumes() { return totalResumes; }
    public void setTotalResumes(long totalResumes) { this.totalResumes = totalResumes; }

    public double getAvgAtsScore() { return avgAtsScore; }
    public void setAvgAtsScore(double avgAtsScore) { this.avgAtsScore = avgAtsScore; }

    public double getAvgKeywordScore() { return avgKeywordScore; }
    public void setAvgKeywordScore(double avgKeywordScore) { this.avgKeywordScore = avgKeywordScore; }

    public long getHighScoringResumes() { return highScoringResumes; }
    public void setHighScoringResumes(long highScoringResumes) { this.highScoringResumes = highScoringResumes; }

    public double getHighScoringPercentage() { return highScoringPercentage; }
    public void setHighScoringPercentage(double highScoringPercentage) { this.highScoringPercentage = highScoringPercentage; }

    public Map<String, Long> getSkillDistribution() { return skillDistribution; }
    public void setSkillDistribution(Map<String, Long> skillDistribution) { this.skillDistribution = skillDistribution; }

    public Map<String, Long> getWeeklyTrends() { return weeklyTrends; }
    public void setWeeklyTrends(Map<String, Long> weeklyTrends) { this.weeklyTrends = weeklyTrends; }

    public Map<String, Double> getCategorySuccessRates() { return categorySuccessRates; }
    public void setCategorySuccessRates(Map<String, Double> categorySuccessRates) { this.categorySuccessRates = categorySuccessRates; }

    public List<String> getSkillLabels() { return skillLabels; }
    public void setSkillLabels(List<String> skillLabels) { this.skillLabels = skillLabels; }

    public List<Long> getSkillCounts() { return skillCounts; }
    public void setSkillCounts(List<Long> skillCounts) { this.skillCounts = skillCounts; }

    public List<String> getScoreLabels() { return scoreLabels; }
    public void setScoreLabels(List<String> scoreLabels) { this.scoreLabels = scoreLabels; }

    public List<Long> getScoreCounts() { return scoreCounts; }
    public void setScoreCounts(List<Long> scoreCounts) { this.scoreCounts = scoreCounts; }

    public List<String> getCategoryLabels() { return categoryLabels; }
    public void setCategoryLabels(List<String> categoryLabels) { this.categoryLabels = categoryLabels; }

    public List<Long> getCategoryCounts() { return categoryCounts; }
    public void setCategoryCounts(List<Long> categoryCounts) { this.categoryCounts = categoryCounts; }

    public List<String> getRoleLabels() { return roleLabels; }
    public void setRoleLabels(List<String> roleLabels) { this.roleLabels = roleLabels; }

    public List<Long> getRoleCounts() { return roleCounts; }
    public void setRoleCounts(List<Long> roleCounts) { this.roleCounts = roleCounts; }
}
