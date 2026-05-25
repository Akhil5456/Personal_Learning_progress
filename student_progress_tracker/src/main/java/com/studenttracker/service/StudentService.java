package com.studenttracker.service;

import com.studenttracker.model.Student;
import com.studenttracker.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Student registerStudent(String username, String rawPassword,
                                 String answer1, String answer2, String answer3) {
        if (studentRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(passwordEncoder.encode(rawPassword));
        student.setSecurityAnswer1(passwordEncoder.encode(answer1));
        student.setSecurityAnswer2(passwordEncoder.encode(answer2));
        student.setSecurityAnswer3(passwordEncoder.encode(answer3));
        return studentRepository.save(student);
    }

    public boolean verifySecurityAnswers(String username, String ans1, String ans2, String ans3) {
        Optional<Student> opt = studentRepository.findByUsername(username);
        if (opt.isEmpty()) return false;
        Student student = opt.get();
        return passwordEncoder.matches(ans1, student.getSecurityAnswer1()) &&
               passwordEncoder.matches(ans2, student.getSecurityAnswer2()) &&
               passwordEncoder.matches(ans3, student.getSecurityAnswer3());
    }

    @Transactional
    public void changePassword(String username, String newRawPassword) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        student.setPassword(passwordEncoder.encode(newRawPassword));
        studentRepository.save(student);
    }

    public Optional<Student> findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
}
