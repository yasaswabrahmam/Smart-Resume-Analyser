package com.smartresume.repository;

import com.smartresume.model.Resume;
import com.smartresume.model.ResumeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {
    Optional<ResumeAnalysis> findByResume(Resume resume);

    @Query("SELECT AVG(ra.atsScore) FROM ResumeAnalysis ra")
    Double getAverageAtsScore();

    @Query("SELECT AVG(ra.keywordMatchScore) FROM ResumeAnalysis ra")
    Double getAverageKeywordScore();

    @Query("SELECT COUNT(ra) FROM ResumeAnalysis ra WHERE ra.atsScore >= 70")
    Long countHighScoring();
}
