package com.learning.tracker.repository;

import com.learning.tracker.model.StudySession;
import com.learning.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    List<StudySession> findByUserOrderByStudyDateDesc(User user);
    List<StudySession> findByUser(User user);
}
