package com.smartresume.controller;

import com.smartresume.dto.FeedbackDto;
import com.smartresume.model.User;
import com.smartresume.service.FeedbackService;
import com.smartresume.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    @GetMapping
    public String showFeedbackPage(Model model) {
        model.addAttribute("feedbackDto", new FeedbackDto());
        Map<String, Object> stats = feedbackService.getFeedbackStats();
        model.addAttribute("stats", stats);
        userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
        return "feedback/form";
    }

    @PostMapping("/submit")
    public String submitFeedback(@Valid @ModelAttribute("feedbackDto") FeedbackDto feedbackDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("stats", feedbackService.getFeedbackStats());
            userService.getCurrentUser().ifPresent(u -> model.addAttribute("currentUser", u));
            return "feedback/form";
        }

        Optional<User> currentUserOpt = userService.getCurrentUser();
        feedbackService.submitFeedback(feedbackDto, currentUserOpt.orElse(null));
        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your feedback has been submitted successfully.");

        return "redirect:/feedback?submitted=true";
    }
}
