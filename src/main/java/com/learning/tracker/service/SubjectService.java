package com.learning.tracker.service;

import com.learning.tracker.model.Subject;
import com.learning.tracker.model.Topic;
import com.learning.tracker.model.User;
import com.learning.tracker.repository.SubjectRepository;
import com.learning.tracker.repository.TopicRepository;
import com.learning.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public List<Subject> getSubjectsByUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(subjectRepository::findByUser).orElse(List.of());
    }

    public List<Subject> searchSubjects(String username, String keyword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return List.of();
        
        List<Subject> allSubjects = subjectRepository.findByUser(user.get());
        if (keyword == null || keyword.trim().isEmpty()) return allSubjects;
        
        String lowerKeyword = keyword.toLowerCase();
        
        return allSubjects.stream()
                .filter(s -> s.getName().toLowerCase().contains(lowerKeyword) || 
                             s.getCode().toLowerCase().contains(lowerKeyword) ||
                             s.getTopics().stream().anyMatch(t -> t.getName().toLowerCase().contains(lowerKeyword)))
                .toList();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public boolean isCodeExistsForUser(String code, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> subjectRepository.existsByCodeAndUser(code, u)).orElse(false);
    }

    public void saveSubject(Subject subject, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            subject.setUser(user.get());
            if (subject.getTopics() != null) {
                for (Topic t : subject.getTopics()) {
                    t.setSubject(subject);
                }
            }
            subjectRepository.save(subject);
        }
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    public void toggleTopicStatus(Long topicId) {
        Optional<Topic> topicOpt = topicRepository.findById(topicId);
        if (topicOpt.isPresent()) {
            Topic topic = topicOpt.get();
            if ("Completed".equals(topic.getStatus())) {
                topic.setStatus("Pending");
                topic.setCompletionDate(null);
            } else {
                topic.setStatus("Completed");
                topic.setCompletionDate(LocalDate.now());
            }
            topicRepository.save(topic);
        }
    }

    public void updateTopicNotes(Long topicId, String notes) {
        Optional<Topic> topicOpt = topicRepository.findById(topicId);
        if (topicOpt.isPresent()) {
            Topic topic = topicOpt.get();
            topic.setNotes(notes);
            topicRepository.save(topic);
        }
    }

    // Analytics Methods
    public int getTotalSubjects(String username) {
        return getSubjectsByUser(username).size();
    }

    public int getTotalTopics(String username) {
        return getSubjectsByUser(username).stream()
                .mapToInt(s -> s.getTopics().size())
                .sum();
    }

    public int getCompletedTopics(String username) {
        return (int) getSubjectsByUser(username).stream()
                .flatMap(s -> s.getTopics().stream())
                .filter(t -> "Completed".equals(t.getStatus()))
                .count();
    }

    public int getOverallCompletionPercentage(String username) {
        int total = getTotalTopics(username);
        if (total == 0) return 0;
        int completed = getCompletedTopics(username);
        return (int) (((double) completed / total) * 100);
    }

    public String getActiveGoal(String username) {
        List<Subject> subjects = getSubjectsByUser(username);
        Subject nearest = null;
        long minDaysRemaining = Long.MAX_VALUE;

        for (Subject s : subjects) {
            LocalDate deadline = s.getCreatedAt().plusDays(s.getDaysToComplete());
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), deadline);
            
            // Check if subject is already completed
            boolean isCompleted = s.getTopics().stream().allMatch(t -> "Completed".equals(t.getStatus()));
            
            if (!isCompleted && daysRemaining < minDaysRemaining) {
                minDaysRemaining = daysRemaining;
                nearest = s;
            }
        }

        if (nearest == null) {
            return "No pending goals";
        }

        if (minDaysRemaining < 0) {
             return nearest.getName() + " (Overdue by " + Math.abs(minDaysRemaining) + " days)";
        }
        return nearest.getName() + " (" + minDaysRemaining + " days remaining)";
    }
}
