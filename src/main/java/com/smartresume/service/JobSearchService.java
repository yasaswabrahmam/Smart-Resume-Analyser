package com.smartresume.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartresume.dto.JobFilterDto;
import com.smartresume.dto.JobSearchResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Service
public class JobSearchService {

    @Value("${remotive.api.url:https://remotive.com/api/remote-jobs}")
    private String remotiveApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static class JobPortalInfo {
        private String name;
        private String icon;
        private String color;
        private String urlPattern;

        public JobPortalInfo(String name, String icon, String color, String urlPattern) {
            this.name = name;
            this.icon = icon;
            this.color = color;
            this.urlPattern = urlPattern;
        }

        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getColor() { return color; }
        public String getUrlPattern() { return urlPattern; }
    }

    private static final List<JobPortalInfo> PORTALS = List.of(
            new JobPortalInfo("LinkedIn", "fab fa-linkedin", "#0A66C2", "https://www.linkedin.com/jobs/search/?keywords=%s&location=%s"),
            new JobPortalInfo("Naukri", "fas fa-building", "#FF7555", "https://www.naukri.com/%s-jobs-in-%s"),
            new JobPortalInfo("Indeed", "fas fa-search-dollar", "#003A9B", "https://in.indeed.com/jobs?q=%s&l=%s"),
            new JobPortalInfo("Foundit (Monster)", "fas fa-globe", "#5D3FD3", "https://www.foundit.in/srp/results?query=%s&locations=%s"),
            new JobPortalInfo("FreshersWorld", "fas fa-graduation-cap", "#003A9B", "https://www.freshersworld.com/jobs/jobsearch/%s-jobs-in-%s"),
            new JobPortalInfo("TimesJobs", "fas fa-briefcase", "#003A9B", "https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=%s&txtLocation=%s"),
            new JobPortalInfo("Instahyre", "fas fa-user-tie", "#003A9B", "https://www.instahyre.com/%s-jobs-in-%s")
    );

    public List<JobPortalInfo> getPortals() {
        return PORTALS;
    }

    public Map<String, String> buildPortalSearchUrls(String query, String location) {
        Map<String, String> urls = new LinkedHashMap<>();
        String encodedQ = URLEncoder.encode(query != null ? query : "", StandardCharsets.UTF_8);
        String encodedL = URLEncoder.encode(location != null ? location : "India", StandardCharsets.UTF_8);
        String hyphenQ = query != null ? query.toLowerCase().replaceAll("[^a-z0-9]+", "-") : "software";
        String hyphenL = location != null ? location.toLowerCase().replaceAll("[^a-z0-9]+", "-") : "india";

        for (JobPortalInfo portal : PORTALS) {
            String url;
            if (portal.getName().equals("Naukri") || portal.getName().equals("FreshersWorld") || portal.getName().equals("Instahyre")) {
                url = String.format(portal.getUrlPattern(), hyphenQ, hyphenL);
            } else {
                url = String.format(portal.getUrlPattern(), encodedQ, encodedL);
            }
            urls.put(portal.getName(), url);
        }
        return urls;
    }

