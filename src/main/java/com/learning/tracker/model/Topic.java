package com.learning.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String status = "Pending"; // Pending or Completed
    
    private LocalDate completionDate;
    
    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public Topic() {}

    public Topic(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
}
