package com.smartresume.dto;

public class JobSearchResultDto {
    private String id;
    private String title;
    private String company;
    private String location;
    private String workMode; // "Remote", "Onsite", "Hybrid"
    private String employmentType; // "Full Time", "Part Time", "Contract"
    private String salary;
    private String source; // "Remotive", "LinkedIn", "Naukri", etc.
    private String sourceUrl;
    private String applyUrl;
    private String postedDate;
    private String description;
    private boolean isLive; // true if scraped or API fetched, false if direct portal search link

    public JobSearchResultDto() {
    }

    public JobSearchResultDto(String id, String title, String company, String location, String workMode,
                              String employmentType, String salary, String source, String sourceUrl,
                              String applyUrl, String postedDate, String description, boolean isLive) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.workMode = workMode;
        this.employmentType = employmentType;
        this.salary = salary;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.applyUrl = applyUrl;
        this.postedDate = postedDate;
        this.description = description;
        this.isLive = isLive;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public String getApplyUrl() { return applyUrl; }
    public void setApplyUrl(String applyUrl) { this.applyUrl = applyUrl; }
    public String getPostedDate() { return postedDate; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isLive() { return isLive; }
    public void setLive(boolean live) { isLive = live; }
}
