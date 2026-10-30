package com.smartresume.repository;

import com.smartresume.model.AIAnalysis;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, Long> {
    List<AIAnalysis> findByUserOrderByCreatedAtDesc(User user);
    List<AIAnalysis> findByResumeOrderByCreatedAtDesc(Resume resume);
    List<AIAnalysis> findAllByOrderByCreatedAtDesc();

    @Query("SELECT AVG(a.resumeScore) FROM AIAnalysis a")
    Double getAverageResumeScore();

    @Query("SELECT AVG(a.atsScore) FROM AIAnalysis a")
    Double getAverageAtsScore();

    @Query("SELECT a.modelUsed, COUNT(a) FROM AIAnalysis a GROUP BY a.modelUsed")
    List<Object[]> countByModelUsed();

    @Query("SELECT a.targetRole, COUNT(a) FROM AIAnalysis a GROUP BY a.targetRole")
    List<Object[]> countByTargetRole();
}
