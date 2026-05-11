package com.learning.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "study_sessions")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate studyDate;
    
    private int durationMinutes;
    
    @Column(length = 500)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public StudySession() {
        this.studyDate = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getStudyDate() { return studyDate; }
    public void setStudyDate(LocalDate studyDate) { this.studyDate = studyDate; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Topic getTopic() { return topic; }
    public void setTopic(Topic topic) { this.topic = topic; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
