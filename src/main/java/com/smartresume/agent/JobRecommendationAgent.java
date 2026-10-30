package com.smartresume.agent;

import com.smartresume.dto.JobFilterDto;
import com.smartresume.dto.JobSearchResultDto;
import com.smartresume.service.JobSearchService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Job Recommendation Agent
 * Matches candidate profiles and target roles against available requisitions
 * and provides direct job portal recommendations.
 */
@Component
public class JobRecommendationAgent {

    private final JobSearchService jobSearchService;

    public JobRecommendationAgent(JobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    public Map<String, String> recommendJobPortals(String targetRole) {
        if (targetRole == null || targetRole.isBlank()) {
            return Collections.emptyMap();
        }
        return jobSearchService.buildPortalSearchUrls(targetRole, "India");
    }

    public List<JobSearchResultDto> recommendLiveJobs(String targetRole) {
        if (targetRole == null || targetRole.isBlank()) {
            return Collections.emptyList();
        }
        JobFilterDto filter = new JobFilterDto();
        filter.setQuery(targetRole);
        return jobSearchService.searchLiveJobs(filter);
    }
}
