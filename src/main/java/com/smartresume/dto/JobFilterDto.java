package com.smartresume.dto;

public class JobFilterDto {
    private String query = "";
    private String location = "";
    private String experienceLevel = "all";
    private String salaryRange = "all";
    private String jobType = "all";
    private String portal = "all";

    public JobFilterDto() {
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public String getPortal() { return portal; }
    public void setPortal(String portal) { this.portal = portal; }
}
