package com.bankaiassistant.controller;

import com.bankaiassistant.config.Config;
import com.bankaiassistant.model.User;
import com.bankaiassistant.service.GeminiAPI;
import com.bankaiassistant.service.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession; // For session handling
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/assistant")
public class AssistantController {

    @Autowired
    private GeminiAPI geminiAPI;

    @Autowired
    private Config config;

    @GetMapping
    public String assistant(Model model, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("user_email") == null) {
            return "redirect:/"; // Redirect to home page if not logged in
        }

        // Clear chat history before loading assistant
        geminiAPI.clearHistory();

        // Load user data
        String email = (String) session.getAttribute("user_email");
        List<Map<String, Object>> users = User.loadData(config.getDataFilePath());

        // Find the logged-in user's topics and pass to view
        for (Map<String, Object> user : users) {
            if (user.get("email").equals(email)) {
                List<Map<String, String>> topics = (List<Map<String, String>>) user.get("topics");
                model.addAttribute("topics", topics);
                break;
            }
        }

        return "assistant";
    }

    @PostMapping("/get_response")
    @ResponseBody
    public Map<String, String> getResponse(@RequestBody Map<String, String> requestData) {
        String userInput = requestData.get("message");

        // Return error if input is empty
        if (userInput == null || userInput.isEmpty()) {
            return Map.of("error", "No input provided");
        }

        // Generate AI response
        String aiResponse = geminiAPI.getResponse(userInput);
        return Map.of("response", aiResponse);
    }

    @PostMapping("/get_topic")
    @ResponseBody
    public Map<String, String> getTopic(@RequestBody Map<String, String> requestData, HttpSession session) {
        String userInput = requestData.get("message");

        if (userInput == null || userInput.isEmpty()) {
            return Map.of("error", "No input provided");
        }

        // Generate new topic using Gemini
        Topic topic = geminiAPI.generateTopic(userInput);

        // If the topic creation failed (empty title or text), return an error
        if (topic.getTitle().isEmpty() || topic.getText().isEmpty()) {
            return Map.of("error", "Failed to generate a valid topic");
        }

        // Get user email from session
        String email = (String) session.getAttribute("user_email");
        if (email == null) {
            return Map.of("error", "User not authenticated");
        }

        int index = new Random().nextInt(3);

        // Create new topic map
        Map<String, String> newTopic = Map.of(
                "title", topic.getTitle(),
                "text", topic.getText()
        );

        // Update topic in storage
        User.updateTopic(config.getDataFilePath(), email, index, newTopic);

        // Return the newly generated topic
        return newTopic;
    }

}
