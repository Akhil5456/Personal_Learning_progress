package com.learning.tracker.controller;

import com.learning.tracker.model.Subject;
import com.learning.tracker.service.StudySessionService;
import com.learning.tracker.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final SubjectService subjectService;
    private final StudySessionService studySessionService;

    @Autowired
    public ReportController(SubjectService subjectService, StudySessionService studySessionService) {
        this.subjectService = subjectService;
        this.studySessionService = studySessionService;
    }

    private String getLoggedInUser(HttpSession session) {
        return (String) session.getAttribute("loggedInUser");
    }

    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        model.addAttribute("subjects", subjectService.getSubjectsByUser(username));
        model.addAttribute("totalStudyMinutes", studySessionService.getTotalStudyMinutes(username));
        model.addAttribute("totalSubjects", subjectService.getTotalSubjects(username));
        model.addAttribute("completionPercentage", subjectService.getOverallCompletionPercentage(username));

        return "reports";
    }

    @GetMapping("/goals")
    public String manageGoals(HttpSession session, Model model) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        List<Subject> subjects = subjectService.getSubjectsByUser(username);
        
        // Let's attach some dynamic data for the view
        model.addAttribute("subjects", subjects);
        
        return "goals";
    }
}
