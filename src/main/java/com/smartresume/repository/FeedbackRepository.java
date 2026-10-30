package com.smartresume.repository;

import com.smartresume.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByOrderByTimestampDesc();

    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double getAverageRating();

    @Query("SELECT AVG(f.usabilityScore) FROM Feedback f")
    Double getAverageUsabilityScore();

    @Query("SELECT AVG(f.featureSatisfaction) FROM Feedback f")
    Double getAverageFeatureSatisfaction();
}
