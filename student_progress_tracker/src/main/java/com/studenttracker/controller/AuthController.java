package com.studenttracker.controller;

import com.studenttracker.model.Student;
import com.studenttracker.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final StudentService studentService;

    @Autowired
    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Registration page
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // src/main/resources/templates/register.html
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String answer1,
                                      @RequestParam String answer2,
                                      @RequestParam String answer3,
                                      Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        try {
            studentService.registerStudent(username, password, answer1, answer2, answer3);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    // Custom login page (Spring Security will handle POST)
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // src/main/resources/templates/login.html
    }

    // Password reset step 1 – verify security answers
    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset_password_step1"; // template step 1
    }

    @PostMapping("/reset-password")
    public String processResetStep1(@RequestParam String username,
                                    @RequestParam String answer1,
                                    @RequestParam String answer2,
                                    @RequestParam String answer3,
                                    Model model) {
        boolean ok = studentService.verifySecurityAnswers(username, answer1, answer2, answer3);
        if (!ok) {
            model.addAttribute("error", "Security answers are incorrect");
            return "reset_password_step1";
        }
        model.addAttribute("username", username);
        return "reset_password_step2"; // template step 2 where new password set
    }

    @PostMapping("/reset-password/confirm")
    public String processResetStep2(@RequestParam String username,
                                    @RequestParam String newPassword,
                                    @RequestParam String confirmPassword,
                                    Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("username", username);
            return "reset_password_step2";
        }
        try {
            studentService.changePassword(username, newPassword);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "reset_password_step2";
        }
        return "redirect:/login?resetSuccess";
    }
}
