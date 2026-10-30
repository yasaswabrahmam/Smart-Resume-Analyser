package com.smartresume.repository;

import com.smartresume.model.Resume;
import com.smartresume.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserOrderByCreatedAtDesc(User user);
    List<Resume> findAllByOrderByCreatedAtDesc();
    long countByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT r.targetCategory, COUNT(r) FROM Resume r GROUP BY r.targetCategory")
    List<Object[]> countByTargetCategory();
}
