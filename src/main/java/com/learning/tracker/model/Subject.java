package com.learning.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(unique = true)
    private String code;
    
    private int daysToComplete;
    
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics = new ArrayList<>();

    public Subject() {
        this.createdAt = LocalDate.now();
    }

    // Helper methods for bidirectional association
    public void addTopic(Topic topic) {
        topics.add(topic);
        topic.setSubject(this);
    }

    public void removeTopic(Topic topic) {
        topics.remove(topic);
        topic.setSubject(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getDaysToComplete() { return daysToComplete; }
    public void setDaysToComplete(int daysToComplete) { this.daysToComplete = daysToComplete; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Topic> getTopics() { return topics; }
    public void setTopics(List<Topic> topics) { this.topics = topics; }
}
