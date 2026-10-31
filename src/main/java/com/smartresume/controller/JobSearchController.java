package com.smartresume.controller;

import com.smartresume.dto.JobFilterDto;
import com.smartresume.dto.JobSearchResultDto;
import com.smartresume.service.JobSearchService;
import com.smartresume.service.LinkedInScraperService;
import com.smartresume.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobSearchController {

    public record SkillInsight(String name, String growth, String icon) {}
    public record LocationInsight(String name, String jobs, String icon) {}
    public record SalaryInsight(String role, String range, String exp) {}
    public record MarketInsights(List<SkillInsight> trendingSkills,
                                 List<LocationInsight> topLocations,
                                 List<SalaryInsight> salaryInsights) {}

    private final JobSearchService jobSearchService;
    private final LinkedInScraperService linkedInScraperService;
    private final UserService userService;

    public JobSearchController(JobSearchService jobSearchService,
                               LinkedInScraperService linkedInScraperService,
                               UserService userService) {
        this.jobSearchService = jobSearchService;
        this.linkedInScraperService = linkedInScraperService;
        this.userService = userService;
    }

    @GetMapping
    public String showJobSearchPage(@ModelAttribute("filter") JobFilterDto filter,
                                    @RequestParam(value = "title", required = false) String title,
                                    Model model) {
        if (title != null && !title.isBlank() && (filter.getQuery() == null || filter.getQuery().isBlank())) {
            filter.setQuery(title.trim());
        }
        populateJobSearchModel(model, filter);
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "jobs/search";
    }

    @PostMapping("/search")
    public String searchJobs(@ModelAttribute("filter") JobFilterDto filter, Model model) {
        populateJobSearchModel(model, filter);

        com.smartresume.model.User user = userService.getCurrentUser().orElse(null);
        if (user != null) {
            model.addAttribute("currentUser", user);
        }
        return "jobs/search";
    }

    private void populateJobSearchModel(Model model, JobFilterDto filter) {
        com.smartresume.model.User user = userService.getCurrentUser().orElse(null);
        List<JobSearchResultDto> jobs = jobSearchService.searchLiveJobs(filter, user);

        model.addAttribute("jobs", jobs);
        model.addAttribute("totalJobs", jobs.size());
        model.addAttribute("filter", filter);
        model.addAttribute("sources", List.of("All", "Remotive", "LinkedIn", "Indeed", "Glassdoor", "Naukri", "Wellfound"));
        model.addAttribute("marketInsights", buildMarketInsights());
        model.addAttribute("title", "Jobs & Internships — Verified Career Discovery");
    }

    private MarketInsights buildMarketInsights() {
        List<SkillInsight> skills = List.of(
            new SkillInsight("Spring Boot & Java 21", "+46%", "fa-brands fa-java"),
            new SkillInsight("Docker & Kubernetes", "+52%", "fa-brands fa-docker"),
            new SkillInsight("React & Next.js", "+38%", "fa-brands fa-react"),
            new SkillInsight("Python & Machine Learning", "+58%", "fa-brands fa-python"),
            new SkillInsight("AWS & Cloud Security", "+44%", "fa-brands fa-aws")
        );
        List<LocationInsight> locs = List.of(
            new LocationInsight("Bangalore (Bengaluru)", "14,500+ Openings", "fa-solid fa-building"),
            new LocationInsight("Hyderabad Tech Hub", "11,200+ Openings", "fa-solid fa-city"),
            new LocationInsight("Pune / Mumbai", "9,800+ Openings", "fa-solid fa-building-wheat"),
            new LocationInsight("Delhi NCR (Gurgaon / Noida)", "10,400+ Openings", "fa-solid fa-landmark"),
            new LocationInsight("Global Remote", "18,200+ Openings", "fa-solid fa-globe")
        );
        List<SalaryInsight> salaries = List.of(
            new SalaryInsight("Full Stack Developer", "₹10 - ₹24 LPA ($80K-$130K)", "1-5 Years"),
            new SalaryInsight("DevOps / Cloud Engineer", "₹12 - ₹28 LPA ($95K-$145K)", "2-6 Years"),
            new SalaryInsight("Machine Learning Engineer", "₹14 - ₹32 LPA ($100K-$160K)", "2-6 Years"),
            new SalaryInsight("Cybersecurity Analyst", "₹9 - ₹22 LPA ($75K-$120K)", "1-4 Years"),
            new SalaryInsight("UI/UX & Product Designer", "₹8 - ₹20 LPA ($70K-$115K)", "1-4 Years")
        );
        return new MarketInsights(skills, locs, salaries);
    }

    @GetMapping("/scrape")
    public String scrapeLinkedIn(@RequestParam(value = "title", defaultValue = "Software Engineer") String title,
                                 @RequestParam(value = "location", defaultValue = "India") String location,
                                 @RequestParam(value = "count", defaultValue = "5") int count,
                                 Model model) {
        JobFilterDto filter = new JobFilterDto();
        filter.setQuery(title);
        filter.setLocation(location);
        populateJobSearchModel(model, filter);
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        model.addAttribute("scrapeMode", true);
        model.addAttribute("scrapedRole", title);
        return "jobs/search";
    }
}
