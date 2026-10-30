package com.smartresume.controller;

import com.smartresume.agent.CareerCopilotOrchestrator;
import com.smartresume.agent.CareerCopilotOrchestrator.CopilotResponse;
import com.smartresume.model.User;
import com.smartresume.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/copilot")
public class CareerCopilotController {

    private final CareerCopilotOrchestrator copilotOrchestrator;
    private final UserService userService;

    public CareerCopilotController(CareerCopilotOrchestrator copilotOrchestrator, UserService userService) {
        this.copilotOrchestrator = copilotOrchestrator;
        this.userService = userService;
    }

    @GetMapping
    public String showCopilotInterface(Model model) {
        User currentUser = userService.getCurrentUser().orElse(null);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("toolRegistry", copilotOrchestrator.getToolRegistry());
        return "career/copilot";
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ResponseEntity<CopilotResponse> chat(@RequestBody Map<String, String> requestPayload) {
        String message = requestPayload.get("message");
        boolean confirmAction = Boolean.parseBoolean(requestPayload.getOrDefault("confirmAction", "false"));
        String actionToken = requestPayload.get("actionToken");

        User user = userService.getCurrentUser().orElse(null);
        CopilotResponse response = copilotOrchestrator.processUserQuery(message, user, confirmAction, actionToken);

        return ResponseEntity.ok(response);
    }
}