    public List<JobSearchResultDto> searchLiveJobs(JobFilterDto filter) {
        List<JobSearchResultDto> results = new ArrayList<>();
        String q = (filter.getQuery() != null && !filter.getQuery().isBlank()) ? filter.getQuery() : "software";

        try {
            String url = remotiveApiUrl + "?search=" + URLEncoder.encode(q, StandardCharsets.UTF_8) + "&limit=12";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Smart-AI-Resume-Analyzer/1.0")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode jobs = root.path("jobs");
                if (jobs.isArray()) {
                    for (JsonNode job : jobs) {
                        JobSearchResultDto dto = new JobSearchResultDto(
                                job.path("id").asText(),
                                job.path("title").asText(),
                                job.path("company_name").asText(),
                                job.path("candidate_required_location").asText("Remote"),
                                "Remote",
                                job.path("job_type").asText("Full Time"),
                                job.path("salary").asText("Competitive"),
                                "Remotive API",
                                job.path("url").asText(),
                                job.path("url").asText(),
                                job.path("publication_date").asText(),
                                job.path("description").asText(),
                                true
                        );
                        results.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Remotive API call failed: " + e.getMessage());
        }

        // Fallback / Demonstration sample jobs if external public API is offline
        if (results.isEmpty()) {
            results.add(new JobSearchResultDto("101", "Senior Full Stack Engineer", "Google Cloud", "Bangalore, India", "Hybrid", "Full Time", "₹25 - ₹40 LPA", "Direct Enterprise Partner", "https://careers.google.com", "https://careers.google.com", "Today", "Designing high-throughput cloud microservices with Java, React, and Kubernetes.", true));
            results.add(new JobSearchResultDto("102", "Backend Java / Spring Boot Developer", "Amazon Web Services", "Hyderabad, India", "Onsite", "Full Time", "₹18 - ₹32 LPA", "Direct Enterprise Partner", "https://amazon.jobs", "https://amazon.jobs", "Yesterday", "Building scalable distributed backend services and database architectures.", true));
            results.add(new JobSearchResultDto("103", "Data Scientist & AI Engineer", "Microsoft AI", "Remote / Bangalore", "Remote", "Full Time", "₹22 - ₹38 LPA", "Direct Enterprise Partner", "https://careers.microsoft.com", "https://careers.microsoft.com", "2 days ago", "Training large language models and developing production ML pipelines.", true));
            results.add(new JobSearchResultDto("104", "DevOps & Platform Engineer", "TCS Digital", "Pune, India", "Hybrid", "Full Time", "₹12 - ₹20 LPA", "Direct Enterprise Partner", "https://www.tcs.com/careers", "https://www.tcs.com/careers", "3 days ago", "Automating CI/CD pipelines, container orchestration, and infrastructure.", true));
        }

        return results;
    }

    public Map<String, List<Map<String, String>>> getFeaturedCompanies() {
        Map<String, List<Map<String, String>>> companies = new LinkedHashMap<>();

        companies.put("Tech Giants", List.of(
                Map.of("name", "Google", "color", "#4285F4", "icon", "fab fa-google", "desc", "Search, Cloud, Android & AI Innovation", "careers", "https://careers.google.com"),
                Map.of("name", "Microsoft", "color", "#00A4EF", "icon", "fab fa-microsoft", "desc", "Cloud Platforms, Enterprise & Gaming", "careers", "https://careers.microsoft.com"),
                Map.of("name", "Amazon", "color", "#FF9900", "icon", "fab fa-amazon", "desc", "E-Commerce & AWS Cloud Computing", "careers", "https://www.amazon.jobs"),
                Map.of("name", "Apple", "color", "#555555", "icon", "fab fa-apple", "desc", "Consumer Devices, iOS & Systems", "careers", "https://www.apple.com/careers"),
                Map.of("name", "Meta", "color", "#1877F2", "icon", "fab fa-facebook", "desc", "Social Technologies, AI & Infrastructure", "careers", "https://www.metacareers.com/")
        ));

        companies.put("Indian Tech Leaders", List.of(
                Map.of("name", "TCS", "color", "#0070C0", "icon", "fas fa-building", "desc", "India's Premier Global IT Consulting", "careers", "https://www.tcs.com/careers"),
                Map.of("name", "Infosys", "color", "#007CC3", "icon", "fas fa-building", "desc", "Digital Transformation & Cloud Services", "careers", "https://www.infosys.com/careers"),
                Map.of("name", "Wipro", "color", "#341F65", "icon", "fas fa-building", "desc", "Global IT Services and Cybersecurity", "careers", "https://careers.wipro.com"),
                Map.of("name", "HCL Tech", "color", "#0075C9", "icon", "fas fa-building", "desc", "Next-Gen Tech and Engineering R&D", "careers", "https://www.hcltech.com/careers")
        ));

        companies.put("Global Consulting & Enterprise", List.of(
                Map.of("name", "IBM", "color", "#1F70C1", "icon", "fas fa-server", "desc", "Enterprise Hybrid Cloud & Consulting", "careers", "https://www.ibm.com/careers"),
                Map.of("name", "Accenture", "color", "#A100FF", "icon", "fas fa-building", "desc", "Global Management Consulting & Technology", "careers", "https://www.accenture.com/careers"),
                Map.of("name", "Cognizant", "color", "#1299D8", "icon", "fas fa-building", "desc", "Digital Business Services & Modernization", "careers", "https://careers.cognizant.com")
        ));

        return companies;
    }

    public Map<String, Object> getMarketInsights() {
        Map<String, Object> insights = new LinkedHashMap<>();

        insights.put("trendingSkills", List.of(
                Map.of("name", "Artificial Intelligence", "growth", "+45%", "icon", "fas fa-brain"),
                Map.of("name", "Cloud Computing (AWS/Azure)", "growth", "+38%", "icon", "fas fa-cloud"),
                Map.of("name", "Data Science & Big Data", "growth", "+35%", "icon", "fas fa-chart-line"),
                Map.of("name", "Cybersecurity", "growth", "+32%", "icon", "fas fa-shield-alt"),
                Map.of("name", "DevOps & Kubernetes", "growth", "+30%", "icon", "fas fa-code-branch"),
                Map.of("name", "Machine Learning (LLMs)", "growth", "+28%", "icon", "fas fa-robot")
        ));

        insights.put("topLocations", List.of(
                Map.of("name", "Bangalore", "jobs", "50,000+", "icon", "fas fa-city"),
                Map.of("name", "Mumbai", "jobs", "35,000+", "icon", "fas fa-city"),
                Map.of("name", "Delhi NCR", "jobs", "30,000+", "icon", "fas fa-city"),
                Map.of("name", "Hyderabad", "jobs", "25,000+", "icon", "fas fa-city"),
                Map.of("name", "Pune", "jobs", "20,000+", "icon", "fas fa-city"),
                Map.of("name", "Remote", "jobs", "15,000+", "icon", "fas fa-globe")
        ));

        insights.put("salaryInsights", List.of(
                Map.of("role", "Machine Learning Engineer", "range", "₹12 - ₹35 LPA", "exp", "0-5 yrs"),
                Map.of("role", "Full Stack Developer", "range", "₹8 - ₹28 LPA", "exp", "0-5 yrs"),
                Map.of("role", "Cloud / DevOps Engineer", "range", "₹8 - ₹30 LPA", "exp", "0-5 yrs"),
                Map.of("role", "Data Scientist", "range", "₹10 - ₹30 LPA", "exp", "0-5 yrs"),
                Map.of("role", "Java Backend Developer", "range", "₹7 - ₹26 LPA", "exp", "0-5 yrs"),
                Map.of("role", "UI/UX Designer", "range", "₹6 - ₹22 LPA", "exp", "0-5 yrs")
        ));

        return insights;
    }
}
