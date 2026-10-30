package com.smartresume.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeAnalysisResult {
    private String documentType = "resume";
    private double atsScore;
    private double keywordMatchScore;
    private double formatScore;
    private double sectionScore;

    private double contactScore;
    private double summaryScore;
    private double skillsScore;
    private double experienceScore;
    private double educationScore;

    private String name;
    private String email;
    private String phone;
    private String linkedin;
    private String github;
    private String portfolio;

    private String summary;
    private List<String> education = new ArrayList<>();
    private List<String> experience = new ArrayList<>();
    private List<String> projects = new ArrayList<>();
    private List<String> skills = new ArrayList<>();

    private List<String> foundSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();
    private List<String> suggestions = new ArrayList<>();

    private List<String> contactSuggestions = new ArrayList<>();
    private List<String> summarySuggestions = new ArrayList<>();
    private List<String> skillsSuggestions = new ArrayList<>();
    private List<String> experienceSuggestions = new ArrayList<>();
    private List<String> educationSuggestions = new ArrayList<>();
    private List<String> formatSuggestions = new ArrayList<>();

    private Map<String, List<String[]>> recommendedCourses = new HashMap<>();
    private Map<String, List<String[]>> resumeVideos = new HashMap<>();
    private Map<String, List<String[]>> interviewVideos = new HashMap<>();

    private String rawText;
    private String error;

    public ResumeAnalysisResult() {
    }

    public String getDocumentType() {
        return documentType != null ? documentType : "resume";
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public double getSkillsScore() {
        return skillsScore;
    }

    public void setSkillsScore(double skillsScore) {
        this.skillsScore = skillsScore;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getExperience() {
        return experience;
    }

    public void setExperience(List<String> experience) {
        this.experience = experience;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getFoundSkills() {
        return foundSkills;
    }

    public void setFoundSkills(List<String> foundSkills) {
        this.foundSkills = foundSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getContactSuggestions() {
        return contactSuggestions;
    }

    public void setContactSuggestions(List<String> contactSuggestions) {
        this.contactSuggestions = contactSuggestions;
    }

    public List<String> getSummarySuggestions() {
        return summarySuggestions;
    }

    public void setSummarySuggestions(List<String> summarySuggestions) {
        this.summarySuggestions = summarySuggestions;
    }

    public List<String> getSkillsSuggestions() {
        return skillsSuggestions;
    }

    public void setSkillsSuggestions(List<String> skillsSuggestions) {
        this.skillsSuggestions = skillsSuggestions;
    }

    public List<String> getExperienceSuggestions() {
        return experienceSuggestions;
    }

    public void setExperienceSuggestions(List<String> experienceSuggestions) {
        this.experienceSuggestions = experienceSuggestions;
    }

    public List<String> getEducationSuggestions() {
        return educationSuggestions;
    }

    public void setEducationSuggestions(List<String> educationSuggestions) {
        this.educationSuggestions = educationSuggestions;
    }

    public List<String> getFormatSuggestions() {
        return formatSuggestions;
    }

    public void setFormatSuggestions(List<String> formatSuggestions) {
        this.formatSuggestions = formatSuggestions;
    }

    public Map<String, List<String[]>> getRecommendedCourses() {
        return recommendedCourses;
    }

    public void setRecommendedCourses(Map<String, List<String[]>> recommendedCourses) {
        this.recommendedCourses = recommendedCourses;
    }

    public Map<String, List<String[]>> getResumeVideos() {
        return resumeVideos;
    }

    public void setResumeVideos(Map<String, List<String[]>> resumeVideos) {
        this.resumeVideos = resumeVideos;
    }

    public Map<String, List<String[]>> getInterviewVideos() {
        return interviewVideos;
    }

    public void setInterviewVideos(Map<String, List<String[]>> interviewVideos) {
        this.interviewVideos = interviewVideos;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
