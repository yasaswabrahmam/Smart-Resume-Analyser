package com.smartresume.repository;

import com.smartresume.model.JobMatch;
import com.smartresume.model.Resume;
import com.smartresume.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobMatchRepository extends JpaRepository<JobMatch, Long> {
    List<JobMatch> findByUserOrderByCreatedAtDesc(User user);
    List<JobMatch> findByUserAndStatusOrderByCreatedAtDesc(User user, String status);
    List<JobMatch> findByResumeOrderByCreatedAtDesc(Resume resume);
    long countByUser(User user);
    long countByUserAndStatus(User user, String status);
}
