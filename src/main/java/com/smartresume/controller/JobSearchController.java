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
import java.util.Map;

@Controller
@RequestMapping("/jobs")
public class JobSearchController {

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
    public String showJobSearchPage(@ModelAttribute("filter") JobFilterDto filter, Model model) {
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

        // Fetch live jobs with personalized eligibility evaluation
        List<JobSearchResultDto> liveJobs = jobSearchService.searchLiveJobs(filter, user);
        model.addAttribute("liveJobs", liveJobs);

        // Generate direct targeted search URLs for all 7 portals
        Map<String, String> portalUrls = jobSearchService.buildPortalSearchUrls(filter.getQuery(), filter.getLocation());
        model.addAttribute("portalUrls", portalUrls);

        model.addAttribute("hasSearched", true);
        return "jobs/search";
    }

    @PostMapping("/scrape-linkedin")
    public String scrapeLinkedIn(@RequestParam(value = "title", defaultValue = "Software Engineer") String title,
                                 @RequestParam(value = "location", defaultValue = "India") String location,
                                 @RequestParam(value = "count", defaultValue = "5") int count,
                                 Model model) {
        JobFilterDto filter = new JobFilterDto();
        filter.setQuery(title);
        filter.setLocation(location);
        populateJobSearchModel(model, filter);

        LinkedInScraperService.ScraperResult scrapeResult = linkedInScraperService.scrapeLinkedIn(title, location, count);
        model.addAttribute("scrapeResult", scrapeResult);
        model.addAttribute("liveJobs", scrapeResult.getJobs());
        model.addAttribute("hasSearched", true);
        model.addAttribute("isLinkedInScrape", true);

        Map<String, String> portalUrls = jobSearchService.buildPortalSearchUrls(title, location);
        model.addAttribute("portalUrls", portalUrls);

        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "jobs/search";
    }

    private void populateJobSearchModel(Model model, JobFilterDto filter) {
        model.addAttribute("portals", jobSearchService.getPortals());
        model.addAttribute("featuredCompanies", jobSearchService.getFeaturedCompanies());
        model.addAttribute("marketInsights", jobSearchService.getMarketInsights());
        model.addAttribute("filter", filter != null ? filter : new JobFilterDto());
    }
}
