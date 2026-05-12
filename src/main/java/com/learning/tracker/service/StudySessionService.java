package com.learning.tracker.service;

import com.learning.tracker.model.StudySession;
import com.learning.tracker.model.Subject;
import com.learning.tracker.model.Topic;
import com.learning.tracker.model.User;
import com.learning.tracker.repository.StudySessionRepository;
import com.learning.tracker.repository.SubjectRepository;
import com.learning.tracker.repository.TopicRepository;
import com.learning.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public StudySessionService(StudySessionRepository studySessionRepository, UserRepository userRepository,
                               SubjectRepository subjectRepository, TopicRepository topicRepository) {
        this.studySessionRepository = studySessionRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
    }

    public List<StudySession> getSessionsByUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(studySessionRepository::findByUserOrderByStudyDateDesc).orElse(List.of());
    }

    public void saveSession(StudySession session, Long subjectId, Long topicId, String username) {
        if (session == null) {
            throw new IllegalArgumentException("Study session cannot be null");
        }
        if (subjectId == null) {
            throw new IllegalArgumentException("Subject ID cannot be null");
        }
        if (topicId == null) {
            throw new IllegalArgumentException("Topic ID cannot be null");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        Optional<Topic> topicOpt = topicRepository.findById(topicId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        if (subjectOpt.isEmpty()) {
            throw new IllegalArgumentException("Subject not found with ID: " + subjectId);
        }
        if (topicOpt.isEmpty()) {
            throw new IllegalArgumentException("Topic not found with ID: " + topicId);
        }

        session.setUser(userOpt.get());
        session.setSubject(subjectOpt.get());
        session.setTopic(topicOpt.get());
        
        if (session.getStudyDate() == null) {
            session.setStudyDate(java.time.LocalDate.now());
        }
        
        studySessionRepository.save(session);
    }

    public int getTotalStudyMinutes(String username) {
        List<StudySession> sessions = getSessionsByUser(username);
        return sessions.stream().mapToInt(StudySession::getDurationMinutes).sum();
    }
}
