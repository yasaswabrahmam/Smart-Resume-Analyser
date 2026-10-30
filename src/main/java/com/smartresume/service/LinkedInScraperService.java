package com.smartresume.service;

import com.smartresume.dto.JobSearchResultDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkedInScraperService {

    public static class ScraperResult {
        private boolean success;
        private String message;
        private String fallbackUrl;
        private List<JobSearchResultDto> jobs = new ArrayList<>();

        public ScraperResult() {}

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getFallbackUrl() { return fallbackUrl; }
        public void setFallbackUrl(String fallbackUrl) { this.fallbackUrl = fallbackUrl; }
        public List<JobSearchResultDto> getJobs() { return jobs; }
        public void setJobs(List<JobSearchResultDto> jobs) { this.jobs = jobs; }
    }

    public ScraperResult scrapeLinkedIn(String jobTitle, String location, int count) {
        ScraperResult result = new ScraperResult();
        String q = (jobTitle != null && !jobTitle.isBlank()) ? jobTitle.trim() : "Software Engineer";
        String loc = (location != null && !location.isBlank()) ? location.trim() : "India";
        int targetCount = Math.min(Math.max(count, 1), 10);

        String searchUrl = String.format("https://www.linkedin.com/jobs/search/?keywords=%s&location=%s",
                URLEncoder.encode(q, StandardCharsets.UTF_8),
                URLEncoder.encode(loc, StandardCharsets.UTF_8));
        result.setFallbackUrl(searchUrl);

        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

            driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            driver.get(searchUrl);

            List<WebElement> jobCards = driver.findElements(By.cssSelector(".base-card, .job-search-card"));
            for (int i = 0; i < Math.min(jobCards.size(), targetCount); i++) {
                WebElement card = jobCards.get(i);
                try {
                    String title = card.findElement(By.cssSelector(".base-search-card__title")).getText().trim();
                    String company = card.findElement(By.cssSelector(".base-search-card__subtitle")).getText().trim();
                    String jobLoc = card.findElement(By.cssSelector(".job-search-card__location")).getText().trim();
                    String link = card.findElement(By.cssSelector("a")).getAttribute("href");

                    JobSearchResultDto job = new JobSearchResultDto(
                            "li-" + (i + 1),
                            title,
                            company,
                            jobLoc,
                            "Onsite / Hybrid",
                            "Full Time",
                            "Competitive",
                            "LinkedIn Scraper (Live)",
                            link,
                            link,
                            "Just now",
                            "Live job listing scraped from LinkedIn.",
                            true
                    );
                    result.getJobs().add(job);
                } catch (Exception inner) {
                    // Ignore individual card extraction issue
                }
            }

            if (!result.getJobs().isEmpty()) {
                result.setSuccess(true);
                result.setMessage("Successfully scraped " + result.getJobs().size() + " live jobs from LinkedIn.");
            } else {
                result.setSuccess(false);
                result.setMessage("LinkedIn anti-bot protection prevented direct scraping. Please use the direct LinkedIn search link below.");
            }
        } catch (Throwable t) {
            result.setSuccess(false);
            result.setMessage("LinkedIn automation driver was unavailable or blocked by network policy. Falling back to direct LinkedIn search URL.");
            System.err.println("Selenium LinkedIn scrape skipped/failed: " + t.getMessage());
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignored) {}
            }
        }

        return result;
    }
}
