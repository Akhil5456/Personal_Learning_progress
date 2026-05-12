package com.learning.tracker.controller;

import com.learning.tracker.model.StudySession;
import com.learning.tracker.service.StudySessionService;
import com.learning.tracker.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/study-sessions")
public class StudySessionController {

    private final StudySessionService studySessionService;
    private final SubjectService subjectService;

    @Autowired
    public StudySessionController(StudySessionService studySessionService, SubjectService subjectService) {
        this.studySessionService = studySessionService;
        this.subjectService = subjectService;
    }

    private String getLoggedInUser(HttpSession session) {
        return (String) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listSessions(HttpSession session, Model model) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        model.addAttribute("sessions", studySessionService.getSessionsByUser(username));
        model.addAttribute("totalMinutes", studySessionService.getTotalStudyMinutes(username));
        return "study-sessions";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        model.addAttribute("studySession", new StudySession());
        model.addAttribute("subjects", subjectService.getSubjectsByUser(username));
        return "study-session-form";
    }

    @PostMapping("/save")
    public String saveSession(@ModelAttribute StudySession studySession, 
                              @RequestParam Long subjectId, 
                              @RequestParam Long topicId, 
                              HttpSession session, 
                              RedirectAttributes redirectAttributes) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        try {
            studySessionService.saveSession(studySession, subjectId, topicId, username);
            redirectAttributes.addFlashAttribute("success", "Study session recorded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save study session: " + e.getMessage());
            return "redirect:/study-sessions/add";
        }
        return "redirect:/study-sessions";
    }
}
