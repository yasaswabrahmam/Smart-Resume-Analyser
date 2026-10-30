package com.smartresume.dto;

import java.util.ArrayList;
import java.util.List;

public class ResumeBuilderDto {
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private String title;
    private String linkedin;
    private String portfolio;

    private String summary;
    private String targetRole;
    private String targetCategory;
    private String template = "Modern"; // "Modern", "Professional", "Minimal", "Creative"

    private List<ExperienceItem> experiences = new ArrayList<>();
    private List<ProjectItem> projects = new ArrayList<>();
    private List<EducationItem> educations = new ArrayList<>();

    private List<String> technicalSkills = new ArrayList<>();
    private List<String> softSkills = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> tools = new ArrayList<>();

    public ResumeBuilderDto() {
    }

    public static class ExperienceItem {
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private String description;
        private String responsibilities; // newline or bullet-separated
        private String achievements;

        public ExperienceItem() {}

        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getResponsibilities() { return responsibilities; }
        public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
        public String getAchievements() { return achievements; }
        public void setAchievements(String achievements) { this.achievements = achievements; }
    }

    public static class ProjectItem {
        private String name;
        private String technologies;
        private String description;
        private String responsibilities;
        private String achievements;
        private String link;

        public ProjectItem() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getTechnologies() { return technologies; }
        public void setTechnologies(String technologies) { this.technologies = technologies; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getResponsibilities() { return responsibilities; }
        public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
        public String getAchievements() { return achievements; }
        public void setAchievements(String achievements) { this.achievements = achievements; }
        public String getLink() { return link; }
        public void setLink(String link) { this.link = link; }
    }

    public static class EducationItem {
        private String school;
        private String degree;
        private String field;
        private String graduationDate;
        private String gpa;
        private String achievements;

        public EducationItem() {}

        public String getSchool() { return school; }
        public void setSchool(String school) { this.school = school; }
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getGraduationDate() { return graduationDate; }
        public void setGraduationDate(String graduationDate) { this.graduationDate = graduationDate; }
        public String getGpa() { return gpa; }
        public void setGpa(String gpa) { this.gpa = gpa; }
        public String getAchievements() { return achievements; }
        public void setAchievements(String achievements) { this.achievements = achievements; }
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }
    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public String getTargetCategory() { return targetCategory; }
    public void setTargetCategory(String targetCategory) { this.targetCategory = targetCategory; }
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
    public List<ExperienceItem> getExperiences() { return experiences; }
    public void setExperiences(List<ExperienceItem> experiences) { this.experiences = experiences; }
    public List<ProjectItem> getProjects() { return projects; }
    public void setProjects(List<ProjectItem> projects) { this.projects = projects; }
    public List<EducationItem> getEducations() { return educations; }
    public void setEducations(List<EducationItem> educations) { this.educations = educations; }
    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) { this.technicalSkills = technicalSkills; }
    public List<String> getSoftSkills() { return softSkills; }
    public void setSoftSkills(List<String> softSkills) { this.softSkills = softSkills; }
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    public List<String> getTools() { return tools; }
    public void setTools(List<String> tools) { this.tools = tools; }
}
