package com.learning.tracker.controller;

import com.learning.tracker.model.User;
import com.learning.tracker.service.AuthService;
import com.learning.tracker.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;
    private final SubjectService subjectService;

    @Autowired
    public AuthController(AuthService authService, SubjectService subjectService) {
        this.authService = authService;
        this.subjectService = subjectService;
    }

    @GetMapping({"/", "/login"})
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if (authService.authenticate(username, password)) {
            session.setAttribute("loggedInUser", username);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute User user,
                                      @RequestParam String confirmPassword,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        if (authService.isUsernameExists(user.getUsername())) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        authService.registerUser(user);
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password/verify")
    public String verifySecurityQuestions(@RequestParam String username,
                                          @RequestParam String friendName,
                                          @RequestParam String schoolName,
                                          @RequestParam String petName,
                                          Model model,
                                          HttpSession session) {
        if (authService.verifySecurityAnswers(username, friendName, schoolName, petName)) {
            session.setAttribute("resetUsername", username);
            model.addAttribute("username", username);
            return "reset-password";
        } else {
            model.addAttribute("error", "Security answers do not match.");
            return "forgot-password";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String newPassword,
                                       @RequestParam String confirmNewPassword,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("resetUsername");
        if (username == null) {
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("username", username);
            return "reset-password";
        }

        authService.updatePassword(username, newPassword);
        session.removeAttribute("resetUsername");
        redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        model.addAttribute("totalSubjects", subjectService.getTotalSubjects(username));
        model.addAttribute("totalTopics", subjectService.getTotalTopics(username));
        model.addAttribute("completedTopics", subjectService.getCompletedTopics(username));
        model.addAttribute("pendingTopics", subjectService.getTotalTopics(username) - subjectService.getCompletedTopics(username));
        model.addAttribute("completionPercentage", subjectService.getOverallCompletionPercentage(username));
        model.addAttribute("activeGoal", subjectService.getActiveGoal(username));
        
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
