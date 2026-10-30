package com.smartresume.service;

import com.smartresume.dto.FeedbackDto;
import com.smartresume.model.Feedback;
import com.smartresume.model.User;
import com.smartresume.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional
    public Feedback submitFeedback(FeedbackDto dto, User user) {
        Feedback f = new Feedback();
        f.setUser(user);
        f.setRating(dto.getRating());
        f.setUsabilityScore(dto.getUsabilityScore());
        f.setFeatureSatisfaction(dto.getFeatureSatisfaction());
        f.setMissingFeatures(dto.getMissingFeatures());
        f.setImprovementSuggestions(dto.getImprovementSuggestions());
        f.setUserExperience(dto.getUserExperience());
        f.setTimestamp(LocalDateTime.now());
        return feedbackRepository.save(f);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByTimestampDesc();
    }

    public Map<String, Object> getFeedbackStats() {
        Map<String, Object> stats = new HashMap<>();
        long total = feedbackRepository.count();
        Double avgRating = feedbackRepository.getAverageRating();
        Double avgUsability = feedbackRepository.getAverageUsabilityScore();
        Double avgSatisfaction = feedbackRepository.getAverageFeatureSatisfaction();

        stats.put("totalResponses", total);
        stats.put("avgRating", avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        stats.put("avgUsability", avgUsability != null ? Math.round(avgUsability * 10.0) / 10.0 : 0.0);
        stats.put("avgSatisfaction", avgSatisfaction != null ? Math.round(avgSatisfaction * 10.0) / 10.0 : 0.0);

        return stats;
    }
}
