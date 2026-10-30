package com.smartresume.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int rating; // 1-5
    private int usabilityScore; // 1-5
    private int featureSatisfaction; // 1-5

    @Column(length = 2000)
    private String missingFeatures;

    @Column(length = 2000)
    private String improvementSuggestions;

    @Column(length = 2000)
    private String userExperience;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public Feedback() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUsabilityScore() {
        return usabilityScore;
    }

    public void setUsabilityScore(int usabilityScore) {
        this.usabilityScore = usabilityScore;
    }

    public int getFeatureSatisfaction() {
        return featureSatisfaction;
    }

    public void setFeatureSatisfaction(int featureSatisfaction) {
        this.featureSatisfaction = featureSatisfaction;
    }

    public String getMissingFeatures() {
        return missingFeatures;
    }

    public void setMissingFeatures(String missingFeatures) {
        this.missingFeatures = missingFeatures;
    }

    public String getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(String improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    public String getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(String userExperience) {
        this.userExperience = userExperience;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Helper getters for dashboard/admin presentation
    public String getName() {
        return user != null && user.getFullName() != null ? user.getFullName() : "Anonymous User";
    }

    public String getEmail() {
        return user != null && user.getEmail() != null ? user.getEmail() : "N/A";
    }

    public int getOverallRating() {
        return rating >= 1 ? rating : 5;
    }

    public String getBestFeature() {
        return userExperience != null && !userExperience.isBlank() ? userExperience : "Resume Analysis";
    }

    public String getComments() {
        if (improvementSuggestions != null && !improvementSuggestions.isBlank()) {
            return improvementSuggestions;
        }
        if (missingFeatures != null && !missingFeatures.isBlank()) {
            return missingFeatures;
        }
        return "No specific comments provided.";
    }
}
