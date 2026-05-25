package com.learning.tracker.repository;

import com.learning.tracker.model.Subject;
import com.learning.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    List<Subject> findByUser(User user);
    
    @Query("SELECT s FROM Subject s WHERE s.user = :user AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.code) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Subject> searchByUserAndKeyword(@Param("user") User user, @Param("keyword") String keyword);
    
    boolean existsByCodeAndUser(String code, User user);
}
