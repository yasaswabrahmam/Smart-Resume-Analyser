package com.smartresume.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class FeedbackDto {
    @Min(1) @Max(5)
    private int rating = 5;

    @Min(1) @Max(5)
    private int usabilityScore = 5;

    @Min(1) @Max(5)
    private int featureSatisfaction = 5;

    private String missingFeatures;
    private String improvementSuggestions;
    private String userExperience;

    public FeedbackDto() {
    }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public int getUsabilityScore() { return usabilityScore; }
    public void setUsabilityScore(int usabilityScore) { this.usabilityScore = usabilityScore; }
    public int getFeatureSatisfaction() { return featureSatisfaction; }
    public void setFeatureSatisfaction(int featureSatisfaction) { this.featureSatisfaction = featureSatisfaction; }
    public String getMissingFeatures() { return missingFeatures; }
    public void setMissingFeatures(String missingFeatures) { this.missingFeatures = missingFeatures; }
    public String getImprovementSuggestions() { return improvementSuggestions; }
    public void setImprovementSuggestions(String improvementSuggestions) { this.improvementSuggestions = improvementSuggestions; }
    public String getUserExperience() { return userExperience; }
    public void setUserExperience(String userExperience) { this.userExperience = userExperience; }
}
