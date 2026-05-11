package com.learning.tracker.controller;

import com.learning.tracker.model.Subject;
import com.learning.tracker.model.Topic;
import com.learning.tracker.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private String getLoggedInUser(HttpSession session) {
        return (String) session.getAttribute("loggedInUser");
    }

    @GetMapping
    public String listSubjects(@RequestParam(required = false) String keyword, HttpSession session, Model model) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("subjects", subjectService.searchSubjects(username, keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("subjects", subjectService.getSubjectsByUser(username));
        }
        
        return "subjects";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (getLoggedInUser(session) == null) return "redirect:/login";
        
        Subject subject = new Subject();
        // Add one empty topic by default for the dynamic form
        subject.getTopics().add(new Topic("", "Pending"));
        model.addAttribute("subject", subject);
        
        return "subject-form";
    }

    @PostMapping("/save")
    public String saveSubject(@ModelAttribute Subject subject, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = getLoggedInUser(session);
        if (username == null) return "redirect:/login";

        // Remove empty topics that might have been submitted by the dynamic form
        subject.getTopics().removeIf(t -> t.getName() == null || t.getName().trim().isEmpty());

        if (subject.getId() == null && subjectService.isCodeExistsForUser(subject.getCode(), username)) {
            redirectAttributes.addFlashAttribute("error", "Subject Code already exists.");
            return "redirect:/subjects/add";
        }

        subjectService.saveSubject(subject, username);
        redirectAttributes.addFlashAttribute("success", "Subject saved successfully.");
        return "redirect:/subjects";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, HttpSession session, Model model) {
        if (getLoggedInUser(session) == null) return "redirect:/login";
        
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            return "redirect:/subjects";
        }
        
        model.addAttribute("subject", subject);
        return "subject-form";
    }

    @PostMapping("/{id}/delete")
    public String deleteSubject(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (getLoggedInUser(session) == null) return "redirect:/login";
        
        subjectService.deleteSubject(id);
        redirectAttributes.addFlashAttribute("success", "Subject deleted successfully.");
        return "redirect:/subjects";
    }

    @PostMapping("/topic/{topicId}/toggle")
    public String toggleTopic(@PathVariable Long topicId, @RequestParam(required = false) String returnUrl, HttpSession session) {
        if (getLoggedInUser(session) == null) return "redirect:/login";
        
        subjectService.toggleTopicStatus(topicId);
        
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/subjects";
    }

    @GetMapping("/{id}/topics")
    public String manageTopics(@PathVariable Long id, HttpSession session, Model model) {
        if (getLoggedInUser(session) == null) return "redirect:/login";
        
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            return "redirect:/subjects";
        }
        
        model.addAttribute("subject", subject);
        return "topic-management";
    }

    @PostMapping("/topic/{topicId}/update")
    public String updateTopicNotes(@PathVariable Long topicId, 
                                   @RequestParam Long subjectId,
                                   @RequestParam(required = false) String notes, 
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (getLoggedInUser(session) == null) return "redirect:/login";
        
        subjectService.updateTopicNotes(topicId, notes);
        redirectAttributes.addFlashAttribute("success", "Topic notes updated successfully.");
        return "redirect:/subjects/" + subjectId + "/topics";
    }
}
