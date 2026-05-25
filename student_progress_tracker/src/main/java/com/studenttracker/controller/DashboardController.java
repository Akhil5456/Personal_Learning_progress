package com.studenttracker.controller;

import com.studenttracker.model.Student;
import com.studenttracker.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final StudentService studentService;

    @Autowired
    public DashboardController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Load the logged‑in student's information
        Student student = studentService.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("student", student);
        // Additional data for progress can be added here later
        return "dashboard"; // src/main/resources/templates/dashboard.html
    }
}
