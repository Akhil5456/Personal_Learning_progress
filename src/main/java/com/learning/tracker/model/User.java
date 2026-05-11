package com.learning.tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String username;
    private String password;
    
    // Security Questions
    private String friendName;
    private String schoolName;
    private String petName;

    // Constructors
    public User() {}

    public User(String fullName, String username, String password, String friendName, String schoolName, String petName) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.friendName = friendName;
        this.schoolName = schoolName;
        this.petName = petName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }

    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
}
