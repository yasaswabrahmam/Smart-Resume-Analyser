package com.smartresume.service;

import com.smartresume.model.JobMatch;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import com.smartresume.repository.JobMatchRepository;
import com.smartresume.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApplicationTrackerService {

    private final JobMatchRepository jobMatchRepository;
    private final ResumeRepository resumeRepository;

    public ApplicationTrackerService(JobMatchRepository jobMatchRepository,
                                     ResumeRepository resumeRepository) {
        this.jobMatchRepository = jobMatchRepository;
        this.resumeRepository = resumeRepository;
    }

    public static class PipelineMetrics {
        private long totalSaved;
        private long totalApplied;
        private long totalInterviewing;
        private long totalOffered;

        public PipelineMetrics(long totalSaved, long totalApplied, long totalInterviewing, long totalOffered) {
            this.totalSaved = totalSaved;
            this.totalApplied = totalApplied;
            this.totalInterviewing = totalInterviewing;
            this.totalOffered = totalOffered;
        }

        public long getTotalSaved() { return totalSaved; }
        public long getTotalApplied() { return totalApplied; }
        public long getTotalInterviewing() { return totalInterviewing; }
        public long getTotalOffered() { return totalOffered; }
        public long getTotalAll() { return totalSaved + totalApplied + totalInterviewing + totalOffered; }
    }

    public List<JobMatch> getApplicationsForUser(User user) {
        return jobMatchRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public PipelineMetrics getMetricsForUser(User user) {
        List<JobMatch> all = jobMatchRepository.findByUserOrderByCreatedAtDesc(user);
        long saved = all.stream().filter(j -> "SAVED".equalsIgnoreCase(j.getStatus())).count();
        long applied = all.stream().filter(j -> "APPLIED".equalsIgnoreCase(j.getStatus())).count();
        long interviewing = all.stream().filter(j -> "INTERVIEWING".equalsIgnoreCase(j.getStatus())).count();
        long offered = all.stream().filter(j -> "OFFERED".equalsIgnoreCase(j.getStatus())).count();
        return new PipelineMetrics(saved, applied, interviewing, offered);
    }

    @Transactional
    public JobMatch saveJob(User user, String jobTitle, String company, String location,
                            double matchScore, String url, String source, String notes) {
        JobMatch match = new JobMatch();
        match.setUser(user);
        match.setJobTitle(jobTitle != null && !jobTitle.isBlank() ? jobTitle : "Software Engineer");
        match.setCompany(company != null && !company.isBlank() ? company : "Tech Company");
        match.setLocation(location != null && !location.isBlank() ? location : "Remote / India");
        match.setMatchScore(matchScore > 0 ? matchScore : 85.0);
        match.setUrl(url != null && !url.isBlank() ? url : "https://www.linkedin.com/jobs");
        match.setSource(source != null && !source.isBlank() ? source : "LinkedIn");
        match.setStatus("SAVED");
        match.setNotes(notes != null ? notes : "Discovered via Smart Resume AI job aggregator.");
        match.setCreatedAt(LocalDateTime.now());

        // Attach latest resume if present
        List<Resume> resumes = resumeRepository.findByUserOrderByCreatedAtDesc(user);
        if (!resumes.isEmpty()) {
            match.setResume(resumes.get(0));
        }

        return jobMatchRepository.save(match);
    }

    @Transactional
    public boolean updateStatus(Long id, User user, String newStatus, String notes) {
        Optional<JobMatch> opt = jobMatchRepository.findById(id);
        if (opt.isPresent()) {
            JobMatch match = opt.get();
            // Ownership check
            if (match.getUser() != null && match.getUser().getId().equals(user.getId())) {
                if (newStatus != null && !newStatus.isBlank()) {
                    match.setStatus(newStatus.toUpperCase().trim());
                }
                if (notes != null) {
                    match.setNotes(notes);
                }
                jobMatchRepository.save(match);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean deleteJob(Long id, User user) {
        Optional<JobMatch> opt = jobMatchRepository.findById(id);
        if (opt.isPresent()) {
            JobMatch match = opt.get();
            // Ownership check
            if (match.getUser() != null && match.getUser().getId().equals(user.getId())) {
                jobMatchRepository.delete(match);
                return true;
            }
        }
        return false;
    }
}
